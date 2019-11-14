/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package carmsmanagementclient;

import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author admin
 */
class CustomerServiceExecutiveModule {
    private EmployeeEntity currentEmployee;
    
    private ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    public CustomerServiceExecutiveModule(EmployeeEntity currentEmployee, ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote) {
        this.currentEmployee = currentEmployee;
        this.reservationRecordEntitySessionBeanRemote = reservationRecordEntitySessionBeanRemote;
    }
    
    public void menuCustomerServiceExecutiveModule() throws InvalidAccessRightException{ {
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.CUSTOMERSERVICEEXECUTIVE)
        {
            throw new InvalidAccessRightException("You don't have EMPLOYEE rights to access the sales manager module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Car Rental Management System :: Customer Service Executive module ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Log out\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                    doPickupCar();
                }
                else if(response == 2)
                {
                    doReturnCar();
                }
                else if(response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    }

    private void doPickupCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter reservation id>");
       // reservationRecordEntitySessionBeanRemote.retrieveReservationByReservationId(sc.nextLong());
        
    }

    private void doReturnCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
