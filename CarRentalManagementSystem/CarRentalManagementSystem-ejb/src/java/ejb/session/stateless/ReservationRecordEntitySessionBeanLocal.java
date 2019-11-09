package ejb.session.stateless;

import entity.ReservationRecordEntity;
import java.util.List;
import util.exception.InvalidFieldEnteredException;
import util.exception.NoReservationAvailable;


public interface ReservationRecordEntitySessionBeanLocal {

    public List<ReservationRecordEntity> retrieveAllReservation();

    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity) throws InvalidFieldEnteredException;
    public List<ReservationRecordEntity> retrieveReservationByStartDate() throws NoReservationAvailable;
    
    
}
