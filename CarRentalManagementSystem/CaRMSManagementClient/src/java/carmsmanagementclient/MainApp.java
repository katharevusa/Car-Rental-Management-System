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
                        
                     //   salesManagerModule = new SalesManagerModule(currentEmployee, rentalRateEntitySessionBeanRemote,categoryEntitySessionBeanRemote);
                        operationManagerModule = new OperationManagerModule(/*missing parameters*/);
                        customerServiceExecutiveModule = new CustomerServiceExecutiveModule(/*missing parameters*/);
                        
                        menuMain();
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

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Car Rental Management System  ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRightEnum().toString() + " rights\n");
            System.out.println("1: Sales manager");
            System.out.println("2: Operation manager");
            System.out.println("3: Customer service executive");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                   // salesManagerModule.menuSalesManagerModule();
                }
                else if(response == 2)
                {
                        operationManagerModule.menuOperationManager();
                    }
                else if(response == 3)
                {
                        customerServiceExecutiveModule.menuCustomerServiceExecutive();
                    }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }

  
    
    
    
}
