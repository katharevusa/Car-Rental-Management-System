/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author admin
 */
class OperationManagerModule {
    
    private EmployeeEntity currentEmployee;
    
    public OperationManagerModule(EmployeeEntity currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    
    public void menuOperationManagerModule() throws InvalidAccessRightException{
        
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.OPERATIONSMANAGER)
        {
            throw new InvalidAccessRightException("You don't have EMPLOYEE rights to access the sales manager module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Car Rental Management System :: Operations manager module ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("===============================");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("===============================");
            System.out.println("8: View Transit Driver Dispatch Record For Current Day Reservation");
            System.out.println("9: Assign Transit Driver");
            System.out.println("10: Update Transit As Complete");
            System.out.println("11: Log out\n");
            response = 0;
            
            while(response < 1 || response > 11)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                        doCreateNewModel();
                }
                else if(response == 2)
                {
                    doViewAllModels();
                }
                else if(response == 3)
                {
                    doUpdateModel();
                }
                else if(response == 4)
                {
                    doDeleteModel();
                }
                else if(response == 5)
                {
                    doCreateNewCar();
                }
                else if(response == 6)
                {
                    doViewAllCars();
                }
                else if(response == 7)
                {
                    doViewCarDetails();
                }
                else if(response == 8)
                {
                    doViewTDDR();
                }
                else if(response == 9)
                {
                    doAssignTD();
                }
                else if(response == 10)
                {
                    doUpdateTransitAsComplete();
                }
                else if(response == 11)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 11)
            {
                break;
            }
        }
    }

    private void doCreateNewModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCreateNewCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllCars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewCarDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewTDDR() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doAssignTD() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateTransitAsComplete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
