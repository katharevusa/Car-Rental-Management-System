package ejb.session.stateless;

import entity.OutletEntity;
import java.util.List;


public interface OutletEntitySessionBeanRemote {
    public Long createOutlet(OutletEntity newOutlet);
    
    public OutletEntity retrieveOutletByOutletId(Long outletId);
    
    public void updateOutlet(OutletEntity outlet);
    
    public void deleteOutlet(Long outletId);
    public List<OutletEntity> retrieveAllOutlet();
}