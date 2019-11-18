/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.InvalidFieldEnteredException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface RentalRateEntitySessionBeanLocal {
    public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity newRentalRate)
             throws CategoryNotFoundException, GeneralException;
    public List<RentalRateEntity> retrieveAllRentalRates();
    public RentalRateEntity retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException;
  public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;
    public void updateRentalRate(RentalRateEntity rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;
    public double checkForExistenceOfRentalRate(Long selectedCategoryId,LocalDateTime pickupDateTime,
            LocalDateTime returnDateTime) throws CategoryNotFoundException, RentalRateNotFoundException;
}
