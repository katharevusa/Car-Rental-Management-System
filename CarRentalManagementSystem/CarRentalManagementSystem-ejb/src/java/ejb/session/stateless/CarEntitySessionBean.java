package ejb.session.stateless;

import entity.CarEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import java.util.List;
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
    

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;
    

    public CarEntitySessionBean() {
    }

    
    
    @Override
    public Long createNewCar(CarEntity newCarEntity,Long modelId) throws NewCarCreationException{
        
        
        try
        {
            ModelEntity modelEntity = modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
            em.persist(newCarEntity);
            newCarEntity.setModelEntity(modelEntity);
            modelEntity.getCars().add(newCarEntity);
            return newCarEntity.getCarId();
            
        }catch (ModelNotFoundException ex){
            throw new NewCarCreationException("Model ID " + modelId + " does not exist!");
        }catch(PersistenceException ex){
            throw new NewCarCreationException("License plate number " + newCarEntity.getPlateNumber() + " already exists!");
        }
        
    }
    
    @Override
    public CarEntity retrieveCarByCarId(Long carId) throws CarNotFoundException{
        
        CarEntity carEntity = em.find(CarEntity.class,carId);
        
        if (carEntity != null){
            return carEntity;
        } else {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
    }
    
    
    @Override
    public List<CarEntity> retrieveAllCars()
    {
        Query query = em.createQuery("SELECT c FROM CarEntity c ORDER BY c.modelEntity.category.name, c.modelEntity.modelName,c.plateNumber ASC");
        
        return query.getResultList();
         
    }
    
//    public void updateCar(CarEntity carEntity) throws CarNotFoundException,UpdateCarException{
//        
//        if (carEntity.getCarId() != null){
//            
//            CarEntity carEntityToUpdate = retrieveCarByCarId(carEntity.getCarId());
//            
//            if (carEntityToUpdate.getPlateNumber().equals(carEntity.getPlateNumber())) {
//                carEntityToUpdate.setOnRental(carEntity.isOnRental());
//                carEntityToUpdate.setColor(carEntity.getColor());
//                carEntityToUpdate.setLocation1(carEntity.getLocation1());
//                carEntityToUpdate.setLocation2(carEntity.getLocation2());
//            }else {
//                throw new UpdateCarException("License plate number of car record to be updated does not match the existing record");
//            }
//            
//        } else {
//            throw new CarNotFoundException("Car ID not provided for car to be updated");
//        }
//    }
    
    
    
    @Override
    public Long deleteCar(Long carId) throws DeleteCarException{
        
        try{
            CarEntity carEntityToRemove = retrieveCarByCarId(carId);
            if (carEntityToRemove.isOnRental() == false) {
                
                carEntityToRemove.getModelEntity().getCars().remove(carEntityToRemove);
                if (carEntityToRemove.getOutletEntity() != null){
                    carEntityToRemove.getOutletEntity().getCars().remove(carEntityToRemove);
                }
                em.remove(carEntityToRemove);
                return carEntityToRemove.getCarId();
                
            } else {
                
                carEntityToRemove.setDisabled(true);
                throw new DeleteCarException();
            }
            
        } catch (CarNotFoundException ex1){
            throw new DeleteCarException("Car ID " + carId + "does not exist!");
        } catch (DeleteCarException ex2){
            throw new DeleteCarException("The car is currently in usage and cannot be deleted, but it has been disabled for fruther rental.");
        }
        
    }



        
}
