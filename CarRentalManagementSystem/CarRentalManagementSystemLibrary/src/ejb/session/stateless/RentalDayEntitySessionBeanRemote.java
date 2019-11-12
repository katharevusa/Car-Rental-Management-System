/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import util.exception.RentalRateNotFoundException;


public interface RentalDayEntitySessionBeanRemote {
   public void createNewRentalDay(RentalRateEntity rentalRate, LocalDate date);

    public void checkForExistenceOfRentalRate(LocalDateTime pickupDateTime, LocalDateTime returnDateTime) throws RentalRateNotFoundException;
}
