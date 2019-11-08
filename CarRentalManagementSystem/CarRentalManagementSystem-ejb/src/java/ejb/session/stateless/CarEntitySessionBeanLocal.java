package ejb.session.stateless;

import entity.CarEntity;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.InvalidFieldEnteredException;


public interface CarEntitySessionBeanLocal {

    public List<CarEntity> retrieveAllCars();

    public CarEntity retrieveCarByCarId(Long carId) throws CarNotFoundException;
    
}
