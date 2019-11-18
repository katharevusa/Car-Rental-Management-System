package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CHEN BINGSEN
 */
@Entity
public class CustomerEntity implements Serializable {

    private static final Logger LOG = Logger.getLogger(CustomerEntity.class.getName());
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(unique = true,nullable = false,length = 32)
    @NotNull
    @Size(min = 2, max = 32,message = "The length of username is at least 4 or at most 32.")
    private String username;
    @Column(nullable = false,length = 32)
    @NotNull
    @Size(min = 8, max = 32,message = "The length of password is at least 8 or at most 32.")
    private String password;
    @Column(nullable = false)
    @NotNull
    @Size(message = "Mobile number is invalid.")
    private String mobileNumber;
    @Column(nullable = false)
    private String email;
    @ManyToOne
    private PartnerEntity partner;
    //bidirectional
    @OneToMany(mappedBy = "customerEntity")
    private List<ReservationRecordEntity> reservations;
    
  
    
    public CustomerEntity() {
        reservations = new ArrayList<>();
    }

    //overloaded constructor
    public CustomerEntity(String userName, String password, String mobileNumber, String email) {
        
        this();
        
        this.username = userName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCustomerId() != null ? getCustomerId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.getCustomerId() == null && other.getCustomerId() != null) || (this.getCustomerId() != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + getCustomerId() + " ]";
    }
    @XmlTransient
    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ReservationRecordEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationRecordEntity> reservations) {
        this.reservations = reservations;
    }

    
}
