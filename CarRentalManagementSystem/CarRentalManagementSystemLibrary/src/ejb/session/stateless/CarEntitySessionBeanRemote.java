package ejb.session.stateless;

import entity.CarEntity;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
import util.exception.OutletNotFoundException;

public interface CarEntitySessionBeanRemote {
    
     public List<CarEntity> retrieveAllCars();
     
     public CarEntity retrieveCarByCarId(Long carId) throws CarNotFoundException;

    public Long createNewCar(CarEntity newCarEntity,String make,String model,Long outletId) throws NewCarCreationException, OutletNotFoundException, ModelNotFoundException;

    public Long deleteCar(Long carId) throws DeleteCarException;
    
    public List<CarEntity> retrieveAvailableCarsBasedOnGivenDateTime(LocalDateTime pickupDateTime, LocalDateTime returnDateTime);
    
}
