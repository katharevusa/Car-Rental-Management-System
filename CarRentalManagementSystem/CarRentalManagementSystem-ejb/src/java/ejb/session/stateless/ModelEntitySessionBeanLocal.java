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
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.UpdateModelFailureException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface ModelEntitySessionBeanLocal {

    public ModelEntity createNewModel(ModelEntity newModelEntity, Long categoryId) throws CreateNewModelFailureException;
    
    public ModelEntity retrieveModelByMakeAndModel(String make, String model) throws ModelNotFoundException;
    
    public List<ModelEntity> retrieveAllModel();
    
    public ModelEntity retrieveModelByModelId(Long modelId) throws ModelNotFoundException;
    
    public ModelEntity retrieveModelByName(String modelName) throws ModelNotFoundException;

    public Long deleteModel(Long modelId) throws DeleteModelException;
    
    public void updateModel(ModelEntity model) throws UpdateModelFailureException, InputDataValidationException;
    
    public Long retrieveCategoryIdByModelId(Long modelId) throws ModelNotFoundException;
}
