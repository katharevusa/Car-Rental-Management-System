package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.OutletNotFoundException;

public interface OutletEntitySessionBeanRemote {

    public Long createOutlet(OutletEntity newOutlet);

    public OutletEntity retrieveOutletByOutletId(Long outletId) throws OutletNotFoundException;

    public void updateOutlet(OutletEntity outlet);
    
    public void deleteOutlet(Long outletId) throws OutletNotFoundException;
    
    public List<OutletEntity> retrieveAllOutlet();
    
    public List<OutletEntity> retrieveOutletByPickupDateTime(LocalDateTime pickupDateTime);
}
