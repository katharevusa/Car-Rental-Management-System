/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import entity.ReservationRecordEntity;
import entity.TransitDriverDispatchRecordEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.DispatchRecordEnum;

/**
 *
 * @author admin
 */
@Stateless
@Remote(TransitDriverDispatchRecordEntitySessionBeanRemote.class)
@Local(TransitDriverDispatchRecordEntitySessionBeanLocal.class)
public class TransitDriverDispatchRecordEntitySessionBean implements TransitDriverDispatchRecordEntitySessionBeanRemote, TransitDriverDispatchRecordEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public void createNewDispatchRecord(OutletEntity pickUpOutlet, ReservationRecordEntity rr, TransitDriverDispatchRecordEntity newDispatchRecord) {
        em.persist(newDispatchRecord);
        rr.setTddr(newDispatchRecord);
        newDispatchRecord.setReservationRecords(rr);
        pickUpOutlet.getDispatchRecord().add(newDispatchRecord);
        newDispatchRecord.setOutlet(pickUpOutlet);
        em.flush();
    }
    @Override
    public TransitDriverDispatchRecordEntity retrieveDispatchRecordById(Long id){
        TransitDriverDispatchRecordEntity dispatchRecord = em.find(TransitDriverDispatchRecordEntity.class, id);
        return dispatchRecord;
    }

}
