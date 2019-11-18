package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import javax.xml.bind.annotation.XmlTransient;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import util.enumeration.CarStatusEnum;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class CarEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @NotNull
    @Size(max = 32)
    @Column(nullable = false, unique = true)
    private String plateNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CarStatusEnum status;
    @NotNull
    @Size(max = 32)
    private String make;
    @NotNull
    @Size(max = 32)
    private String model;
    private boolean disabled = false;
    //bidirectional
    @ManyToOne
    private ModelEntity modelEntity;
    //bidirectional
    @ManyToOne
    private OutletEntity outletEntity;
    //bidirectional
    @OneToOne(mappedBy = "carEntity")
    private ReservationRecordEntity reservationRecordEntity;

    
    public CarEntity() {
        

    }
    public CarEntity(String plateNumber,String make,String model, CarStatusEnum status) {
        
        this();
        
        this.plateNumber = plateNumber;
        this.model = model;
        this.make = make;
        this.status = status;

    }
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCarId() != null ? getCarId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof CarEntity)) {
            return false;
        }
        CarEntity other = (CarEntity) object;
        if ((this.getCarId() == null && other.getCarId() != null) || (this.getCarId() != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarEntity[ id=" + getCarId() + " ]";
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }


    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    @XmlTransient
    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public void setModelEntity(ModelEntity modelEntity) {
        this.modelEntity = modelEntity;
    }
    @XmlTransient
    public OutletEntity getOutletEntity() {
        return outletEntity;
    }

    public void setOutletEntity(OutletEntity outletEntity) {
        this.outletEntity = outletEntity;
    }
    @XmlTransient
    public ReservationRecordEntity getReservationRecordEntity() {
        return reservationRecordEntity;
    }

    public void setReservationRecordEntity(ReservationRecordEntity reservationRecordEntity) {
        this.reservationRecordEntity = reservationRecordEntity;
    }

    public CarStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CarStatusEnum status) {
        this.status = status;
    }

    

}
