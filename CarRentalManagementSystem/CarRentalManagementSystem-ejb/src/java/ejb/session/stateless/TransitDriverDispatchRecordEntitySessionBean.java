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
import util.exception.UpdateDispatchRecordFailureException;

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

    @Override
    public void createNewDispatchRecord(ReservationRecordEntity rr, TransitDriverDispatchRecordEntity newDispatchRecord) {

        em.persist(newDispatchRecord);
        newDispatchRecord.setReservationRecord(rr);
        rr.setTddr(newDispatchRecord);
        em.flush();
    }

    @Override
    public TransitDriverDispatchRecordEntity retrieveDispatchRecordById(Long id) {
        TransitDriverDispatchRecordEntity dispatchRecord = em.find(TransitDriverDispatchRecordEntity.class, id);
        return dispatchRecord;
    }
    
    @Override
    public void updateDispatchRecordStatusAsCompleted(Long dispatchRecordId) throws UpdateDispatchRecordFailureException{
        
        TransitDriverDispatchRecordEntity record = retrieveDispatchRecordById(dispatchRecordId);
        if(record == null){
            throw new UpdateDispatchRecordFailureException("Dispatch record Id " + dispatchRecordId + " does not exist!");
        }
        
        record.setDispatchRecordStatus(DispatchRecordEnum.COMPLETED);
        em.flush();
    }

}
