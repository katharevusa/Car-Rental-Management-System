/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class ReservationRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationRecordId;
    @Column(nullable = false)
    private LocalDateTime pickUpDateTime;
    @Column(nullable = false)
    private LocalDateTime returnDateTime;
    private double rentalRate;
    private Boolean isCancelled = false;
    private Double paidAmount;
    private Double refund;

    //uni
    @OneToOne
    private CategoryEntity category;
    //uni
    @OneToOne
    private ModelEntity model;
    //bidirectional
    @OneToOne
    private CarEntity carEntity;
    //unidirectional
    @OneToOne
    private OutletEntity pickUpOutlet;
    //unidirectional
    @OneToOne
    private OutletEntity returnOutlet;
    //bidirectional
    @ManyToOne
    @JoinColumn(nullable = false)
    private CustomerEntity customerEntity;
    @ManyToOne
    @JoinColumn(nullable = true)
    private PartnerEntity partner;
    
   

    public ReservationRecordEntity() {
        
    }

    public ReservationRecordEntity(Double rentalRate, LocalDateTime pickUpDateTime, LocalDateTime returnDateTime) {
        
        this();
        
        this.rentalRate = rentalRate;
        this.pickUpDateTime = pickUpDateTime;
        this.returnDateTime = returnDateTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getReservationRecordId() != null ? getReservationRecordId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationRecordId fields are not set
        if (!(object instanceof ReservationRecordEntity)) {
            return false;
        }
        ReservationRecordEntity other = (ReservationRecordEntity) object;
        if ((this.getReservationRecordId() == null && other.getReservationRecordId() != null) || (this.getReservationRecordId() != null && !this.reservationRecordId.equals(other.reservationRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationRecordEntity[ id=" + getReservationRecordId() + " ]";
    }

    public Long getReservationRecordId() {
        return reservationRecordId;
    }

    public void setReservationRecordId(Long reservationRecordId) {
        this.reservationRecordId = reservationRecordId;
    }

    public LocalDateTime getPickUpDateTime() {
        return pickUpDateTime;
    }

    public void setPickUpDateTime(LocalDateTime pickUpDateTime) {
        this.pickUpDateTime = pickUpDateTime;
    }

    public LocalDateTime getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(LocalDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getRefund() {
        return refund;
    }

    public void setRefund(Double refund) {
        this.refund = refund;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public ModelEntity getModel() {
        return model;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }

    public CarEntity getCarEntity() {
        return carEntity;
    }

    public void setCarEntity(CarEntity carEntity) {
        this.carEntity = carEntity;
    }

    public OutletEntity getPickUpOutlet() {
        return pickUpOutlet;
    }

    public void setPickUpOutlet(OutletEntity pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    public OutletEntity getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(OutletEntity returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

    

}
