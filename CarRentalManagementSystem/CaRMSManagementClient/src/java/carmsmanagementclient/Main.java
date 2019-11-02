/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author admin
 */
public class Main {

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
    
    public static void main(String[] args) {
    MainApp mainApp = new MainApp(outletEntitySessionBeanRemote, employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, 
            categoryEntitySessionBeanRemote, rentalRateEntitySessionBeanRemote);
        mainApp.runApp();
    }
    
}
//abcdefg
