package ejb.session.stateless;

import entity.OutletEntity;


public interface OutletEntitySessionBeanRemote {
    public Long createOutlet(OutletEntity newOutlet);
    
    public OutletEntity retrieveOutletByOutletId(Long outletId);
    
    public void updateOutlet(OutletEntity outlet);
    
    public void deleteOutlet(Long outletId);
}