package ejb.session.stateless;

import entity.CarEntity;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.InvalidFieldEnteredException;

public interface CarEntitySessionBeanRemote {
     public List<CarEntity> retrieveAllCars();
     
     public CarEntity createNewCar(CarEntity newCarEntity, Long modelId, Long outletId) throws InvalidFieldEnteredException;
    
     public CarEntity retrieveCarByCarId(Long carId) throws CarNotFoundException;
    
}
