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
import java.util.List;


public interface RentalDayEntitySessionBeanLocal {
   public void createNewRentalDay(RentalRateEntity rentalRate, LocalDate date);

    public List<RentalDayEntity> retrieveAllRentalDays();
}
