package ejb.session.stateless;

import entity.OutletEntity;
import java.util.List;
import util.exception.OutletNotFoundException;


public interface OutletEntitySessionBeanLocal {
    public Long createOutlet(OutletEntity newOutlet);
    
    public OutletEntity retrieveOutletByOutletId(Long outletId) throws OutletNotFoundException;
    
    public void updateOutlet(OutletEntity outlet);
    
    public List<OutletEntity> retrieveAllOutlet();

    public void deleteOutlet(Long outletId) throws OutletNotFoundException;
}