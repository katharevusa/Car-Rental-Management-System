/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidFieldEnteredException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegistrationFailureException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface CustomerEntitySessionBeanLocal {

    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Long createNewCustomer(CustomerEntity customerEntity) throws InvalidFieldEnteredException,UnknownPersistenceException;

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public CustomerEntity login(String username, String password) throws InvalidLoginCredentialException;

    public void register(String username, String password, String email, String mobileNumber) throws RegistrationFailureException;
    
}
