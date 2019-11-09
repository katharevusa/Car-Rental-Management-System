package ejb.session.stateless;

import entity.ReservationRecordEntity;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidFieldEnteredException;
import util.exception.NoReservationAvailable;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(ReservationRecordEntitySessionBeanLocal.class)
@Remote(ReservationRecordEntitySessionBeanRemote.class)

public class ReservationRecordEntitySessionBean implements ReservationRecordEntitySessionBeanRemote, ReservationRecordEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public ReservationRecordEntitySessionBean() {
    }

    @Override
    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity) throws InvalidFieldEnteredException {

        try {
            em.persist(reservationRecordEntity);
            em.flush();

            return reservationRecordEntity.getReservationRecordId();
        } catch (PersistenceException ex) {
            throw new InvalidFieldEnteredException();
        }
    }

    @Override
    public List<ReservationRecordEntity> retrieveAllReservationRecord() {
        Query query = em.createQuery("SELECT rr FROM ReservationRecordEntity rr");

        return query.getResultList();
    }

    @Override
    public List<ReservationRecordEntity> retrieveReservationByStartDate() throws NoReservationAvailable {

        Query query = em.createQuery("SELECT rr FROM ReservationRecordEntity WHERE ");
        return query.getResultList();
    }

    @Override
    public ReservationRecordEntity retrieveReservationBylId(Long reservationId) throws ReservationRecordNotFoundException{
        ReservationRecordEntity reservationRecordEntity = em.find(ReservationRecordEntity.class, reservationId);
        
        if(reservationRecordEntity != null)
        {
            return reservationRecordEntity;
        }
        else
        {
            throw new ReservationRecordNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }           
    }
}

//    @Override
//    public void cancelReservation(Long reservationRecordId) {
//        ReservationRecordEntity reservationRecord = retrieveReservationBylId(reservationRecordId);
//        
//        if(!reservationRecord.getIsCancelled())
//        {
//            //check when is this method being called
//            //if at least 14 days before pickup - 0%
//            //if less than 14 days but at least 7 days before pickup -20%
//            //if less than 7 days but at least 3 days before pickup -50%
//            //less than 3 days before pickup -70%
//            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItemEntities())
//            {
//                try
//                {   
//                    LocalDateTime currentDateTime = LocalDateTime.now();
//                    LocalDateTime reservationTime = reservationRecord.getPickUpDateTime();
//                    LocalDateTime threeDays = reservationTime.minusDays(7);
//                    
//                    if(currentDateTime)
//                    
//                    productEntitySessionBeanLocal.creditQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
//                }
//                catch(ProductNotFoundException ex)
//                {
//                    ex.printStackTrace(); // Ignore exception since this should not happen
//                }                
//            }
//            
//            saleTransactionEntity.setVoidRefund(true);
//        }
//        else
//        {
//            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
//        }
//    }
//
//}
