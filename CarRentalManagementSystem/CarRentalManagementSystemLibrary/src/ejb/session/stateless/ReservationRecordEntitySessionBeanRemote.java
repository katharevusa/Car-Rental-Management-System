package ejb.session.stateless;

import entity.ReservationRecordEntity;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.InvalidFieldEnteredException;
import util.exception.NoReservationAvailable;
import util.exception.ReservationAlreadyCancelledException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;

public interface ReservationRecordEntitySessionBeanRemote {
    
    public List<ReservationRecordEntity> retrieveAllReservationRecord();
    
//    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity) throws InvalidFieldEnteredException;
//    
//    public List<ReservationRecordEntity> retrieveReservationByStartDate() throws NoReservationAvailable;
//
//    public ReservationRecordEntity retrieveReservationBylId(Long reservationId)throws ReservationRecordNotFoundException;
////public boolean search(String inputCategory, String inputModel, String inputPickupOutlet, LocalDateTime pickUpDateTime, String inputReturnOutlet, LocalDateTime returnDateTime);
//    
//    public void cancelReservation(Long reservationRecordId) throws ReservationAlreadyCancelledException;
//
//   // public void cancelReservation(Long reservationRecordId);

    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity, Long customerId, Long carId, Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId) throws ReservationCreationException;
}
