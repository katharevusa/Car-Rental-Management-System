/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Local(OutletEntitySessionBeanLocal.class)
@Remote(OutletEntitySessionBeanRemote.class)
@Stateless
public class OutletEntitySessionBean implements OutletEntitySessionBeanRemote, OutletEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

     @Override
    public Long createOutlet(OutletEntity newOutlet) {
        em.persist(newOutlet);
        em.flush();       
        return newOutlet.getOutletId(); 
    }

    @Override
    public OutletEntity retrieveOutletByOutletId(Long outletId) {
        OutletEntity outlet = em.find(OutletEntity.class, outletId);
        return outlet;
    }

    @Override
    public void updateOutlet(OutletEntity outlet) {
        em.merge(outlet);
    }

    @Override
    public void deleteOutlet(Long outletId) {
        OutletEntity outlet = retrieveOutletByOutletId(outletId);
        em.remove(outlet);
    }
    @Override
    public List<OutletEntity> retrieveAllOutlet(){
        
        Query query = em.createQuery("SELECT o FROM OutletEntity o");
        return query.getResultList();
    }  
}

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
