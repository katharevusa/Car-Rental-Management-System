package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.ModelEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelException;
import util.exception.UpdateModelFailureException;
import util.exception.ModelIsDisabledException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(ModelEntitySessionBeanLocal.class)
@Remote(ModelEntitySessionBeanRemote.class)

public class ModelEntitySessionBean implements ModelEntitySessionBeanRemote, ModelEntitySessionBeanLocal {

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLlocal;

    @Resource
    private EJBContext eJBContext;
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ModelEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ModelEntity createNewModel(ModelEntity newModelEntity, Long categoryId) throws CreateNewModelFailureException {

        try {

            CategoryEntity categoryEntity = categoryEntitySessionBeanLlocal.retrieveCategoryByCategoryId(categoryId);
            newModelEntity.setCategoryEntity(categoryEntity);
            categoryEntity.getModels().add(newModelEntity);

            em.persist(newModelEntity);
            em.flush();

            return newModelEntity;
        } catch (PersistenceException ex1) {
//            eJBContext.setRollbackOnly();
            throw new CreateNewModelFailureException("Model exists! Cannot create this new Model!");
        } catch (CategoryNotFoundException ex2) {
//            eJBContext.setRollbackOnly();
            throw new CreateNewModelFailureException(ex2.getMessage());
        }

    }

    @Override
    public ModelEntity retrieveModelByMakeAndModel(String make, String model) throws ModelNotFoundException {
        Query query = em.createQuery("SELECT m FROM ModelEntity m WHERE m.make = :inMake AND m.modelName = :inModel");
        query.setParameter("inMake", make);
        query.setParameter("inModel", model);
        try {
            return (ModelEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ModelNotFoundException("Model does not exist!");
        }

    }

    @Override
    public List<ModelEntity> retrieveAllModel() {

        Query query = em.createQuery("SELECT m FROM ModelEntity m ORDER BY m.categoryEntity.name ASC, m.modelName ASC");
        return query.getResultList();
    }

    @Override
    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException {

        ModelEntity modelEntity = em.find(ModelEntity.class, modelId);

        if (modelEntity != null) {
            return modelEntity;
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }

    @Override
    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException {

        Query query = em.createQuery("SELECT m FROM ModelEntity m WHERE m.modelName = :inName");
        query.setParameter("inName", modelName);

        try {
            return (ModelEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ModelNotFoundException("Model name " + modelName + " does not exist!");
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ModelEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Long deleteModel(Long modelId) throws DeleteModelException {

        try {

            ModelEntity modelToDelete = retrieveModelByModelId(modelId);
            
            if (modelToDelete.getCars().isEmpty()) {
                em.remove(modelToDelete);
                return modelToDelete.getModelId();
            } else {
                //if there are cars under this model, cannot delete this model
                modelToDelete.setDisabled(true);
                throw new DeleteModelException();
            }
        } catch (ModelNotFoundException ex1) {
            throw new DeleteModelException("Model ID " + modelId + " does not exist.");
        } catch (DeleteModelException ex2) {
            throw new DeleteModelException("Model " + modelId + " cannot be deleted, but it has been marked as disabled.");
        }

    }

    public List<ModelEntity> retrieveModelsBasedOnCars(List<CarEntity> cars) {

        TreeSet<ModelEntity> models = new TreeSet<>();
        for (CarEntity car : cars) {
            models.add(car.getModelEntity());
        }

        return new ArrayList<ModelEntity>(models);
    }

    @Override
    public void updateModel(ModelEntity model) throws UpdateModelFailureException, InputDataValidationException {
        try {
            if (model != null && model.getModelId() != null && !model.isDisabled()) {
                
                Set<ConstraintViolation<ModelEntity>> constraintViolations = validator.validate(model);

                if (constraintViolations.isEmpty()) {
                    
                    ModelEntity modelToUpdate = retrieveModelByModelId(model.getModelId());

                    if (modelToUpdate.getModelId().equals(model.getModelId())) {
                        modelToUpdate.setMake(model.getMake());
                        modelToUpdate.setModelName(model.getModelName());
                        modelToUpdate.setCategoryEntity(model.getCategoryEntity());
                    } else {
                        throw new UpdateModelFailureException("ID of model to be updated does not match the existing model");
                    }
                } else {
                    throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            } else {
                throw new ModelIsDisabledException("Model cannot be updated as it is being disabled!");
            }
        } catch (ModelNotFoundException ex1) {
            eJBContext.setRollbackOnly();
            throw new UpdateModelFailureException(ex1.getMessage());
        } catch (ModelIsDisabledException ex2) {
            eJBContext.setRollbackOnly();
            throw new UpdateModelFailureException(ex2.getMessage());
        }
    }

}
