package ejb.session.stateless;

import entity.CategoryEntity;
import entity.RentalRateEntity;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
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
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    
    public RentalRateEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity newRentalRate)
             throws  RentalRateExistException,CategoryNotFoundException, GeneralException
    {
        /*Set<ConstraintViolation<RentalRateEntity>>constraintViolations = validator.validate(rentalRateEntity);
  
        if(constraintViolations.isEmpty())
        {  
            try
            {   
                CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
                rentalRateEntity.setCategory(category);
                category.getRentalRate().add(rentalRateEntity);
                em.persist(rentalRateEntity);
                em.flush();
                return rentalRateEntity;
            }
            
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new RentalRateExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InvalidFieldEnteredException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }*/
         
        try
        {
            CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
            
            em.persist(newRentalRate);
            
             newRentalRate.setCategory(category);
             category.getRentalRate().add(newRentalRate);
            
  
            em.flush();
            em.refresh(newRentalRate);

            return newRentalRate;
        }
        catch(CategoryNotFoundException ex)
        {
            throw new CategoryNotFoundException("Unable to create new ATM card as the customer record does not exist");
        }
       
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new RentalRateExistException("Atm card with same card number already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    
    public List<RentalRateEntity> retrieveAllRentalRates(){
        Query query = em.createQuery("SELECT rr FROM RentalRateEntity rr");
        return query.getResultList();
    }
    
    public RentalRateEntity retrieveRentalRateByRentalId(Long rentalRateId) 
            //throws RentalRateNotFoundException
    {
        
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class,rentalRateId);
        
       // if (rentalRateEntity != null){
            return rentalRateEntity;
      /*  } else {
            throw new RentalRateNotFoundException("Rental Rate ID" + rentalRateId + "does not exist");
        }*/
    }
    
    
    public void updateRentalRate(RentalRateEntity rentalRateEntity) throws RentalRateNotFoundException,UpdateRentalRateException{
        
        if (rentalRateEntity.getRentalRateId() != null){
            
            RentalRateEntity rentalRateToUpdate = retrieveRentalRateByRentalId(rentalRateEntity.getRentalRateId());
            
            if (rentalRateToUpdate.getRentalRateName().equals(rentalRateEntity.getRentalRateName())){
                //update
            } else {
                throw new UpdateRentalRateException();
            }
                
        } else {
            throw new RentalRateNotFoundException();
        }
    }
    
//    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException,DeleteRentalRateException{
//        
//        RentalRateEntity rentalEntityToRemove = retrieveRentalRateByRentalId(rentalRateId);
//   
//        if (rentalEntityToRemove != null) {
//            if () {
//
//            } else {
//
//                rentalEntityToRemove.setDisabled(true);
//                throw new DeleteRentalRateException();
//            }
//        } else {
//            throw new RentalRateNotFoundException("Rental rate" + rentalRateId + "does not exist");
//        }
//        
//    }
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
