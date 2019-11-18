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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kaixin
 */
@Entity
public class PartnerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(unique = true, nullable = false,length = 32)
    @NotNull
    @Size(min = 4,max = 32,message = "The length of username is at least 4 or at most 32.")
    private String username;
    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 8,max = 32,message = "The length of password is at least 8 or at most 32.")
    private String password;
    //bidirectional
    @OneToMany(mappedBy = "partner")
    private List<ReservationRecordEntity> reservationRecord;
    @OneToMany(mappedBy = "partner")
    private List<CustomerEntity> customer;

    public PartnerEntity() {
    }

    public PartnerEntity(String username, String password) {
        
        this();
        this.username = username;
        this.password = password;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getPartnerId() != null ? getPartnerId().hashCode() : 0);
        return hash;
    }

    public List<CustomerEntity> getCustomer() {
        return customer;
    }

    public void setCustomer(List<CustomerEntity> customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.getPartnerId() == null && other.getPartnerId() != null) || (this.getPartnerId() != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + getPartnerId() + " ]";
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
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

    public List<ReservationRecordEntity> getReservationRecord() {
        return reservationRecord;
    }

    public void setReservationRecord(List<ReservationRecordEntity> reservationRecord) {
        this.reservationRecord = reservationRecord;
    }

}
