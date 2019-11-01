package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidFieldEnteredException;

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
    public Long createNewCustomer(CustomerEntity customerEntity) throws InvalidFieldEnteredException{
        
        try 
        {
            em.persist(customerEntity);
            em.flush();
            
            return customerEntity.getCustomerId();
        } 
        catch (PersistenceException ex){
            throw new InvalidFieldEnteredException();
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
    
    

    
}
