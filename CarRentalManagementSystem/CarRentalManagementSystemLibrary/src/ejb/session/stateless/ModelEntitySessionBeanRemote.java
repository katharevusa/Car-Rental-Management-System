package ejb.session.stateless;

import entity.ModelEntity;
import java.util.List;
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteModelException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelFailureException;


public interface ModelEntitySessionBeanRemote {
    
    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException;

    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException;

    public List<ModelEntity> retrieveAllModel();
    
    public Long deleteModel(Long modelId) throws DeleteModelException;

    public ModelEntity createNewModel(ModelEntity newModelEntity, Long categoryId) throws CreateNewModelFailureException;
    public ModelEntity retrieveModelByMakeAndModel(String make,String model) throws ModelNotFoundException;

    public void updateModel(ModelEntity modelEntity, Long categoryId) throws UpdateModelFailureException;
}
