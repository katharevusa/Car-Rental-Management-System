/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private Long id;
    private DispatchRecordEnum dispatchRecordEnum;
    @ManyToOne
    private OutletEntity outletEntity;
    @OneToOne
    private ReservationRecordEntity reservationRecords;
    @ManyToOne
    private EmployeeEntity employee;

    public TransitDriverDispatchRecordEntity() {
        this.dispatchRecordEnum = DispatchRecordEnum.UNASSIGNED;

    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public OutletEntity getOutlet() {
        return outletEntity;
    }

    public void setOutlet(OutletEntity outletEntity) {
        this.outletEntity = outletEntity;
    }

    public DispatchRecordEnum getDispatchRecordEnum() {
        return dispatchRecordEnum;
    }

    public void setDispatchRecordEnum(DispatchRecordEnum dispatchRecordEnum) {
        this.dispatchRecordEnum = dispatchRecordEnum;
    }

    public ReservationRecordEntity getReservationRecords() {
        return reservationRecords;
    }

    public void setReservationRecords(ReservationRecordEntity reservationRecords) {
        this.reservationRecords = reservationRecords;
    }



    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransitDriverDispatchRecordEntity)) {
            return false;
        }
        TransitDriverDispatchRecordEntity other = (TransitDriverDispatchRecordEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecordEntity[ id=" + id + " ]";
    }
    
}
