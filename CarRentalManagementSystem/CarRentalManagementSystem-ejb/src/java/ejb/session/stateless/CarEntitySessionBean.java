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
import util.enumeration.CarStatusEnum;
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
    public Boolean checkCarAvailability(LocalDateTime pickupDateTime,LocalDateTime returnDateTime,
            Long selectedPickupOutletId, Long selectedReturnOutletId, Long selectedCategoryId, Long selectedModelId){
        
        Query query;
        int totalAvailableCars;
        if(selectedModelId.equals(-1)){
            query = em.createQuery("SELECT c FROM CarEntity c WHERE c.disabled = FALSE "
                    + "AND c.modelEntity.categoryEntity.categoryId = :inCategoryId");
            query.setParameter("inCategoryId", selectedCategoryId);
            totalAvailableCars = query.getResultList().size();
            
            System.out.println("************" + totalAvailableCars);
            
            query = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.isCancelled = FALSE AND r.category.categoryId = :inCategoryId");
            query.setParameter("inCategoryId", selectedCategoryId);
            List<ReservationRecordEntity> reservations = query.getResultList();
            List<ReservationRecordEntity> overlappedReservations = new ArrayList<>();

            //check for reservation made on the selected category that overlap with the new reservation
            for (ReservationRecordEntity reservation : reservations) {
                
                
                if(reservation.getPickUpDateTime().isBefore(pickupDateTime)){
                    if(reservation.getReturnDateTime().isAfter(pickupDateTime)){
                        overlappedReservations.add(reservation);
                    } else if (reservation.getReturnDateTime().isEqual(pickupDateTime)){
                        if(!(reservation.getReturnOutlet().getOutletId().equals(selectedPickupOutletId))){
                            overlappedReservations.add(reservation);
                        }
                    } else if(reservation.getReturnDateTime().isBefore(pickupDateTime)){
                        if(reservation.getReturnDateTime().isAfter(pickupDateTime.plusHours(2))){
                            if(!(reservation.getReturnOutlet().equals(selectedPickupOutletId))){
                                overlappedReservations.add(reservation);
                            }
                        }
                    }
                } else if(reservation.getPickUpDateTime().isEqual(pickupDateTime)){
                    overlappedReservations.add(reservation);
                } else if(reservation.getPickUpDateTime().isAfter(pickupDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime)){
                    overlappedReservations.add(reservation);
                } else if(reservation.getPickUpDateTime().isEqual(returnDateTime)){
                    if(!(reservation.getPickUpOutlet().equals(selectedReturnOutletId))){
                        overlappedReservations.add(reservation);
                    }
                } else if(reservation.getPickUpDateTime().isAfter(returnDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime.plusHours(2))){
                    if(!(reservation.getPickUpOutlet().getOutletId().equals(selectedReturnOutletId))){
                        overlappedReservations.add(reservation);
                    }
                }
                
            }
            
            totalAvailableCars = totalAvailableCars - overlappedReservations.size();
            System.out.println("************" + totalAvailableCars);
            
        } else {
            query = em.createQuery("SELECT c FROM CarEntity c WHERE c.disabled = FALSE "
                    + "AND c.modelEntity.modelId = :inModelId");
            query.setParameter("inModelId", selectedModelId);
            totalAvailableCars = query.getResultList().size();
            
            System.out.println("************" + totalAvailableCars);
            
            query = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.model.modelId = :inModelId AND r.isCancelled = FALSE");
            query.setParameter("inModelId", selectedModelId);
            List<ReservationRecordEntity> reservations = query.getResultList();
            List<ReservationRecordEntity> overlappedReservations = new ArrayList<>();
            
            for (ReservationRecordEntity reservation : reservations) {
                
                
                if(reservation.getPickUpDateTime().isBefore(pickupDateTime)){
                    if(reservation.getReturnDateTime().isAfter(pickupDateTime)){
                        overlappedReservations.add(reservation);
                    } else if (reservation.getReturnDateTime().isEqual(pickupDateTime)){
                        if(!(reservation.getReturnOutlet().getOutletId().equals(selectedPickupOutletId))){
                            overlappedReservations.add(reservation);
                        }
                    } else if(reservation.getReturnDateTime().isBefore(pickupDateTime)){
                        if(reservation.getReturnDateTime().isAfter(pickupDateTime.plusHours(2))){
                            if(!(reservation.getReturnOutlet().equals(selectedPickupOutletId))){
                                overlappedReservations.add(reservation);
                            }
                        }
                    }
                } else if(reservation.getPickUpDateTime().isEqual(pickupDateTime)){
                    overlappedReservations.add(reservation);
                } else if(reservation.getPickUpDateTime().isAfter(pickupDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime)){
                    overlappedReservations.add(reservation);
                } else if(reservation.getPickUpDateTime().isEqual(returnDateTime)){
                    if(!(reservation.getPickUpOutlet().equals(selectedReturnOutletId))){
                        overlappedReservations.add(reservation);
                    }
                } else if(reservation.getPickUpDateTime().isAfter(returnDateTime) && reservation.getPickUpDateTime().isBefore(returnDateTime.plusHours(2))){
                    if(!(reservation.getPickUpOutlet().getOutletId().equals(selectedReturnOutletId))){
                        overlappedReservations.add(reservation);
                    }
                }
                
               
            }
            
            totalAvailableCars = totalAvailableCars - overlappedReservations.size();
            System.out.println("************" + totalAvailableCars);
        }
        
        
        if(totalAvailableCars > 0){
            return true;
        } else {
            return false;
        }
        
    }
    
    @Override
    public List<CarEntity> filterCarsBasedOnCategoryId(List<CarEntity> cars, Long categoryId){
        
        List<CarEntity> allCars = new ArrayList<>(cars);
        List<CarEntity> filtered = new ArrayList<>();
        
        
        for(CarEntity car:allCars){
            em.merge(car);
            System.out.println(car.getModelEntity().getCategoryEntity().getCategoryId());
            if(car.getModelEntity().getCategoryEntity().getCategoryId().equals(categoryId)){
                filtered.add(car);
            }
        }
        return filtered;
    }
    
    @Override
    public List<CarEntity> filterCarsBasedOnModelId(List<CarEntity> cars, Long modelId){
     
        
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
}
