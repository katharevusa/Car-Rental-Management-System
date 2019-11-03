package ejb.session.stateless;

import entity.ModelEntity;
import util.exception.ModelNotFoundException;


public interface ModelEntitySessionBeanRemote {
    
    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException;

    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException;
    
}
