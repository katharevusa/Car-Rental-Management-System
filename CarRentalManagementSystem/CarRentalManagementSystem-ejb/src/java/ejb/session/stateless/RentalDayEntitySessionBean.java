/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalDayEntity;
import entity.RentalRateEntity;
import java.time.LocalDate;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Local(RentalDayEntitySessionBeanLocal.class)
@Remote(RentalDayEntitySessionBeanRemote.class)
@Stateless
public class RentalDayEntitySessionBean implements RentalDayEntitySessionBeanRemote, RentalDayEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createNewRentalDay(RentalRateEntity rentalRate, LocalDate startDate, LocalDate endDate){
        RentalDayEntity newRentalDay = new RentalDayEntity(startDate, endDate);
        em.persist(newRentalDay);
        newRentalDay.setRentalRate(rentalRate);
        rentalRate.getRentalDay().add(newRentalDay);
        em.flush();
        em.refresh(newRentalDay);
    }

}
