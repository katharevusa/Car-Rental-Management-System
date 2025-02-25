/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarAllocationSessionBeanRemote;
import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordEntitySessionBeanRemote;
import javax.ejb.EJB;


public class Main {

    @EJB
    private static TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote;
    @EJB
    private static CarAllocationSessionBeanRemote carAllocationSessionBeanRemote;
    @EJB
    private static ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    @EJB
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    @EJB
    private static CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    @EJB
    private static RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    @EJB
    private static ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    @EJB
    private static CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(outletEntitySessionBeanRemote, employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote,
                categoryEntitySessionBeanRemote, modelEntitySessionBeanRemote, rentalRateEntitySessionBeanRemote, carEntitySessionBeanRemote,
                reservationRecordEntitySessionBeanRemote, carAllocationSessionBeanRemote, transitDriverDispatchRecordEntitySessionBeanRemote);
        mainApp.runApp();
    }

}

