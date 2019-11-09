package ejb.session.stateless;


import entity.CategoryEntity;
import entity.ModelEntity;
import java.util.List;
import java.util.Set;
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
    
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    

    public ModelEntitySessionBean() {
        
    }

    
    @Override
    public ModelEntity createNewModel(ModelEntity newModelEntity,Long categoryId) throws CreateNewModelFailureException{
        
        try {
            
            CategoryEntity categoryEntity = categoryEntitySessionBeanLlocal.retrieveCategoryByCategoryId(categoryId);
            newModelEntity.setCategoryEntity(categoryEntity);
            categoryEntity.getModels().add(newModelEntity);
            
            em.persist(newModelEntity);
            em.flush();
            
            return newModelEntity;
        } catch (PersistenceException ex1) {
//            eJBContext.setRollbackOnly();
            throw new CreateNewModelFailureException(ex1.getMessage());
        } catch (CategoryNotFoundException ex2){
//            eJBContext.setRollbackOnly();
            throw new CreateNewModelFailureException(ex2.getMessage());
        }
            
    }
    
    
    @Override
    public List<ModelEntity> retrieveAllModel(){
        
        Query query = em.createQuery("SELECT m FROM ModelEntity m ORDER BY m.categoryEntity.name ASC, m.modelName ASC");
        return query.getResultList();
    }
    
    @Override
    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException{
        
        ModelEntity modelEntity = em.find(ModelEntity.class, modelId);
        
        if (modelEntity != null){
            return modelEntity;
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + "does not exist!");
        }
    }
    
    @Override
    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException{
    
        Query query = em.createQuery("SELECT m FROM ModelEntity m WHERE m.modelName = :inName");
        query.setParameter("inName", modelName);
        
        try {
            return (ModelEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex){
            throw new ModelNotFoundException("Model name " + modelName + "does not exist!");
        }
        
    }

    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ModelEntity>> constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    
    @Override
    public Long deleteModel(Long modelId) throws DeleteModelException{
        
        try{
            
            ModelEntity modelToDelete = retrieveModelByModelId(modelId);
            if (modelToDelete.getCars().isEmpty()){
                em.remove(modelToDelete);
                return modelToDelete.getModelId();
            } else {
                modelToDelete.setDisabled(true);
                throw new DeleteModelException();
            }
            
        }catch(ModelNotFoundException ex1){
            throw new DeleteModelException("Model ID " + modelId + "does not exist.");
        } catch (DeleteModelException ex2){
            throw new DeleteModelException("Model " + modelId + "cannot be deleted, but it has been marked as disabled.");
        }
        
    }
    
}
