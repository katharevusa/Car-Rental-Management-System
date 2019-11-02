package ejb.session.stateless;

import entity.RentalRateEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteRentalRateException;
import util.exception.InvalidFieldEnteredException;
import util.exception.RentalRateNotFoundException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(RentalRateEntitySessionBeanLocal.class)
@Remote(RentalRateEntitySessionBeanRemote.class)


public class RentalRateEntitySessionBean implements RentalRateEntitySessionBeanRemote, RentalRateEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public RentalRateEntitySessionBean() {
    }

    
    public Long createNewRentalRate(RentalRateEntity rentalRateEntity) throws InvalidFieldEnteredException{
        
        try {
            em.persist(rentalRateEntity);
            em.flush();
            
            return rentalRateEntity.getRentalRateId();
        } catch (PersistenceException ex){
            throw new InvalidFieldEnteredException();
        }
    }
    
    public List<RentalRateEntity> retrieveAllRentalRates(){
        Query query = em.createQuery("SELECT rr FROM RentalRateEntity");
        
        return query.getResultList();
    }
    
    public RentalRateEntity retrieveRentalRateByRentalId(Long rentalRateId) throws RentalRateNotFoundException{
        
        RentalRateEntity rentalRateEntity = em.find(RentalRateEntity.class,rentalRateId);
        
        if (rentalRateEntity != null){
            return rentalRateEntity;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID" + rentalRateId + "does not exist");
        }
    }
    
    
    public void updateRentalRate(RentalRateEntity rentalRateEntity) throws RentalRateNotFoundException,UpdateRentalRateException{
        
        if (rentalRateEntity.getRentalRateId() != null){
            
            RentalRateEntity rentalRateToUpdate = retrieveRentalRateByRentalId(rentalRateEntity.getRentalRateId());
            
            if (rentalRateToUpdate.getRentalRateName().equals(rentalRateEntity.getRentalRateName())){
                //update
            } else {
                throw new UpdateRentalRateException();
            }
                
        } else {
            throw new RentalRateNotFoundException();
        }
    }
    
//    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException,DeleteRentalRateException{
//        
//        RentalRateEntity rentalEntityToRemove = retrieveRentalRateByRentalId(rentalRateId);
//   
//        if (rentalEntityToRemove != null) {
//            if () {
//
//            } else {
//
//                rentalEntityToRemove.setDisabled(true);
//                throw new DeleteRentalRateException();
//            }
//        } else {
//            throw new RentalRateNotFoundException("Rental rate" + rentalRateId + "does not exist");
//        }
//        
//    }
}
