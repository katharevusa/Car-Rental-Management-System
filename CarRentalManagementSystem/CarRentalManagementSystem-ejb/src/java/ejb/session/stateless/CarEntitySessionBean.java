package ejb.session.stateless;

import entity.CarEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
import util.exception.OutletNotFoundException;
import util.exception.UpdateCarException;

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

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    public CarEntitySessionBean() {
    }

    @Override
    public Long createNewCar(CarEntity newCarEntity, String make, String model, Long outletId) throws NewCarCreationException, OutletNotFoundException, ModelNotFoundException {

        try {
            ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelByMakeAndModel(make, model);
            OutletEntity outletEntity = outletEntitySessionBeanLocal.retrieveOutletByOutletId(outletId);
            em.persist(newCarEntity);
            newCarEntity.setModelEntity(modelEntity);
            newCarEntity.setOutletEntity(outletEntity);
            outletEntity.getCars().add(newCarEntity);
            modelEntity.getCars().add(newCarEntity);
            em.flush();
            return newCarEntity.getCarId();

        } catch (PersistenceException ex) {
            throw new NewCarCreationException("License plate number " + newCarEntity.getPlateNumber() + " already exists!");
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet not found" + outletId);
        } catch (ModelNotFoundException ex) {
            throw new ModelNotFoundException("Model associated to the car does not exist!");
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
            if (carEntityToRemove.isOnRental() == false) {

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
            throw new DeleteCarException("The car is currently in usage and cannot be deleted, but it has been disabled for fruther rental.");
        }

    }

    @Override
    public List<CarEntity> retrieveAvailableCars(LocalDateTime pickupDateTime,LocalDateTime returnDateTime,Long selectedPickupOutletId, Long selectedReturnOutletId){
        

        List<ReservationRecordEntity> overlappedReservations = new ArrayList<>();
        List<ReservationRecordEntity> reservations = reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
        for (ReservationRecordEntity reservation : reservations) {
            if (reservation.getPickUpDateTime().isAfter(pickupDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime)) {
                overlappedReservations.add(reservation);
            } else if (reservation.getReturnDateTime().isAfter(pickupDateTime) && reservation.getReturnDateTime().isBefore(returnDateTime)) {
                overlappedReservations.add(reservation);
            } else if(reservation.getReturnDateTime().compareTo(pickupDateTime) == 0 && reservation.getReturnOutlet().getOutletId() != selectedPickupOutletId){
                overlappedReservations.add(reservation);
            } else if(reservation.getReturnDateTime().isBefore(pickupDateTime) && reservation.getReturnDateTime().isAfter(pickupDateTime.minusHours(2))){
                if(reservation.getReturnOutlet().getOutletId() != selectedPickupOutletId){
                    overlappedReservations.add(reservation);
                }
            }
        }

        List<CarEntity> availableCars = retrieveAllCars();
        for (ReservationRecordEntity reservation : overlappedReservations) {
            availableCars.remove(reservation.getCarEntity());
        }

        for(CarEntity car:availableCars){
            if(car.getStatus().equals("Repair")){
                availableCars.remove(car);
            }
        }
        
        return availableCars;
    }
    
    @Override
    public List<CarEntity> filterCarsBasedOnCategoryId(List<CarEntity> cars, Long categoryId){
        
        List<CarEntity> filtered = new ArrayList<>(cars);
        for(CarEntity car:cars){
            if(car.getModelEntity().getCategoryEntity().getCategoryId() != categoryId){
                filtered.remove(car);
            }
        }
        return filtered;
    }
    
    @Override
    public List<CarEntity> filterCarsBasedOnModelId(List<CarEntity> cars, Long modelId){
     
        List<CarEntity> filtered = new ArrayList<>(cars);
        if (modelId == -1) {
            return filtered;
        } else {
            
            for (CarEntity car : cars) {
                if (car.getModelEntity().getModelId() != modelId) {
                    filtered.remove(car);
                }
            }
            return filtered;

        }

    }
}
