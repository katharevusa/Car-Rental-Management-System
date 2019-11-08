/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ModelEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteModelException;
import util.exception.ModelNotFoundException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface ModelEntitySessionBeanLocal {

    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException;

    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException;

    public ModelEntity createNewModel(ModelEntity newModelEntity) throws CreateNewModelFailureException;

    public List<ModelEntity> retrieveAllModel();

    public Long deleteModel(Long modelId) throws DeleteModelException;
    
}
