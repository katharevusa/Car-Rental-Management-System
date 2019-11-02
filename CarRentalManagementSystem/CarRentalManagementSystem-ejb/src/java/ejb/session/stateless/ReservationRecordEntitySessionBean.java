package ejb.session.stateless;

import entity.ReservationRecordEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidFieldEnteredException;

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
    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity) throws InvalidFieldEnteredException{
        
        try
        {
            em.persist(reservationRecordEntity);
            em.flush();
            
            return reservationRecordEntity.getReservationRecordId();
        }
        catch(PersistenceException ex){
            throw new InvalidFieldEnteredException();
        }
    }

    @Override
    public List<ReservationRecordEntity> retrieveAllReservation()
    {
        Query query = em.createQuery("SELECT rr FROM ReservationRecordEntity rr");
        
        return query.getResultList();
    }
 
    
}
