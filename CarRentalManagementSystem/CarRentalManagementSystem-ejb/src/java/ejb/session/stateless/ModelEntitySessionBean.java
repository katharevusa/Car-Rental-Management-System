package ejb.session.stateless;

import com.sun.tools.ws.processor.model.ModelException;
import entity.ModelEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteModelException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
@Local(ModelEntitySessionBeanLocal.class)
@Remote(ModelEntitySessionBeanRemote.class)

public class ModelEntitySessionBean implements ModelEntitySessionBeanRemote, ModelEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public ModelEntitySessionBean() {
    }

    public Long createNewModel(ModelEntity modelEntity) throws InvalidFieldEnteredException{
        
        try
        {
            em.persist(modelEntity);
            em.flush();
            
            return modelEntity.getModelId();
            
        } catch (PersistenceException ex){
            throw new InvalidFieldEnteredException();
        }
    } 
    
    public List<ModelEntity> retrieveAllModel(){
        Query query = em.createQuery("SELECT m FROM ModelEntity");
        return query.getResultList();
    }
    
    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException{
        
        ModelEntity modelEntity = em.find(ModelEntity.class, modelId);
        
        if (modelEntity != null){
            return modelEntity;
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + "does not exist!");
        }
    }

    public void updateModel(ModelEntity modelEntity) throws UpdateModelException,ModelNotFoundException{
        
        if (modelEntity.getModelId() != null){
            
            ModelEntity modelToUpdate = retrieveModelByModelId(modelEntity.getModelId());
            if (modelToUpdate.getMake().equals(modelEntity.getMake()) && modelToUpdate.getModel().equals(modelEntity.getModel())){
                modelToUpdate.setCategory(modelEntity.getCategory());
                modelToUpdate.setMake(modelEntity.getMake());
                modelToUpdate.setModel(modelEntity.getModel());
            } else {
                throw new UpdateModelException("Make and model of the model to be updated does not match the existing record");
            }
        } else {
            throw new ModelNotFoundException("Model ID not provided for model to be updated");
        }
    }
    
    public void deleteModel(Long modelId) throws DeleteModelException, ModelNotFoundException{
        
        
        ModelEntity modelEntityToRemove = retrieveModelByModelId(modelId);
        
        if (modelEntityToRemove != null){
            if (modelEntityToRemove.getCars().isEmpty()){
                em.remove(modelEntityToRemove);
            } else {
                modelEntityToRemove.setDisabled(true);
                throw new DeleteModelException("The model is currently in usage, thus cannot be deleted.");
            } 
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + "does not exist.");
        }
    }
    
    
    
}
