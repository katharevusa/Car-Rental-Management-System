package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
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
import util.exception.CarIsDisabledException;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
import util.exception.OutletNotFoundException;
import util.exception.UpdateCarException;
import util.exception.UpdateCarFailureException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(CarEntitySessionBeanLocal.class)
@Remote(CarEntitySessionBeanRemote.class)

public class CarEntitySessionBean implements CarEntitySessionBeanRemote, CarEntitySessionBeanLocal {

    @EJB
    private ReservationRecordEntitySessionBeanLocal reservationRecordEntitySessionBeanLocal;

    @Resource
    private EJBContext eJBContext;
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    public CarEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCar(CarEntity newCarEntity, String make, String model, Long outletId) throws NewCarCreationException{

        try {
       
            Set<ConstraintViolation<CarEntity>> constraintViolations = validator.validate(newCarEntity);

            if (constraintViolations.isEmpty()) {
                
                ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelByMakeAndModel(make, model);
                OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletByOutletId(outletId);
                em.persist(newCarEntity);
                newCarEntity.setModelEntity(modelEntity);
                newCarEntity.setOutletEntity(outletEntity);
                outletEntity.getCars().add(newCarEntity);
                modelEntity.getCars().add(newCarEntity);
                em.flush();
                return newCarEntity.getCarId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } catch (PersistenceException ex1) {
            throw new NewCarCreationException("License plate number " + newCarEntity.getPlateNumber() + " already exists!");
        } catch (OutletNotFoundException ex2) {
            throw new NewCarCreationException("Outlet not found" + outletId);
        } catch (ModelNotFoundException ex3) {
            throw new NewCarCreationException("Model associated to the car does not exist!");
        } catch (InputDataValidationException ex4){
            throw new NewCarCreationException(ex4.getMessage());
        }

    }

    @Override
    public CarEntity retrieveCarByCarId(Long carId) throws CarNotFoundException {

        CarEntity carEntity = em.find(CarEntity.class, carId);

        if (carEntity != null) {
            return carEntity;
        } else {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
    }

    @Override
    public List<CarEntity> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM CarEntity c ORDER BY c.modelEntity.categoryEntity.name, c.modelEntity.modelName,c.plateNumber ASC");

        return query.getResultList();

    }

    @Override
    public Long deleteCar(Long carId) throws DeleteCarException {

        try {
            CarEntity carEntityToRemove = retrieveCarByCarId(carId);
            if (carEntityToRemove.getStatus() != CarStatusEnum.ONRENTAL && carEntityToRemove.getStatus()!= CarStatusEnum.RESERVED) {

                carEntityToRemove.getModelEntity().getCars().remove(carEntityToRemove);
                if (carEntityToRemove.getOutletEntity() != null) {
                    carEntityToRemove.getOutletEntity().getCars().remove(carEntityToRemove);
                }
                em.remove(carEntityToRemove);
                return carEntityToRemove.getCarId();

            } else {

                carEntityToRemove.setDisabled(true);
                throw new DeleteCarException();
            }

        } catch (CarNotFoundException ex1) {
            throw new DeleteCarException("Car ID " + carId + "does not exist!");
        } catch (DeleteCarException ex2) {
            throw new DeleteCarException("The car is currently in usage and cannot be deleted, but it has been disabled for future rental.");
        }

    }

    @Override
    public Boolean checkCarAvailability(LocalDateTime pickupDateTime, LocalDateTime returnDateTime,
            Long selectedPickupOutletId, Long selectedReturnOutletId, Long selectedCategoryId, Long selectedModelId) {

        Query query;
        int totalAvailableCars;
        if (selectedModelId < 0) {
            query = em.createQuery("SELECT c FROM CarEntity c WHERE c.disabled = FALSE "
                    + "AND c.modelEntity.categoryEntity.categoryId = :inCategoryId");
            query.setParameter("inCategoryId", selectedCategoryId);
            totalAvailableCars = query.getResultList().size();

            query = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.isCancelled = FALSE AND r.category.categoryId = :inCategoryId");
            query.setParameter("inCategoryId", selectedCategoryId);
            List<ReservationRecordEntity> reservations = query.getResultList();
            List<ReservationRecordEntity> overlappedReservations = new ArrayList<>();

            //check for reservation made on the selected category that overlap with the new reservation
            for (ReservationRecordEntity reservation : reservations) {

                if (reservation.getPickUpDateTime().isBefore(pickupDateTime)) {
                    if (reservation.getReturnDateTime().isAfter(pickupDateTime)) {
                        overlappedReservations.add(reservation);
                    } else if (reservation.getReturnDateTime().isEqual(pickupDateTime)) {
                        if (!(reservation.getReturnOutlet().getOutletId().equals(selectedPickupOutletId))) {
                            overlappedReservations.add(reservation);
                        }
                    } else if (reservation.getReturnDateTime().isBefore(pickupDateTime)) {
                        if (reservation.getReturnDateTime().isAfter(pickupDateTime.plusHours(2))) {
                            if (!(reservation.getReturnOutlet().getOutletId().equals(selectedPickupOutletId))) {
                                overlappedReservations.add(reservation);
                            }
                        }
                    }
                } else if (reservation.getPickUpDateTime().isEqual(pickupDateTime)) {
                    overlappedReservations.add(reservation);
                } else if (reservation.getPickUpDateTime().isAfter(pickupDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime)) {
                    overlappedReservations.add(reservation);
                } else if (reservation.getPickUpDateTime().isEqual(returnDateTime)) {
                    if (!(reservation.getPickUpOutlet().getOutletId().equals(selectedReturnOutletId))) {
                        overlappedReservations.add(reservation);
                    }
                } else if (reservation.getPickUpDateTime().isAfter(returnDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime.plusHours(2))) {
                    if (!(reservation.getPickUpOutlet().getOutletId().equals(selectedReturnOutletId))) {
                        overlappedReservations.add(reservation);
                    }
                }

            }

            totalAvailableCars = totalAvailableCars - overlappedReservations.size();

        } else {
            query = em.createQuery("SELECT c FROM CarEntity c WHERE c.disabled = FALSE "
                    + "AND c.modelEntity.modelId = :inModelId");
            query.setParameter("inModelId", selectedModelId);
            totalAvailableCars = query.getResultList().size();

            query = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.model.modelId = :inModelId AND r.isCancelled = FALSE");
            query.setParameter("inModelId", selectedModelId);
            List<ReservationRecordEntity> reservations = query.getResultList();
            List<ReservationRecordEntity> overlappedReservations = new ArrayList<>();

            for (ReservationRecordEntity reservation : reservations) {

                if (reservation.getPickUpDateTime().isBefore(pickupDateTime)) {
                    if (reservation.getReturnDateTime().isAfter(pickupDateTime)) {
                        overlappedReservations.add(reservation);
                    } else if (reservation.getReturnDateTime().isEqual(pickupDateTime)) {
                        if (!(reservation.getReturnOutlet().getOutletId().equals(selectedPickupOutletId))) {
                            overlappedReservations.add(reservation);
                        }
                    } else if (reservation.getReturnDateTime().isBefore(pickupDateTime)) {
                        if (reservation.getReturnDateTime().isAfter(pickupDateTime.plusHours(2))) {
                            if (!(reservation.getReturnOutlet().getOutletId().equals(selectedPickupOutletId))) {
                                overlappedReservations.add(reservation);
                            }
                        }
                    }
                } else if (reservation.getPickUpDateTime().isEqual(pickupDateTime)) {
                    overlappedReservations.add(reservation);
                } else if (reservation.getPickUpDateTime().isAfter(pickupDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime)) {
                    overlappedReservations.add(reservation);
                } else if (reservation.getPickUpDateTime().isEqual(returnDateTime)) {
                    if (!(reservation.getPickUpOutlet().getOutletId().equals(selectedReturnOutletId))) {
                        overlappedReservations.add(reservation);
                    }
                } else if (reservation.getPickUpDateTime().isAfter(returnDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime.plusHours(2))) {
                    if (!(reservation.getPickUpOutlet().getOutletId().equals(selectedReturnOutletId))) {
                        overlappedReservations.add(reservation);
                    }
                }

            }

            totalAvailableCars = totalAvailableCars - overlappedReservations.size();
        }

        if (totalAvailableCars > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<CarEntity> filterCarsBasedOnCategoryId(List<CarEntity> cars, Long categoryId) {

        List<CarEntity> allCars = new ArrayList<>(cars);
        List<CarEntity> filtered = new ArrayList<>();

        for (CarEntity car : allCars) {
            em.merge(car);
            System.out.println(car.getModelEntity().getCategoryEntity().getCategoryId());
            if (car.getModelEntity().getCategoryEntity().getCategoryId().equals(categoryId)) {
                filtered.add(car);
            }
        }
        return filtered;
    }

    @Override
    public List<CarEntity> filterCarsBasedOnModelId(List<CarEntity> cars, Long modelId) {

        List<CarEntity> allCars = new ArrayList<>(cars);
        List<CarEntity> filtered = new ArrayList<>();

        if (modelId == -1) {
            return allCars;
        } else {

            for (CarEntity car : cars) {
                em.merge(car);
                if (car.getModelEntity().getModelId().equals(modelId)) {
                    filtered.add(car);
                }
            }
            return filtered;

        }

    }

    /*  @Override
    public void updateCar(CarEntity carEntity,Long modelId,Long outletId) throws UpdateCarFailureException{
        
        try{
            if (carEntity != null && carEntity.getCarId() != null){
                CarEntity carToUpdate = retrieveCarByCarId(carEntity.getCarId());
                carToUpdate.setPlateNumber(carEntity.getPlateNumber());
                carToUpdate.setStatus(carEntity.getStatus());
                
                if(!(carToUpdate.getModelEntity().getModelId().equals(modelId))){
                    ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
                    carToUpdate.getModelEntity().getCars().remove(carToUpdate);
                    modelEntity.getCars().add(carToUpdate);
                    carToUpdate.setModelEntity(modelEntity);
                }
                
                if(!(carToUpdate.getOutletEntity().getOutletId().equals(outletId))){
                    OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletByOutletId(outletId);
                    carToUpdate.getOutletEntity().getCars().remove(carToUpdate);
                    outletEntity.getCars().add(carToUpdate);
                    carToUpdate.setOutletEntity(outletEntity);
                }
            }
        } catch (CarNotFoundException ex1){
            eJBContext.setRollbackOnly();
            throw new UpdateCarFailureException(ex1.getMessage());
        } catch (ModelNotFoundException ex2){
            eJBContext.setRollbackOnly();
            throw new UpdateCarFailureException(ex2.getMessage());
        } catch (OutletNotFoundException ex3){
            eJBContext.setRollbackOnly();
            throw new UpdateCarFailureException(ex3.getMessage());
        }
        
    }*/
    @Override
    public void updateCar(CarEntity car) throws UpdateCarFailureException, InputDataValidationException {
        try {
            if (car != null && car.getCarId() != null && !car.isDisabled()) {
                Set<ConstraintViolation<CarEntity>> constraintViolations = validator.validate(car);

                if (constraintViolations.isEmpty()) {
                    CarEntity carToUpdate = retrieveCarByCarId(car.getCarId());

                    if (carToUpdate.getCarId().equals(car.getCarId())) {
                        carToUpdate.setPlateNumber(car.getPlateNumber());
                        carToUpdate.setMake(car.getMake());
                        carToUpdate.setModel(car.getModel());
                        carToUpdate.setModelEntity(car.getModelEntity());
                        carToUpdate.setStatus(car.getStatus());
                        carToUpdate.setOutletEntity(car.getOutletEntity());
                    } else {
                        throw new UpdateCarFailureException("ID of car to be updated does not match the existing car");
                    }
                } else {
                    throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            } else {
                throw new CarIsDisabledException("Car cannot be updated as it is being disabled!");
            }
        } catch (CarNotFoundException ex1) {
            eJBContext.setRollbackOnly();
            throw new UpdateCarFailureException(ex1.getMessage());
        } catch (CarIsDisabledException ex2) {
            eJBContext.setRollbackOnly();
            throw new UpdateCarFailureException(ex2.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
