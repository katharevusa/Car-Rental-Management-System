/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package carmsmanagementclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import entity.CategoryEntity;
import entity.EmployeeEntity;
import entity.RentalRateEntity;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author admin
 */
class SalesManagerModule {


    
  /*  private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private EmployeeEntity currentEmployee;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;

    public SalesManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesManagerModule(EmployeeEntity currentEmployee, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote,
        CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
    }
            
    
    public void menuSalesManagerModule() throws InvalidAccessRightException{
        
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.EMPLOYEE)
        {
            throw new InvalidAccessRightException("You don't have EMPLOYEE rights to access the sales manager module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Car Rental Management System :: Sales manager module ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View Rental Rate Details");
            System.out.println("3: View All Rental Rate");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                    doCreateNewRentalRate();
                }
                else if(response == 2)
                {
                    //doViewRentalRateDetails();
                    //update and delete be inside View Rental Rate details?
                }
                else if(response == 3)
                {
                    //doViewAllRentalRate();
                }
                else if(response == 4)
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

    private void doCreateNewRentalRate() throws CategoryNotFoundException {
        {
        Scanner scanner = new Scanner(System.in);
        RentalRateEntity newRentalRateEntity = new RentalRateEntity();
        
        System.out.println("*** POS System :: System Administration :: Create New Rental Rate ***\n");
        System.out.print("Enter Car Category> ");
        String categoryname = scanner.nextLine().trim();
        CategoryEntity enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryName(categoryname);
        System.out.print("Enter name of rental rate> ");
        newRentalRateEntity.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter rental rate per day> ");
        newRentalRateEntity.setRatePerDay(scanner.nextBigDecimal());
        System.out.print("Enter validity period> ");
        String dateFormat = "dd/MM/yyyy";
        newRentalRateEntity.setValidityPeriod(new SimpleDateFormat(dateFormat).parse(scanner.nextLine()));
        
        
        while(true)
        {
            System.out.print("Select Access Right (1: Cashier, 2: Manager)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 2)
            {
                newStaffEntity.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newStaffEntity.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newStaffEntity.setPassword(scanner.nextLine().trim());
        
        Set<ConstraintViolation<StaffEntity>>constraintViolations = validator.validate(newStaffEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                Long newStaffId = staffEntitySessionBeanRemote.createNewStaff(newStaffEntity);
                System.out.println("New staff created successfully!: " + newStaffId + "\n");
            }
            catch(StaffUsernameExistException ex)
            {
                System.out.println("An error has occurred while creating the new staff!: The user name already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new staff!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForStaffEntity(constraintViolations);
        }
   }
    
}*/
}

