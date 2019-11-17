/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Column(nullable = false)
    @NotNull
    private String rentalRateName;
    @Column(nullable = false,precision = 11)
    @NotNull
    @DecimalMin("0.00")
    private Double ratePerDay;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @ManyToOne
    private CategoryEntity category;


    public RentalRateEntity() {

    }


    public RentalRateEntity(String rentalRateName, Double ratePerDay, LocalDateTime startDateTime, LocalDateTime endDateTime)
    {
        this();
        this.rentalRateName = rentalRateName;
        this.ratePerDay = ratePerDay;
        this.endDateTime = endDateTime;
        this.startDateTime = startDateTime;
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

    /**
     * @return the rentalRateId
     */
    public Long getRentalRateId() {
        return rentalRateId;
    }

    /**
     * @param rentalRateId the rentalRateId to set
     */
    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    /**
     * @return the rentalRateName
     */
    public String getRentalRateName() {
        return rentalRateName;
    }

    /**
     * @param rentalRateName the rentalRateName to set
     */
    public void setRentalRateName(String rentalRateName) {
        this.rentalRateName = rentalRateName;
    }

    /**
     * @return the ratePerDay
     */
    public Double getRatePerDay() {
        return ratePerDay;
    }

    /**
     * @param ratePerDay the ratePerDay to set
     */
    public void setRatePerDay(Double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    /**
     * @return the startDateTime
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the category
     */
    public CategoryEntity getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
    

    
}
