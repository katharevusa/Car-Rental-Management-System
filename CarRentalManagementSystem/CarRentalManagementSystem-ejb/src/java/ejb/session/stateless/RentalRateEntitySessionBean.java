package ejb.session.stateless;

import entity.CategoryEntity;
import entity.RentalDayEntity;
import entity.RentalRateEntity;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.DeleteRentalRateException;
import util.exception.GeneralException;
import util.exception.InvalidFieldEnteredException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(RentalRateEntitySessionBeanLocal.class)
@Remote(RentalRateEntitySessionBeanRemote.class)


public class RentalRateEntitySessionBean implements RentalRateEntitySessionBeanRemote, RentalRateEntitySessionBeanLocal {

    
    
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private RentalDayEntitySessionBeanLocal rentalDayEntitySessionBeanLocal;
    
    public RentalRateEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        
    }
    
    @Override
    public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity newRentalRate)
            throws  RentalRateExistException,CategoryNotFoundException, GeneralException
    { 
        try
    {
        CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
        
        em.persist(newRentalRate);
        newRentalRate.setCategory(category);
        category.getRentalRate().add(newRentalRate);
        
        rentalDayEntitySessionBeanLocal.createNewRentalDay(newRentalRate, newRentalRate.getStartDate(), newRentalRate.getEndDate());
        
        em.flush();
        em.refresh(newRentalRate);
        
        return newRentalRate;
    }
    catch(CategoryNotFoundException ex)
    {
        throw new CategoryNotFoundException("Unable to create new rental rate as the categroy does not exist");
    }
    
    catch(PersistenceException ex)
    {
        if(ex.getCause() != null &&
                ex.getCause().getCause() != null &&
                ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
        {
            //how to check for rental rate exists
            throw new RentalRateExistException();
        }
        else
        {
            throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
        }
    }
    }
    
    @Override
    public List<RentalRateEntity> retrieveAllRentalRates(){
        Query query = em.createQuery("SELECT rr FROM RentalRateEntity rr");
        
        return query.getResultList();
    }
    @Override
    public RentalRateEntity retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException
    {
    
    RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId);
        
        if(rentalRateEntity != null)
        {
            return rentalRateEntity;
        }
        else
        {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }               
    }
    
    
    @Override
    public void updateRentalRate(RentalRateEntity rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException
    {
        if(rentalRate != null && rentalRate.getRentalRateId()!= null)
        {
            Set<ConstraintViolation<RentalRateEntity>>constraintViolations = validator.validate(rentalRate);
        
            if(constraintViolations.isEmpty())
            {
                RentalRateEntity rentalRateEntityToUpdate = retrieveRentalRateByRentalId(rentalRate.getRentalRateId());

                if(rentalRateEntityToUpdate.getRentalRateId().equals(rentalRate.getRentalRateId()))
                {
                    rentalRateEntityToUpdate.setRentalRateName(rentalRate.getRentalRateName());
                    rentalRateEntityToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                    rentalRateEntityToUpdate.setStartDate(rentalRate.getStartDate());
                    rentalRateEntityToUpdate.setEndDate(rentalRate.getEndDate());
                }
                else
                {
                    throw new UpdateRentalRateException("ID of rental rate to be updated does not match the existing rental rate");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new RentalRateNotFoundException("Rental rate ID not provided for product to be updated");
        }
    }
    
    
   @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException
    {
        RentalRateEntity rentalRateEntityToRemove = retrieveRentalRateByRentalId(rentalRateId);
        rentalRateEntityToRemove.getCategory().getRentalRate().remove(rentalRateEntityToRemove);
        rentalRateEntityToRemove.getRentalDay().clear();
        em.remove(rentalRateEntityToRemove);
    }

    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRateEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
        
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
