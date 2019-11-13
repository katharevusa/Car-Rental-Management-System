package ejb.session.stateless;

import entity.RentalRateEntity;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

public interface RentalRateEntitySessionBeanRemote {

    public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity newRentalRate)
            throws RentalRateExistException, CategoryNotFoundException, GeneralException;

    public List<RentalRateEntity> retrieveAllRentalRates();

    public RentalRateEntity retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException;

    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRateEntity rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;

    public double checkForExistenceOfRentalRate(Long selectedCategoryId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime) throws CategoryNotFoundException, RentalRateNotFoundException;

}
