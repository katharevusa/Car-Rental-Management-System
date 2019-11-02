/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.AccessRightEnum;
import static util.enumeration.AccessRightEnum.CUSTOMERSERVICEEXECUTIVE;
import static util.enumeration.AccessRightEnum.OPERATIONSMANAGER;
import static util.enumeration.AccessRightEnum.SALESMANAGER;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author admin
 */
class MainApp {

   
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    private EmployeeEntity currentEmployee;
    private  AccessRightEnum currentAccessRight;
    private SalesManagerModule salesManagerModule;
    private OperationManagerModule operationManagerModule;
    private CustomerServiceExecutiveModule customerServiceExecutiveModule;


    
    public MainApp(){
    }
    
    public MainApp(OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote, 
            CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote) {
    this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
    this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
    this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
    this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
    this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;

    }

    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Car Rental Management System  ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        if(currentEmployee.getAccessRightEnum() == AccessRightEnum.SALESMANAGER){
                        salesManagerModule = new SalesManagerModule(currentEmployee, rentalRateEntitySessionBeanRemote,categoryEntitySessionBeanRemote);
                            try {
                                salesManagerModule.menuSalesManagerModule();
                            } catch (InvalidAccessRightException ex) {
                                System.out.println("invalid access");
                            }
                        }
                        else if(currentEmployee.getAccessRightEnum() == AccessRightEnum.OPERATIONSMANAGER){
                        operationManagerModule = new OperationManagerModule(currentEmployee);
                         try {
                                operationManagerModule.menuOperationManagerModule();
                            } catch (InvalidAccessRightException ex) {
                                System.out.println("invalid access");
                            }
                        }
                        else if(currentEmployee.getAccessRightEnum() == AccessRightEnum.CUSTOMERSERVICEEXECUTIVE){
                        customerServiceExecutiveModule = new CustomerServiceExecutiveModule(currentEmployee);
                        try {
                                customerServiceExecutiveModule.menuCustomerServiceExecutiveModule();
                            } catch (InvalidAccessRightException ex) {
                                System.out.println("invalid access");
                            }
                        }
                        
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
        
    }
    
        private void doLogin() throws InvalidLoginCredentialException 
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CaRMSystem :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployee = employeeEntitySessionBeanRemote.employeeLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    }

  
    
    
    

