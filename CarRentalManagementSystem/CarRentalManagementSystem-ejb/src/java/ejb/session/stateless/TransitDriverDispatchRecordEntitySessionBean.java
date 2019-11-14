/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import entity.ReservationRecordEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.OutletNotFoundException;

/**
 *
 * @author admin
 */
@Stateless
@Remote(TransitDriverDispatchRecordEntitySessionBeanRemote.class)
@Local(TransitDriverDispatchRecordEntitySessionBeanLocal.class)
public class TransitDriverDispatchRecordEntitySessionBean implements TransitDriverDispatchRecordEntitySessionBeanRemote, TransitDriverDispatchRecordEntitySessionBeanLocal {

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createNewDispatchRecord(OutletEntity pickUpOutlet, ReservationRecordEntity rr, TransitDriverDispatchRecordEntity newDispatchRecord) {
        em.persist(newDispatchRecord);
        rr.setTddr(newDispatchRecord);
        newDispatchRecord.setReservationRecord(rr);
        pickUpOutlet.getDispatchRecord().add(newDispatchRecord);
        newDispatchRecord.setOutlet(pickUpOutlet);
        em.flush();
    }

    @Override
    public TransitDriverDispatchRecordEntity retrieveDispatchRecordById(Long id) {
        TransitDriverDispatchRecordEntity dispatchRecord = em.find(TransitDriverDispatchRecordEntity.class, id);
        return dispatchRecord;
    }

//    @Override
//    public List<TransitDriverDispatchRecordEntity> retrieveDispatchRecordsByOutletId(Long outletId) {
//
//        try {
//            OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletByOutletId(outletId);
//            List<TransitDriverDispatchRecordEntity> list = outletEntity.getDispatchRecord();
//            return list;
//        } catch (OutletNotFoundException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }

    @Override
    public List<TransitDriverDispatchRecordEntity> retrieveDispatchRecordsByOutletId(Long outletId) {
        OutletEntity outlet = em.find(OutletEntity.class, outletId);
        return outlet.getDispatchRecord();
    }
}
