/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import entity.CarEntity;
import entity.EmployeeEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.CarNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;

/**
 *
 * @author admin
 */
class OperationManagerModule {
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private EmployeeEntity currentEmployee;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    
    
    public OperationManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    public OperationManagerModule(EmployeeEntity currentEmployee,ModelEntitySessionBeanRemote modelEntitySessionBeanRemote,OutletEntitySessionBeanRemote outletEntitySessionBeanRemote,CarEntitySessionBeanRemote carEntitySessionBeanRemote) {
        this();
        
        this.currentEmployee = currentEmployee;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
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



    private void doViewTDDR() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doAssignTD() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateTransitAsComplete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private void doCreateNewCar() {
        
        Scanner sc = new Scanner(System.in);
        CarEntity newCarEntity = new CarEntity();

        try {
            System.out.print("Enter Model name> ");
            String modelName = sc.nextLine().trim();
            ModelEntity modelEntity = modelEntitySessionBeanRemote.retrieveModelByName(modelName);
            System.out.print("Enter license plate number> ");
            String plateNumber = sc.nextLine().trim();
            newCarEntity.setPlateNumber(plateNumber);
            System.out.print("Enter color> ");
            String color = sc.nextLine().trim();
            newCarEntity.setColor(color);
            System.out.print("Enter outlet ID> ");
            Long outletId = sc.nextLong();
            OutletEntity outletEntity = outletEntitySessionBeanRemote.retrieveOutletByOutletId(outletId);
            
            Set<ConstraintViolation<CarEntity>> constraintViolations = validator.validate(newCarEntity);
            
            if (constraintViolations.isEmpty()){
                
                try{
                    newCarEntity = carEntitySessionBeanRemote.createNewCar(newCarEntity,modelEntity.getModelId(),outletEntity.getOutletId());
                    System.out.println("New car created successfully!: " + newCarEntity.getCarId() + "\n");
                }
                catch(InvalidFieldEnteredException ex){
                    System.out.println(ex.getMessage());
                }
                
            } else {
                showInputDataValidationErrorsForCarEntity(constraintViolations);
            }


        } catch (ModelNotFoundException modelNotFoundException){
            System.out.println(modelNotFoundException.getMessage());
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        } catch (OutletNotFoundException outletNotFoundException){
            System.out.println(outletNotFoundException.getMessage());
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        }

    }
    
    private void doViewAllCars() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** CaRMS System ::  View All Cars ***\n");
        
        List<CarEntity> allCars = carEntitySessionBeanRemote.retrieveAllCars();
        
        for (CarEntity carEntity : allCars){
            System.out.println(carEntity);
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private void doViewCarDetails() {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.print("Enter Car ID> ");
        Long carId = sc.nextLong();
        
        try {
            
            CarEntity carEntity = carEntitySessionBeanRemote.retrieveCarByCarId(carId);
            System.out.println(carEntity);
            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();
            
            if (response == 1){
//                doUpdateCar(carEntity);
            } else if(response == 2){
//                doDeleteCar(carEntity);
            }
       
        } catch (CarNotFoundException ex) {
            System.out.println("An error has occurred while retrieving car: " + ex.getMessage() + "\n");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        }
    }

    
    

    private void showInputDataValidationErrorsForCarEntity(Set<ConstraintViolation<CarEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }  
    
}
