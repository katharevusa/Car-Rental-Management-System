package ejb.session.stateless;

import entity.CustomerEntity;
import entity.PartnerEntity;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidFieldEnteredException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegistrationFailureException;
import util.exception.UnknownPersistenceException;


public interface CustomerEntitySessionBeanRemote {
    
    public Long createNewCustomer(CustomerEntity customerEntity) throws InputDataValidationException, UnknownPersistenceException;
    
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public CustomerEntity login(String username, String password) throws InvalidLoginCredentialException;
    
    public void register(String username, String password, String email, String mobileNumber) throws RegistrationFailureException;
public CustomerEntity registerationInWeb(Long partnerId,String username,String password,String email, String mobileNumber)throws RegistrationFailureException;
}
