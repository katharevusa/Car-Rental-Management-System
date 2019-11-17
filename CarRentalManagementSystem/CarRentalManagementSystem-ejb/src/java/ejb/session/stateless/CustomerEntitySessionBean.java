package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.Set;
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
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidFieldEnteredException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegistrationFailureException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)

public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    @Override
    public Long createNewCustomer(CustomerEntity customerEntity) throws InputDataValidationException, UnknownPersistenceException {

        try {
            Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(customerEntity);

            if (constraintViolations.isEmpty()) {
                em.persist(customerEntity);
                em.flush();

                return customerEntity.getCustomerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } catch (PersistenceException ex){
            throw new UnknownPersistenceException(ex.getMessage());
        }

        
    }
    
    @Override
    public void register(String username,String password,String email,String mobileNumber) throws RegistrationFailureException{
        
        try{
            
            CustomerEntity customerEntity = new CustomerEntity(username,password,email,mobileNumber);
            createNewCustomer(customerEntity);
            
        } catch (InputDataValidationException | UnknownPersistenceException ex){
            
            throw new RegistrationFailureException(ex.getMessage());
            
        }
    }
    
    
    @Override
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException{
        
        CustomerEntity customerEntity = em.find(CustomerEntity.class,customerId);
        
        if (customerEntity != null){
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }
    
    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException{

        try {
            Query query = em.createQuery("SELECT c FROM CustomerEntity c where c.username = :inUsername");
            query.setParameter("inUsername", username);
            
            return (CustomerEntity) query.getSingleResult();
        } catch (PersistenceException ex) {
            System.out.println("yes");
            throw new CustomerNotFoundException("Customer Username " + username + "does not exist.");
        }

    }
    
    @Override
    public CustomerEntity login(String username,String password) throws InvalidLoginCredentialException{
        
        try {
            
            CustomerEntity customerEntity = retrieveCustomerByUsername(username);
            
            if (customerEntity.getPassword().equals(password)){
                
                //to preload the assoicated entities before returning
                return customerEntity;
                
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
            
        } catch (CustomerNotFoundException ex){
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
