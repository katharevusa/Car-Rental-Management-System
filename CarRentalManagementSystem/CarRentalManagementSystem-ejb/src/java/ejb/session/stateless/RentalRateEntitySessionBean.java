package ejb.session.stateless;

import entity.CategoryEntity;

import entity.ModelEntity;

import entity.RentalRateEntity;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import util.exception.ModelNotFoundException;

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


    


    public RentalRateEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }

    @Override
    public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity newRentalRate)
            throws  CategoryNotFoundException, GeneralException {
       
            CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

            em.persist(newRentalRate);
            newRentalRate.setCategory(category);
            category.getRentalRate().add(newRentalRate);

            em.flush();
            em.refresh(newRentalRate);
            return newRentalRate;
        
    }

    @Override
    public List<RentalRateEntity> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT rr FROM RentalRateEntity rr");

        return query.getResultList();
    }

    @Override
    public RentalRateEntity retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException {

        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class, rentalRateId);

        if (rentalRateEntity != null) {
            return rentalRateEntity;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public void updateRentalRate(RentalRateEntity rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException {
        if (rentalRate != null && rentalRate.getRentalRateId() != null) {
            Set<ConstraintViolation<RentalRateEntity>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                RentalRateEntity rentalRateEntityToUpdate = retrieveRentalRateByRentalId(rentalRate.getRentalRateId());

                if (rentalRateEntityToUpdate.getRentalRateId().equals(rentalRate.getRentalRateId())) {
                    rentalRateEntityToUpdate.setRentalRateName(rentalRate.getRentalRateName());
                    rentalRateEntityToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                    rentalRateEntityToUpdate.setStartDateTime(rentalRate.getStartDateTime());
                    rentalRateEntityToUpdate.setEndDateTime(rentalRate.getEndDateTime());
                } else {
                    throw new UpdateRentalRateException("ID of rental rate to be updated does not match the existing rental rate");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new RentalRateNotFoundException("Rental rate ID not provided for product to be updated");
        }
    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRateEntity rentalRateEntityToRemove = retrieveRentalRateByRentalId(rentalRateId);
        rentalRateEntityToRemove.getCategory().getRentalRate().remove(rentalRateEntityToRemove);
        em.remove(rentalRateEntityToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRateEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    
    @Override
    public double checkForExistenceOfRentalRate(Long selectedCategoryId,LocalDateTime pickupDateTime,
            LocalDateTime returnDateTime) throws CategoryNotFoundException, RentalRateNotFoundException{
        
        CategoryEntity category;
        ModelEntity model;
        try{
            category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(selectedCategoryId);
        }catch(CategoryNotFoundException ex){
            throw new CategoryNotFoundException("Category Id " + selectedCategoryId + " does not exist!");
        }
        
        boolean rentalRateExist;
        LocalDateTime currStartDateTime = pickupDateTime;
        LocalDateTime next24HourDateTime = currStartDateTime.plusDays(1);
        double totalRate = 0;
        
        List<RentalRateEntity> rentalRates = category.getRentalRate();
        int count = 0;
        
        
        while(next24HourDateTime.isBefore(returnDateTime)){
            
            System.out.println(next24HourDateTime + " : " + returnDateTime);
            rentalRateExist = false;
            List<Double> rates = new ArrayList<>();
            for(RentalRateEntity rentalRate:rentalRates){
                if(currStartDateTime.compareTo(rentalRate.getStartDateTime())==0 || currStartDateTime.isAfter(rentalRate.getStartDateTime())){
                    if(next24HourDateTime.compareTo(rentalRate.getEndDateTime()) == 0 || next24HourDateTime.isBefore(rentalRate.getEndDateTime())){
                        rentalRateExist = true;
                        rates.add(rentalRate.getRatePerDay());
                    }
                }
            }
            
            
            if(!rentalRateExist){
                throw new RentalRateNotFoundException();
            } else {
                
                
                double minimumRate = rates.get(0);
                for(int i=0;i<rates.size();i++){
                    minimumRate = Math.min(minimumRate,rates.get(i));
                }
                totalRate += minimumRate;
            }
            
            next24HourDateTime = next24HourDateTime.plusDays(1);
            count++;
            
            if(count == 10000){
                break;
            }
            
                
        }
        
        if(count == 10000){
            System.out.println("break becos of count");
        }
        
        //after exit from the previous while loop, next24HourDateTime can only be after returnDateTime
        //check for the last period of reservation time one more time through the same process
        if(next24HourDateTime.isAfter(returnDateTime) || next24HourDateTime.isEqual(returnDateTime)){
            currStartDateTime = next24HourDateTime.minusDays(1);
            next24HourDateTime = returnDateTime;
            
            rentalRateExist = false;
            List<Double> rates = new ArrayList<>();
            for(RentalRateEntity rentalRate:rentalRates){
                if(currStartDateTime.compareTo(rentalRate.getStartDateTime())==0 || currStartDateTime.isAfter(rentalRate.getStartDateTime())){
                    if(next24HourDateTime.compareTo(rentalRate.getEndDateTime()) == 0 || next24HourDateTime.isBefore(rentalRate.getEndDateTime())){
                        rentalRateExist = true;
                        rates.add(rentalRate.getRatePerDay());
                    }
                }
            }
            
            
            if(!rentalRateExist){
                throw new RentalRateNotFoundException();
            } else {
                
                double minimumRate = rates.get(0);
                for(int i=0;i<rates.size();i++){
                    minimumRate = Math.min(minimumRate,rates.get(i));
                }
                totalRate += minimumRate;
            }
        }
        
        return totalRate;
    }
}
