/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package carmsmanagementclient;

import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import entity.CarEntity;
import entity.EmployeeEntity;
import entity.ReservationRecordEntity;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationRecordNotFoundException;

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
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter reservation id>");
            ReservationRecordEntity reservation = reservationRecordEntitySessionBeanRemote.retrieveReservationBylId(sc.nextLong());
            reservation.getCarEntity().setStatus(CarStatusEnum.ONRENTAL);
            reservation.getCarEntity().setOutletEntity(null);
            reservation.setHasPast(true);
            if(reservation.getPaidAmount()==0){
                System.out.println("Please collect the payment of "+reservation.getRentalRate());
            }
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
            
        
    }

    private void doReturnCar() {
        try{
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter reservation id>");
            ReservationRecordEntity reservation = reservationRecordEntitySessionBeanRemote.retrieveReservationBylId(sc.nextLong());
            CarEntity car = reservation.getCarEntity();
            car.setStatus(CarStatusEnum.AVAILABLE);
            car.setOutletEntity(reservation.getReturnOutlet());
            car.setReservationRecordEntity(null);
            reservation.setCarEntity(null);
        }catch(ReservationRecordNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
}
