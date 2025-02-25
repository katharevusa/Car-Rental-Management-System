package carmsreservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Main {


    @EJB
    private static RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    @EJB
    private static CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    @EJB
    private static ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    @EJB
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    @EJB
    private static CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    @EJB
    private static ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    
    
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(reservationRecordEntitySessionBeanRemote,outletEntitySessionBeanRemote,
                customerEntitySessionBeanRemote, carEntitySessionBeanRemote, categoryEntitySessionBeanRemote,
                rentalRateEntitySessionBeanRemote,modelEntitySessionBeanRemote);

        mainApp.runApp();
    }
   
}
