/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class RentalRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false,unique = true)
    private String rentalRateName;
    @Column(nullable = false, length = 12)
    private Double ratePerDay;
   // private String validityPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    //validity period i.e. monday-wednesday 1-3
    @ManyToOne
    private CategoryEntity category;
    //setting up uni directional for RENTALDAY AND RENTALRATE
    //RENTALRATE -> RENTALDAY
    @OneToMany
     (cascade=CascadeType.ALL, orphanRemoval=true)
    private List<RentalDayEntity> rentalDay;


    public RentalRateEntity() {
        rentalDay = new ArrayList<>();
    }

    public RentalRateEntity(String rentalRateName, Double ratePerDay, LocalDate startDate, LocalDate endDate)
    {
        this();
        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.endDate = endDate;
        this.startDate = startDate;
    }
    


    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRentalRateId() != null ? getRentalRateId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRateEntity)) {
            return false;
        }
        RentalRateEntity other = (RentalRateEntity) object;
        if ((this.getRentalRateId() == null && other.getRentalRateId() != null) || (this.getRentalRateId() != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRateEntity[ id=" + getRentalRateId() + " ]";
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    public String getRentalRateName() {
        return rentalRateName;
    }

    public void setRentalRateName(String rentalRateName) {
        this.rentalRateName = rentalRateName;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public List<RentalDayEntity> getRentalDay() {
        return rentalDay;
    }

    public void setRentalDay(List<RentalDayEntity> rentalDay) {
        this.rentalDay = rentalDay;
    }



    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(Double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    
}
