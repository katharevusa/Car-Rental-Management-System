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
import entity.RentalDayEntity;
import entity.RentalRateEntity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.CategoryNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidFieldEnteredException;
import util.exception.RentalRateExistException;
import util.exception.UnknownPersistenceException;
import java.lang.String;
import java.util.List;

/**
 *
 * @author admin
 */
class SalesManagerModule {


    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private EmployeeEntity currentEmployee;
    private CategoryEntity enteredCategory;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    private SimpleDateFormat dateFormatter;

    public SalesManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        dateFormatter =  new SimpleDateFormat();
        this.enteredCategory = enteredCategory;
    }

    public SalesManagerModule(EmployeeEntity currentEmployee, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote,
        CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
        
    }
            
    
    public void menuSalesManagerModule() throws InvalidAccessRightException{
        
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.SALESMANAGER)
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
            System.out.println("4: Log out\n");
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
                    //update and delete be inside View Rental Rate details
                }
                else if(response == 3)
                {
                    doViewAllRentalRate();
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

    private void doCreateNewRentalRate()  

    {
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while(true)
        {
            System.out.println("*** CaRMS :: Sales manager :: Create New Rental Rate ***\n");
            System.out.println("Choose Car Category> \n");
            System.out.println("1. Luxury Sedan");
            System.out.println("2. Family Sedan");
            System.out.println("3. Standard Sedan");
            System.out.println("4. SUV/Minivan Sedan");
            System.out.println("5. Back\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                    try {
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(1));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("category cannot be found");
                    }
                enterRentalRateDetails(enteredCategory);
                }
                else if(response == 2)
                {
                    try {  
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(2));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("category cannot be found");
                    }
                enterRentalRateDetails(enteredCategory);
                }
                else if(response == 3)
                {
                    try {  
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(3));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("category cannot be found");
                    }
                enterRentalRateDetails(enteredCategory);
                }
                else if(response == 4){
                    try {  
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(4));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("category cannot be found");
                    }
                enterRentalRateDetails(enteredCategory);
                }
                else if(response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 5)
            {
                break;
            }
        } 
    }
        private void enterRentalRateDetails(CategoryEntity enteredCategory) {
        Scanner scanner = new Scanner(System.in);
        RentalRateEntity newRentalRateEntity = new RentalRateEntity();
        System.out.println("*** CaRMS sales manager terminal :: Create rental rate under "+enteredCategory.getName()+" ***\n\n");
        System.out.print("Enter name of rental rate> ");
        newRentalRateEntity.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter rental rate per day> ");
        newRentalRateEntity.setRatePerDay(scanner.nextDouble());
        scanner.nextLine();
        System.out.print("Enter validity period> ");
//         RentalDayEntity newRentalDay = new RentalDayEntity();
//         while(scanner.hasNextLine()){
//            String inputdow = scanner.nextLine();
//            String inputDOW = inputdow.toUpperCase() ; 
//            DayOfWeek dow = DayOfWeek.valueOf(inputDOW);
//            newRentalDay.setDow(dow);
//            
//            //set up the relationship btween rental day and rental rate
//            
//            newRentalDay.setRentalRate(newRentalRateEntity);
//            newRentalRateEntity.getRentalDay().add(newRentalDay);
//            rentalDayEntitySessionBeanRemote.createNewRentalDay(newRentalDay);
//            System.out.println("New rental day of "+ newRentalDay.getDow().toString()+"is created");
//        }
        newRentalRateEntity.setValidityPeriod(scanner.nextLine().trim());
        
        Set<ConstraintViolation<RentalRateEntity>>constraintViolations = validator.validate(newRentalRateEntity);
        
        if(constraintViolations.isEmpty())
        {
            
            try
            {
                
                newRentalRateEntity = rentalRateEntitySessionBeanRemote.createNewRentalRate(enteredCategory.getCategoryId(), newRentalRateEntity);
                System.out.println("New rental rate of"+newRentalRateEntity.getRatePerDay()+" is created under "+ newRentalRateEntity.getCategory().getName()  + "\n");
           }
            catch(CategoryNotFoundException ex){
                System.out.println("Category not found!\n");
            }
            catch(RentalRateExistException ex)
            {
                System.out.println("An error has occurred while creating the rental rate!: The rental rate already exist\n");
            }
            catch(GeneralException ex)
            {
                System.out.println("An unknown error has occurred while creating the new rental rate!: " + ex.getMessage() + "\n");
            }

        }
        else
        {
            showInputDataValidationErrorsForRentalRateEntity(constraintViolations);
        }
    }
  
    
    private void showInputDataValidationErrorsForRentalRateEntity(Set<ConstraintViolation<RentalRateEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doViewAllRentalRate() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** :: View All Rental Rate ***\n");
        
        List<RentalRateEntity> rentalRateEntities = rentalRateEntitySessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%20s%20s%20s%20s%20s\n", "Rental rate id", "name", "rate per day", "validity period","category");

        for(RentalRateEntity rentalRateEntity:rentalRateEntities)
        {
            System.out.printf("%20s%20s%20f%20s%20s\n", rentalRateEntity.getRentalRateId(), rentalRateEntity.getRentalRateName(), 
                    rentalRateEntity.getRatePerDay(), rentalRateEntity.getValidityPeriod(),rentalRateEntity.getCategory().getName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}


