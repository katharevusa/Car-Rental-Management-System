package ejb.session.stateless;

import entity.CarEntity;
import java.util.List;

public interface CarEntitySessionBeanRemote {
     public List<CarEntity> retrieveAllCars();
}
