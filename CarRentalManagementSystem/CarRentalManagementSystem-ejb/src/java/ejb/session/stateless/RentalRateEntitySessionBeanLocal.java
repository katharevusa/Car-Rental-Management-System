/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateEntity;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidFieldEnteredException;
import util.exception.RentalRateExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface RentalRateEntitySessionBeanLocal {
    public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity rentalRateEntity) 
            throws InvalidFieldEnteredException,RentalRateExistException,UnknownPersistenceException, CategoryNotFoundException;
}
