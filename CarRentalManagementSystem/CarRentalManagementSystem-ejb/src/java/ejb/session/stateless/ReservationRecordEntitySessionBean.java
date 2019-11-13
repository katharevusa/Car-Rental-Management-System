package ejb.session.stateless;


import entity.ReservationRecordEntity;
import java.math.BigDecimal;
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
import util.exception.ReservationAlreadyCancelledException;
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
    public ReservationRecordEntity retrieveReservationBylId(Long reservationId) throws ReservationRecordNotFoundException {
        ReservationRecordEntity reservationRecordEntity = em.find(ReservationRecordEntity.class, reservationId);

        if (reservationRecordEntity != null) {
            return reservationRecordEntity;
        } else {
            throw new ReservationRecordNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }
    }

//    @Override
//    public boolean search(String inputCategory, String inputModel, String inputPickupOutlet, LocalDateTime pickUpDateTime, String inputReturnOutlet, LocalDateTime returnDateTime) {
//       
//    }
   @Override
    public void cancelReservation(Long reservationRecordId) throws ReservationAlreadyCancelledException {
        try {
            ReservationRecordEntity reservationRecord = retrieveReservationBylId(reservationRecordId);
            if (!reservationRecord.getIsCancelled()) {
                //check when is this method being called
                //if at least 14 days before pickup - 0%
                //if less than 14 days but at least 7 days before pickup -20%
                //if less than 7 days but at least 3 days before pickup -50%
                //less than 3 days before pickup -70%

                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime reservationTime = reservationRecord.getPickUpDateTime();
                LocalDateTime threeDays = reservationTime.minusDays(3);
                LocalDateTime sevenDays = reservationTime.minusDays(7);
                LocalDateTime fourteenDays = reservationTime.minusDays(14);
                if (currentDateTime.isBefore(fourteenDays) || currentDateTime.isEqual(fourteenDays)) {
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setRefund(reservationRecord.getPaidAmount());
                        reservationRecord.setPaidAmount(0.00);
                    } else {
                        reservationRecord.setRefund(0.00);
                    }
                } else if (currentDateTime.isBefore(sevenDays) && currentDateTime.isAfter(fourteenDays)) {
                    //20%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.8);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.2));
                    }
                } else if (currentDateTime.isBefore(threeDays) && currentDateTime.isAfter(sevenDays)) {
                    //50%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.5);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.5));
                    }
                } else if (currentDateTime.isAfter(threeDays)) {
                    //70%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.3);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.7));
                    }
                }
                //unset relationship
                reservationRecord.setCarEntity(null);
                reservationRecord.setModel(null);
                reservationRecord.setCategory(null);
//                List<RentalDayEntity> rds = reservationRecord.getRentalDays();
//                for (RentalDayEntity rd : rds) {
//                    reservationRecord.setRentalDays(null);
//                }
                reservationRecord.setPickUpOutlet(null);
                reservationRecord.setReturnOutlet(null);
                reservationRecord.setCustomer(null);
                reservationRecord.setPartner(null);
                //reservationRecord.setDispatchRecord(null);
                reservationRecord.setIsCancelled(true);
            } else {
                throw new ReservationAlreadyCancelledException("reservation has already been cancelled!");
            }
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("reservation not found!");
        }
    }
}


