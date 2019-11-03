package ejb.session.stateless;

import entity.RentalRateEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidFieldEnteredException;
import util.exception.RentalRateExistException;
import util.exception.UnknownPersistenceException;


public interface RentalRateEntitySessionBeanRemote {
public RentalRateEntity createNewRentalRate(Long categoryId, RentalRateEntity newRentalRate)
             throws  RentalRateExistException,CategoryNotFoundException, GeneralException;
public List<RentalRateEntity> retrieveAllRentalRates();
}
