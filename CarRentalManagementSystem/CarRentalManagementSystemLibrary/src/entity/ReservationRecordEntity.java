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
    @Column(nullable = false)
    @NotNull
    private Boolean isCancelled;
    @Column(nullable = true)
    private Double paidAmount;
    @Column(nullable = true)
    private Double refund;

    @OneToOne
    //unidirectional
    private OutletEntity pickUpOutlet;
    //unidirectional
    @OneToOne
    private OutletEntity returnOutlet;
    @OneToOne
    private CarEntity carEntity;
    @OneToMany(mappedBy = "reservationRecordEntity")
    private List<RentalDayEntity> rentalDays;
    @ManyToOne
    private CustomerEntity customer;
    @ManyToOne
    private PartnerEntity partner;
    @OneToOne
    private CategoryEntity category;
    @OneToOne
    private ModelEntity model;
   

    public ReservationRecordEntity() {
        rentalDays = new ArrayList<>();
        isCancelled = false;

    }

    public ReservationRecordEntity(LocalDateTime pickUpDateTime, LocalDateTime returnDateTime, Boolean isCancelled,
            Double paidAmount, Double refund) {
        this();
        this.pickUpDateTime = pickUpDateTime;
        this.returnDateTime = returnDateTime;
        this.isCancelled = isCancelled;
        this.paidAmount = paidAmount;
        this.refund = refund;
        
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
    
    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
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

    public List<RentalDayEntity> getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(List<RentalDayEntity> rentalDays) {
        this.rentalDays = rentalDays;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
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

    public CarEntity getCarEntity() {
        return carEntity;
    }

    public void setCarEntity(CarEntity carEntity) {
        this.carEntity = carEntity;
    }



}
