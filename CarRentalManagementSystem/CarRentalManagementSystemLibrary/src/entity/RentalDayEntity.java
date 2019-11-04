/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class RentalDayEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalDayId;
    @Column(nullable = false)
    private Date start;
    private Date end;
    
    @ManyToOne
    private RentalRateEntity rentalRate;


    public RentalDayEntity() {
        
    }

    public RentalDayEntity(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

  

    public RentalRateEntity getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(RentalRateEntity rentalRate) {
        this.rentalRate = rentalRate;
    }
    
    
    
    
    
    
    
    
    
//    //-----------relationship fields------------------------
//    @ManyToOne
//    private RentalRateEntity rentalRate;
//    @ManyToOne
//    private ReservationRecordEntity reservationRecordEntity;
    
    
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRentalDayId() != null ? getRentalDayId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalDayId fields are not set
        if (!(object instanceof RentalDayEntity)) {
            return false;
        }
        RentalDayEntity other = (RentalDayEntity) object;
        if ((this.getRentalDayId() == null && other.getRentalDayId() != null) || (this.getRentalDayId() != null && !this.rentalDayId.equals(other.rentalDayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalDayEntity[ id=" + getRentalDayId() + " ]";
    }

    public Long getRentalDayId() {
        return rentalDayId;
    }

    public void setRentalDayId(Long rentalDayId) {
        this.rentalDayId = rentalDayId;
    }
    
}
