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
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author admin
 */
@Entity
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(nullable = false, length = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    private String lastName;
    @Column(nullable = false, length = 32)
    private String role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessRightEnum accessRightEnum;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;

    
   @ManyToOne
   @JoinColumn(nullable = false)
    private OutletEntity outletEntity;
    //@OneToMany(mappedBy = "employee")
   // private DispatchRecord dispatchRecord;
    @OneToMany
    private List<TransitDriverDispatchRecordEntity> dispatchRecord;

    public List<TransitDriverDispatchRecordEntity> getDispatchRecord() {
        return dispatchRecord;
    }

    public void setDispatchRecord(List<TransitDriverDispatchRecordEntity> dispatchRecord) {
        this.dispatchRecord = dispatchRecord;
    }
    
    public EmployeeEntity(){
        dispatchRecord = new ArrayList<>();
        
    }
    
    public EmployeeEntity(String firstName, String lastName, String role, AccessRightEnum accessRightEnum, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
    }
@XmlTransient
    public OutletEntity getOutletEntity() {
        return outletEntity;
    }

    public void setOutletEntity(OutletEntity outletEntity) {
        this.outletEntity = outletEntity;
    }

    /*public DispatchRecord getDispatchRecord() {
        return dispatchRecord;
    }

    public void setDispatchRecord(DispatchRecord dispatchRecord) {
        this.dispatchRecord = dispatchRecord;
    }*/
    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }
    
}
