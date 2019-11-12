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
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
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

    public OperationManagerModule(EmployeeEntity currentEmployee, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
    }

    public void menuOperationManagerModule() throws InvalidAccessRightException {

        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.OPERATIONSMANAGER) {
            throw new InvalidAccessRightException("You don't have EMPLOYEE rights to access the sales manager module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
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

            while (response < 1 || response > 11) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewModel();
                } else if (response == 2) {
                    doViewAllModels();
                } else if (response == 3) {
                    doUpdateModel();
                } else if (response == 4) {
                    doDeleteModel();
                } else if (response == 5) {
                    doCreateNewCar();
                } else if (response == 6) {
                    doViewAllCars();
                } else if (response == 7) {
                    doViewCarDetails();
                } else if (response == 8) {
                    doViewTDDR();
                } else if (response == 9) {
                    doAssignTD();
                } else if (response == 10) {
                    doUpdateTransitAsComplete();
                } else if (response == 11) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 11) {
                break;
            }
        }
    }

    

    

    private void doUpdateModel() {
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



    private void showInputDataValidationErrorsForCarEntity(Set<ConstraintViolation<CarEntity>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    
    private void doCreateNewModel() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new model name>");
        String modelName = sc.nextLine();
        System.out.print("Enter category ID>");
        Long categoryId = sc.nextLong();
        sc.nextLine();
        
        ModelEntity newModelEntity = new ModelEntity(modelName);
        
        try{
            modelEntitySessionBeanRemote.createNewModel(newModelEntity,categoryId);
            System.out.println("New model " + modelName + "successfully created!");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        } catch(CreateNewModelFailureException ex){
            System.out.println(ex.getMessage());
        }

    }
    
    private void doViewAllModels() {
        
        Scanner sc = new Scanner(System.in);
        List<ModelEntity> models = modelEntitySessionBeanRemote.retrieveAllModel();
        
        for (ModelEntity modelEntity : models){
            System.out.printf("%20s%20s",modelEntity.getCategoryEntity().getName(),modelEntity.getModelName());
            System.out.println();
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();

    }
    
    
    private void doDeleteModel() {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter model ID> ");
        Long modelId = sc.nextLong();

        try {

            modelId = modelEntitySessionBeanRemote.deleteModel(modelId);
            System.out.println("Model " + modelId + "has been successfully deleted.");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
            
        } catch (DeleteModelException ex) {
            System.out.println(ex.getMessage());
        }

    }
    
    
    private void doCreateNewCar(){
        
        Scanner sc = new Scanner(System.in);
        CarEntity newCarEntity = new CarEntity();
        
        System.out.print("Enter model ID>");
        Long modelId = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter license plate number> ");
        String plateNumber = sc.nextLine().trim();
        newCarEntity.setPlateNumber(plateNumber);
        System.out.print("Enter color> ");
        String color = sc.nextLine().trim();
        newCarEntity.setColor(color);
        System.out.print("Enter outlet>");
        List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveAllOutlet();
        for(OutletEntity outlet : outlets){
            System.out.println(outlet.getOutletId()+" "+outlet.getName());
        }

        try {
            
            Long carId = carEntitySessionBeanRemote.createNewCar(newCarEntity, modelId);
            System.out.println("New Car " + carId + "has been successfully created.");
        
        }catch(NewCarCreationException ex){
            System.out.println(ex.getMessage());
        }

    }
    
    
    private void doViewAllCars(){
        
        Scanner sc = new Scanner(System.in);
        
        List<CarEntity> cars = carEntitySessionBeanRemote.retrieveAllCars();
        System.out.printf("%30s%40s%30s\n","Car Category","Make and Model","License Plate Number");
        for (CarEntity car : cars){
            System.out.printf("%30s%40s%30s\n",car.getModelEntity().getCategoryEntity().getName(),car.getModelEntity().getModelName(),car.getPlateNumber());
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

            CarEntity car = carEntitySessionBeanRemote.retrieveCarByCarId(carId);
            System.out.println("Current car :" + car.getCarId());
            
            System.out.printf("%20s%20s%20s\n","Model Name","Plate Number","Color");
            System.out.printf("%20s%20s%20s\n",car.getModelEntity().getModelName(),car.getPlateNumber(),car.getColor());
            System.out.print("Press any key to continue...>");
            sc.nextLine();
            
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
//                doUpdateCar(carEntity);
            } else if (response == 2) {
                doDeleteCar(car);
            } else if (response == 3){
                //do nothing
            } else {
                System.out.println("Invalid Option");
            }

        } catch (CarNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    
    private void doDeleteCar(CarEntity carToDelete){
        
        Scanner sc = new Scanner(System.in);
        
        try{
            Long carId = carEntitySessionBeanRemote.deleteCar(carToDelete.getCarId());
            System.out.println("Car ID " + carId + " has been successfully deleted.");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        
        }catch(DeleteCarException ex){
            System.out.println(ex.getMessage());
        }

    }
}
