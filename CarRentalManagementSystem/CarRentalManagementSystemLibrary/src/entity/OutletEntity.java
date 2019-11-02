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
    @Column(nullable = false)
    private Integer openingTime;
    @Column(nullable = false)
    private Integer closingTime;

    @OneToMany
    private List<EmployeeEntity> employees;
    
    public OutletEntity(){
        employees = new ArrayList<>();
    }
    
    public OutletEntity(String name, String address,Integer openingTime, Integer closingTime) {
        this();
        this.name = name;
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public List<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeEntity> employees) {
        this.employees = employees;
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

    public Integer getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Integer openingTime) {
        this.openingTime = openingTime;
    }

    public Integer getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Integer closingTime) {
        this.closingTime = closingTime;
    }
    
    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof OutletEntity)) {
            return false;
        }
        OutletEntity other = (OutletEntity) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }
    
}
