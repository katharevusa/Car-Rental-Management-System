/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
public class OutletEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, length = 64)
    private String name;
    //do we need an outlet name?
    @Column(nullable = false, length = 128)
    private String address;

    private LocalTime openingTime;
  
    private LocalTime closingTime;

    //bidirectional
    @OneToMany(mappedBy = "outletEntity")
    private List<CarEntity> cars;
    //bidirectional
    @OneToMany(mappedBy = "outletEntity")
    private List<EmployeeEntity> employees;
    @OneToMany(mappedBy = "outletEntity")
    private List<TransitDriverDispatchRecordEntity> dispatchRecord;
    

    public OutletEntity(){
        employees = new ArrayList<>();
        cars = new ArrayList<>();
        dispatchRecord = new ArrayList<>();
    }

    public List<TransitDriverDispatchRecordEntity> getDispatchRecord() {
        return dispatchRecord;
    }

    public void setDispatchRecord(List<TransitDriverDispatchRecordEntity> dispatchRecord) {
        this.dispatchRecord = dispatchRecord;
    }
    
    public OutletEntity(String name, String address,LocalTime openingTime, LocalTime closingTime) {
        this();
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOutletId() != null ? getOutletId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof OutletEntity)) {
            return false;
        }
        OutletEntity other = (OutletEntity) object;
        if ((this.getOutletId() == null && other.getOutletId() != null) || (this.getOutletId() != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + getOutletId() + " ]";
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
@XmlTransient
    public List<CarEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    public List<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeEntity> employees) {
        this.employees = employees;
    }
    
}
