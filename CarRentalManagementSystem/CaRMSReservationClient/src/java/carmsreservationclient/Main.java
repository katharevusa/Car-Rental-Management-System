package carmsreservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB
    private static ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    @EJB(name = "OutletEntitySessionBeanRemote")
    private static OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    @EJB(name = "CarEntitySessionBeanRemote")
    private static CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    
    
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(reservationRecordEntitySessionBeanRemote,outletEntitySessionBeanRemote,customerEntitySessionBeanRemote,carEntitySessionBeanRemote);
        mainApp.runApp();
    }
    
    
    //test
    //test kaixin
    //test kaixin2
    //1
//    2
//    3
}
