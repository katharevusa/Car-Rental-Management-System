/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateEntity;
import java.util.Date;


public interface RentalDayEntitySessionBeanLocal {
    public void createNewRentalDay(RentalRateEntity rentalRate, Date startDate, Date endDate);
}
