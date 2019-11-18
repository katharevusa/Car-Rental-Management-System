package ejb.session.stateless;

import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.exception.CancelReservationFailureException;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.PastReservationException;
import util.exception.ReservationAlreadyCancelledException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.UpdateReservationStatusFailureException;

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
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationRecordEntitySessionBean() {
        
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewReservationRecord(ReservationRecordEntity reservationRecordEntity, Long customerId,
            Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId) throws ReservationCreationException {

        try {

            Set<ConstraintViolation<ReservationRecordEntity>> constraintViolations = validator.validate(reservationRecordEntity);

            if (constraintViolations.isEmpty()) {

                ModelEntity model;

                try {
                    model = modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
                } catch (ModelNotFoundException ex) {
                    model = null;
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

                em.persist(reservationRecordEntity);
                em.flush();
                return reservationRecordEntity.getReservationRecordId();

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } catch (PersistenceException ex1) {
            throw new ReservationCreationException("");
        } catch (CustomerNotFoundException ex2) {
            throw new ReservationCreationException("");
        } catch (CategoryNotFoundException ex4) {
            throw new ReservationCreationException("");
        } catch (OutletNotFoundException ex5) {
            throw new ReservationCreationException("");
        } catch (InputDataValidationException ex6){
            throw new ReservationCreationException(ex6.getMessage());
        }

    }

    
    @Override
    public Long createReservationRecordForWebClient(double totalRentalRate, Long selectedModelId, Long selectedCategoryId,
            LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Long selectedPickupOutletId,
            Long selectedReturnOutletId, String ccNumber, double paidAmt,Long customerId) throws ReservationCreationException{
        
        ReservationRecordEntity reservation = new ReservationRecordEntity(totalRentalRate, pickupDateTime, returnDateTime, ccNumber, paidAmt);
        try{
             
            return createNewReservationRecord(reservation, customerId, selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId);
        
        } catch (ReservationCreationException ex){
            throw new ReservationCreationException("Failed to create new reservation record.");
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
    public List<ReservationRecordEntity> retrieveReservationRecordByDate(LocalDate currDate){
        
        List<ReservationRecordEntity> reservations = retrieveAllReservationRecord();
        List<ReservationRecordEntity> currentDayReservations = new ArrayList<>();
        for(ReservationRecordEntity reservation:reservations){
            if(reservation.getPickUpDateTime().toLocalDate().isEqual(currDate)){
                currentDayReservations.add(reservation);
            }
        }
        return currentDayReservations;
        
    }
    

    @Override
    public ReservationRecordEntity cancelReservation(Long reservationId) throws CancelReservationFailureException {

        try {

            ReservationRecordEntity reservationRecord = retrieveReservationBylId(reservationId);
            if (!reservationRecord.getIsCancelled() && !reservationRecord.getHasPast()) {

                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime reservationTime = reservationRecord.getPickUpDateTime();
                LocalDateTime threeDays = reservationTime.minusDays(3);
                LocalDateTime sevenDays = reservationTime.minusDays(7);
                LocalDateTime fourteenDays = reservationTime.minusDays(14);

                if (currentDateTime.isBefore(threeDays)) {
                    //70%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.3);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.7));
                    }
                } else if (currentDateTime.isBefore(sevenDays)) {
                    //50%
                    if (reservationRecord.getPaidAmount() != 0) {
                        reservationRecord.setPaidAmount(0.00);
                        reservationRecord.setRefund(reservationRecord.getPaidAmount() * 0.5);
                    } else {
                        reservationRecord.setRefund(0.00 - (reservationRecord.getPaidAmount() * 0.5));
                    }
                } else if (currentDateTime.isBefore(fourteenDays)) {
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

            } else if (reservationRecord.getIsCancelled()) {
                throw new ReservationAlreadyCancelledException("Reservation " + reservationId + " has already been cancelled!");
            } else {
                throw new PastReservationException("Past reservation cannot be cancelled!");
            }
        } catch (ReservationRecordNotFoundException ex1) {
            throw new CancelReservationFailureException("Reservation ID " + reservationId + " not found!");
        } catch (ReservationAlreadyCancelledException ex2) {
            throw new CancelReservationFailureException(ex2.getMessage());
        } catch (PastReservationException ex3) {
            throw new CancelReservationFailureException(ex3.getMessage());
        }

    }

    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReservationRecordEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    @Override
    public ReservationRecordEntity createReservationInWebService(Long partnerId, Long selectedModelId, Long selectedCategoryId, Long selectedPickupOutletId, Long selectedReturnedOutletId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Double totalRentalRate, String ccNumber, Double paidAmt) throws ReservationRecordNotFoundException {
        try {
            ReservationRecordEntity r = new ReservationRecordEntity(totalRentalRate, pickupDateTime, returnDateTime, ccNumber, paidAmt);
            createNewReservationRecord(r, partnerId, selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnedOutletId);
            return r;
        } catch (ReservationCreationException ex) {
            throw new ReservationRecordNotFoundException("Reservation not found!");
        }
    }

    @Override
    public void updateReservationStatus(Long reservationId) throws UpdateReservationStatusFailureException{
        
        try{
            
            ReservationRecordEntity reservation = retrieveReservationBylId(reservationId);
            reservation.getCarEntity().setStatus(CarStatusEnum.ONRENTAL);
            reservation.getCarEntity().setOutletEntity(null);
            reservation.setHasPast(true);
            em.flush();
            
        } catch (ReservationRecordNotFoundException ex){
            throw new UpdateReservationStatusFailureException("Reservation Id " + reservationId + " does not exist.");
        }
        
    }
    
    
}
