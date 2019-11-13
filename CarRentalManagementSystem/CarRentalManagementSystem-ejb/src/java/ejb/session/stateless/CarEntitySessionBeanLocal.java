package ejb.session.stateless;

import entity.CarEntity;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.InvalidFieldEnteredException;
import util.exception.NewCarCreationException;

public interface CarEntitySessionBeanLocal {

    public Long createNewCar(CarEntity newCarEntity, Long modelId) throws NewCarCreationException;

    public List<CarEntity> retrieveAllCars();

    public Long deleteCar(Long carId) throws DeleteCarException;

    public CarEntity retrieveCarByCarId(Long carId) throws CarNotFoundException;

}
