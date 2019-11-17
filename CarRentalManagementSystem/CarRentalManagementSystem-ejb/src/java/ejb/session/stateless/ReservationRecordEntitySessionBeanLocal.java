package ejb.session.stateless;

import entity.ReservationRecordEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.CancelReservationFailureException;
import util.exception.InvalidFieldEnteredException;
import util.exception.NoReservationAvailable;
import util.exception.ReservationAlreadyCancelledException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;


public interface ReservationRecordEntitySessionBeanLocal {
    
    public List<ReservationRecordEntity> retrieveAllReservationRecord();

    public ReservationRecordEntity retrieveReservationBylId(Long reservationId) throws ReservationRecordNotFoundException;

    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity, Long customerId, Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId) throws ReservationCreationException;

    public ReservationRecordEntity cancelReservation(Long reservationId) throws CancelReservationFailureException;
    
    public List<ReservationRecordEntity> retrieveReservationRecordByDate(LocalDate currDate);
    
}
