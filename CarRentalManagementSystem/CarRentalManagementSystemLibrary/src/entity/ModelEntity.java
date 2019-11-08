/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class ModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;
    @Column(unique = true,nullable = false)
    private String modelName;
    private boolean disabled;
    
    //bidirctional
    @ManyToOne
    @Column(nullable = false)
    private CategoryEntity category;
    
    //bidirdctional
    @OneToMany(mappedBy = "modelEntity")
    private List<CarEntity> cars;

    public ModelEntity() {
        cars = new ArrayList<>();
    }

    public ModelEntity(String modelName) {
        
        this();
        this.modelName = modelName;
        disabled = false;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getModelId() != null ? getModelId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the modelId fields are not set
        if (!(object instanceof ModelEntity)) {
            return false;
        }
        ModelEntity other = (ModelEntity) object;
        if ((this.getModelId() == null && other.getModelId() != null) || (this.getModelId() != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ModelEntity[ id=" + getModelId() + " ]";
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<CarEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

 
    
}
