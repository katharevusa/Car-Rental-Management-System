package ejb.session.stateless;

import entity.ModelEntity;
import java.util.List;
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelFailureException;


public interface ModelEntitySessionBeanRemote {
    
    public ModelEntity createNewModel(ModelEntity newModelEntity, Long categoryId) throws CreateNewModelFailureException;
    
    public ModelEntity retrieveModelByMakeAndModel(String make, String model) throws ModelNotFoundException;
    
    public List<ModelEntity> retrieveAllModel();
    
    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException;
    
    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException;

    public Long deleteModel(Long modelId) throws DeleteModelException;
    
    public void updateModel(ModelEntity model) throws UpdateModelFailureException, InputDataValidationException;
}
