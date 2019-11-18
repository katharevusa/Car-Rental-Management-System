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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;
import javax.validation.constraints.NotNull;
import util.enumeration.DispatchRecordEnum;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class TransitDriverDispatchRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dispatchRecordId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DispatchRecordEnum dispatchRecordStatus;
    //bi
    @OneToOne
    @JoinColumn(nullable = false)
    private ReservationRecordEntity reservationRecord;
    @ManyToOne(optional = true)
    private EmployeeEntity employee;

    
    
    public TransitDriverDispatchRecordEntity() {
        this.dispatchRecordStatus = DispatchRecordEnum.UNASSIGNED;
    }
    
    
    public Long getDispatchRecordId() {
        return dispatchRecordId;
    }

    public void setDispatchRecordId(Long dispatchRecordId) {
        this.dispatchRecordId = dispatchRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dispatchRecordId != null ? dispatchRecordId.hashCode() : 0);
        return hash;
    }


    public DispatchRecordEnum getDispatchRecordStatus() {
        return dispatchRecordStatus;
    }

    public void setDispatchRecordStatus(DispatchRecordEnum dispatchRecordStatus) {
        this.dispatchRecordStatus = dispatchRecordStatus;
    }


    @XmlTransient
    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dispatchRecordId fields are not set
        if (!(object instanceof TransitDriverDispatchRecordEntity)) {
            return false;
        }
        TransitDriverDispatchRecordEntity other = (TransitDriverDispatchRecordEntity) object;
        if ((this.dispatchRecordId == null && other.dispatchRecordId != null) || (this.dispatchRecordId != null && !this.dispatchRecordId.equals(other.dispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecordEntity[ id=" + dispatchRecordId + " ]";
    }

    public ReservationRecordEntity getReservationRecord() {
        return reservationRecord;
    }

    public void setReservationRecord(ReservationRecordEntity reservationRecord) {
        this.reservationRecord = reservationRecord;
    }
    
}
