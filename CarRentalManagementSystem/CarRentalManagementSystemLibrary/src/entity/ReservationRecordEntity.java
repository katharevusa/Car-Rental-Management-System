/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
    
    //-----------relationship fields------------------------
    //bidirectional
    @OneToOne
    private CarEntity carEntity;
    
    
    
    
  
    
    
//    //-----------relationship fields------------------------
//    @OneToMany(mappedBy = "reservationRecordEntity")
//    private List<RentalDayEntity> rentalDays;
//    //unidirectional
//    @OneToOne
//    private OutletEntity pickupOutlet;
//    //unidirectional
//    @OneToOne
//    private OutletEntity returnOutlet;
//    //bidirectional
//    @ManyToOne
//    private CustomerEntity customer;
//    
//    //bidirectional
//    @ManyToOne
//    private PartnerEntity partner;
    
    

    
    


    public ReservationRecordEntity() {
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
