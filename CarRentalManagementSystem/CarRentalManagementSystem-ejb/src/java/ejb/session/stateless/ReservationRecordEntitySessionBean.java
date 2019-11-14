package ejb.session.stateless;


import entity.CarEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CancelReservationFailureException;
import util.exception.CarNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.NoReservationAvailable;
import util.exception.OutletNotFoundException;
import util.exception.ReservationAlreadyCancelledException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(ReservationRecordEntitySessionBeanLocal.class)
@Remote(ReservationRecordEntitySessionBeanRemote.class)

public class ReservationRecordEntitySessionBean implements ReservationRecordEntitySessionBeanRemote, ReservationRecordEntitySessionBeanLocal {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;
    @EJB
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
    
    
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public ReservationRecordEntitySessionBean() {
    }

    @Override
    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity, Long customerId, 
            Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId) throws ReservationCreationException{

        try{
            em.persist(reservationRecordEntity);

            System.out.println("here1");
            ModelEntity model;
            if(modelId.equals(-1)){
                model = null;
            } else {
                model = modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
            }
            
            CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
            CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
            OutletEntity pickupOutlet = outletEntitySessionBeanLocal.retrieveOutletByOutletId(pickupOutletId);
            OutletEntity returnOutlet = outletEntitySessionBeanLocal.retrieveOutletByOutletId(returnOutletId);
            
            
            reservationRecordEntity.setCategory(category);
            reservationRecordEntity.setModel(model);
            reservationRecordEntity.setPickUpOutlet(pickupOutlet);
            reservationRecordEntity.setReturnOutlet(returnOutlet);
            reservationRecordEntity.setCustomerEntity(customer);
            customer.getReservations().add(reservationRecordEntity);
            
            em.flush();
            return reservationRecordEntity.getReservationRecordId();
            
        } catch (PersistenceException ex1) {
            throw new ReservationCreationException("");
        } catch (CustomerNotFoundException ex2){
            throw new ReservationCreationException("");
        } catch (ModelNotFoundException ex3){
            throw new ReservationCreationException("");
        } catch (CategoryNotFoundException ex4){
            throw new ReservationCreationException("");
        } catch (OutletNotFoundException ex5){
            throw new ReservationCreationException("");
        }

    }

    
    @Override
    public List<ReservationRecordEntity> retrieveAllReservationRecord() {
        Query query = em.createQuery("SELECT rr FROM ReservationRecordEntity rr");
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

    @Override
    public ReservationRecordEntity cancelReservation(Long reservationId) throws CancelReservationFailureException{
        
        try{
            
            ReservationRecordEntity reservationRecord = retrieveReservationBylId(reservationId);
            if (!reservationRecord.getIsCancelled()) {
                
                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime reservationTime = reservationRecord.getPickUpDateTime();
                LocalDateTime threeDays = reservationTime.minusDays(3);
                LocalDateTime sevenDays = reservationTime.minusDays(7);
                LocalDateTime fourteenDays = reservationTime.minusDays(14);
                
                if(currentDateTime.isBefore(threeDays)){
                    //70%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.3);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.7));
                    }
                } else if(currentDateTime.isBefore(sevenDays)){
                    //50%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.5);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.5));
                    }
                } else if(currentDateTime.isBefore(fourteenDays)){
                    //20%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.8);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.2));
                    }
                } else {
                    //full refund
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount());
                    } else {
                        reservationRecord.setRefund(0.00);
                    }
                }

                reservationRecord.setIsCancelled(true);
                return reservationRecord;
                
            }else {
                throw new ReservationAlreadyCancelledException("Reservation " + reservationId + " has already been cancelled!");
            }
            
        } catch(ReservationRecordNotFoundException ex1){
            throw new CancelReservationFailureException("Reservation ID " + reservationId + " not found!");
        } catch (ReservationAlreadyCancelledException ex2){
            throw new CancelReservationFailureException(ex2.getMessage());
        }
        
    }
    
}


