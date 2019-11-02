package ejb.session.stateless;

import entity.CarEntity;
import java.util.List;


public interface CarEntitySessionBeanLocal {

    public List<CarEntity> retrieveAllCars();
    
}
