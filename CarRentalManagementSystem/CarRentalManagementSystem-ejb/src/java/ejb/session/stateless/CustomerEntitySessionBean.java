package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
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

    public CustomerEntitySessionBean() {
    }

    
    @Override
    public Long createNewCustomer(CustomerEntity customerEntity) throws InvalidFieldEnteredException, UnknownPersistenceException{
        
        try 
        {
            em.persist(customerEntity);
            em.flush();
            
            return customerEntity.getCustomerId();
        } 
        catch (PersistenceException ex){
            
            throw new InvalidFieldEnteredException();
//            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
//            {
//                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
//                {
//                    throw new InvalidFieldEnteredException();
//                }
//                else
//                {
//                    throw new UnknownPersistenceException(ex.getMessage());
//                }
//            }
//            else
//            {
//                throw new UnknownPersistenceException(ex.getMessage());
//            }
                
        }
    }
    
    @Override
    public void register(String username,String password,String email,String mobileNumber) throws RegistrationFailureException{
        
        try{
            
            CustomerEntity customerEntity = new CustomerEntity(username,password,email,mobileNumber);
            createNewCustomer(customerEntity);
            
        } catch (InvalidFieldEnteredException | UnknownPersistenceException ex){
            
            throw new RegistrationFailureException("Usernmae already exists.");
            
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
    
}
