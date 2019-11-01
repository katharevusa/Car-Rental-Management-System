/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidFieldEnteredException;

/**
 *
 * @author CHEN BINGSEN
 */
@Remote
public interface CustomerEntitySessionBeanRemote {
    
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Long createNewCustomer(CustomerEntity customerEntity) throws InvalidFieldEnteredException;
}
