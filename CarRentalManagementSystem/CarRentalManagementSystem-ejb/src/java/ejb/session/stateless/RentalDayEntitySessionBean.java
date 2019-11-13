/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalDayEntity;
import entity.RentalRateEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RentalRateNotFoundException;

@Local(RentalDayEntitySessionBeanLocal.class)
@Remote(RentalDayEntitySessionBeanRemote.class)
@Stateless
public class RentalDayEntitySessionBean implements RentalDayEntitySessionBeanRemote, RentalDayEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createNewRentalDay(RentalRateEntity rentalRate, LocalDate date){
        RentalDayEntity newRentalDay = new RentalDayEntity(date,rentalRate.getRatePerDay());
        em.persist(newRentalDay);
        newRentalDay.setRentalRate(rentalRate);
        rentalRate.getRentalDay().add(newRentalDay);
        em.flush();
        em.refresh(newRentalDay);
    }
    
    @Override
    public void checkForExistenceOfRentalRate(LocalDateTime pickupDateTime,LocalDateTime returnDateTime) throws RentalRateNotFoundException{
        
        boolean rentalDayExist;
        LocalDate curr = pickupDateTime.toLocalDate();
        List<RentalDayEntity> rentalDays = retrieveAllRentalDays();
        while(curr.compareTo(returnDateTime.toLocalDate())<=0){
            rentalDayExist = false;
            for(RentalDayEntity rentalDayEntity : rentalDays){
                if(curr.compareTo(rentalDayEntity.getDate()) == 0){
                    rentalDayExist = true;
                    break;
                }
            }
            if(!rentalDayExist){
                throw new RentalRateNotFoundException();
            }
            curr.plusDays(1);
        }
    }
    
    @Override
    public List<RentalDayEntity> retrieveAllRentalDays(){
        Query query = em.createQuery("SELECT rd FROM RentalDayEntity rd");
        return query.getResultList();
    }

}
