/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarAllocationSessionBeanRemote;
import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordEntitySessionBeanRemote;
import entity.CarEntity;
import entity.EmployeeEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.TransitDriverDispatchRecordEntity;
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
import util.enumeration.CarStatusEnum;
import util.enumeration.DispatchRecordEnum;
import util.exception.CarNotFoundException;
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidFieldEnteredException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
import util.exception.OutletNotFoundException;
import util.exception.UpdateCarException;

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
    private CarAllocationSessionBeanRemote carAllocationSessionBeanRemote;
    private TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    public OperationManagerModule() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationManagerModule(EmployeeEntity currentEmployee, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote,
            CarAllocationSessionBeanRemote carAllocationSessionBeanRemote, TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote,
            EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.carAllocationSessionBeanRemote = carAllocationSessionBeanRemote;
        this.transitDriverDispatchRecordEntitySessionBeanRemote = transitDriverDispatchRecordEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
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
            System.out.println("11: Allocate cars");
            System.out.println("12: Log out\n");
            response = 0;

            while (response < 1 || response > 12) {
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
                    try {
                        doCreateNewCar();
                    } catch (OutletNotFoundException ex) {
                        System.out.println("outlet not found");
                    }
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
                    doAllocateCars();
                } else if (response == 12) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 12) {
                break;
            }
        }
    }

    private void doUpdateModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewTDDR() {

        List<TransitDriverDispatchRecordEntity> dispatchRecord = outletEntitySessionBeanRemote.retrieveAllDispatchRecord(currentEmployee.getOutletEntity());
        System.out.println(dispatchRecord);
       // System.out.println(currentEmployee.getOutletEntity());
        Scanner sc = new Scanner(System.in);
        System.out.printf("%8s%20s%20s%20s%20s\n", "Id", "Outlet", "Status", "Reservation Id", "Assigned Driver");
       
        for (TransitDriverDispatchRecordEntity r : dispatchRecord) {
            System.out.printf("%8s%20s%20s%20s%20s\n", r.getId(), r.getOutlet().getName(), r.getDispatchRecordEnum(), r.getReservationRecord().getReservationRecordId(), r.getEmployeeEntity());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private void doAssignTD() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Dispatch Record ID>");
        TransitDriverDispatchRecordEntity dispatchRecord = transitDriverDispatchRecordEntitySessionBeanRemote.retrieveDispatchRecordById(sc.nextLong());
        sc.nextLine();
//if the dispatch record driver is not null, throw exception
        OutletEntity outlet = dispatchRecord.getOutlet();
        List<EmployeeEntity> employees = outletEntitySessionBeanRemote.retrieveAllEmployee(outlet);
        System.out.printf("%8s%20s%20s\n", "employee id", "employee username", "employee role");
        for (EmployeeEntity e : employees) {
            System.out.printf("%8s%20s%20s\n", e.getEmployeeId(), e.getUsername(), e.getRole());
        }
        System.out.println("Please assign a driver to this reservation");
        System.out.println("Enter the employee ID>");
        EmployeeEntity e = employeeEntitySessionBeanRemote.retrieveEmployeeByEmployeeId(sc.nextLong());
        sc.nextLine();
        dispatchRecord.setEmployeeEntity(e);
        dispatchRecord.setDispatchRecordEnum(DispatchRecordEnum.ASSIGNED);
        e.getDispatchRecord().add(dispatchRecord);
        System.out.println("Employee with ID of " + e.getEmployeeId() + "is successfully assigned to the reservation");
    }

    private void doUpdateTransitAsComplete() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the dispatch record id>");
        TransitDriverDispatchRecordEntity dispatchRecord = transitDriverDispatchRecordEntitySessionBeanRemote.retrieveDispatchRecordById(sc.nextLong());
        sc.nextLine();
        dispatchRecord.setDispatchRecordEnum(DispatchRecordEnum.COMPLETE);
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
        System.out.print("Enter new make name>");
        String makeName = sc.nextLine();
        System.out.print("Enter category ID>");
        Long categoryId = sc.nextLong();
        sc.nextLine();

        ModelEntity newModelEntity = new ModelEntity(modelName, makeName);

        try {
            modelEntitySessionBeanRemote.createNewModel(newModelEntity, categoryId);
            System.out.println("New model " + modelName + "successfully created!");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        } catch (CreateNewModelFailureException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doViewAllModels() {

        Scanner sc = new Scanner(System.in);
        List<ModelEntity> models = modelEntitySessionBeanRemote.retrieveAllModel();

        for (ModelEntity modelEntity : models) {
            System.out.printf("%20s%20s", modelEntity.getCategoryEntity().getName(), modelEntity.getModelName());
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

    private void doCreateNewCar() throws OutletNotFoundException {

        Scanner sc = new Scanner(System.in);
        CarEntity newCarEntity = new CarEntity();

        System.out.print("Enter license plate number> ");
        String plateNumber = sc.nextLine().trim();
        newCarEntity.setPlateNumber(plateNumber);

        List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveAllOutlet();
        for (OutletEntity outlet : outlets) {
            System.out.println(outlet.getOutletId() + " " + outlet.getName());
        }
        System.out.print("Enter outlet ID>");
        Long outletId = sc.nextLong();
        sc.nextLine();
        System.out.println("Enter status (Availble/Repair)>");
        CarStatusEnum status;
        if (sc.nextLine().trim().equals("Availble")) {
            status = CarStatusEnum.AVAILABLE;
        } else {
            status = CarStatusEnum.REPAIR;
        }
        newCarEntity.setStatus(status);
        System.out.println("Enter make>");
        String make = sc.nextLine().trim();
        newCarEntity.setMake(make);
        System.out.println("Enter model>");
        String model = sc.nextLine().trim();
        newCarEntity.setModel(model);
        sc.nextLine();

        try {

            Long carId = carEntitySessionBeanRemote.createNewCar(newCarEntity, make, model, outletId);
            System.out.println("New Car " + carId + "has been successfully created.");

        } catch (NewCarCreationException ex) {
            System.out.println(ex.getMessage());
        } catch (ModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doViewAllCars() {

        Scanner sc = new Scanner(System.in);

        List<CarEntity> cars = carEntitySessionBeanRemote.retrieveAllCars();
        System.out.printf("%8s%20s%20s%20s%20s%20s\n", "Id", "Car Category", "Make", "Model", "Status", "License Plate Number");
        for (CarEntity car : cars) {
            System.out.printf("%8s%20s%20s%20s%20s%20s\n", car.getCarId(), car.getModelEntity().getCategoryEntity().getName(), car.getMake(), car.getModel(), car.getStatus(), car.getPlateNumber());
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

            System.out.printf("%8s%20s%20s%20s%20s%20s\n", "Id", "Car Category", "Make", "Model", "Status", "License Plate Number");
            System.out.printf("%8s%20s%20s%20s%20s%20s\n", car.getCarId(), car.getModelEntity().getCategoryEntity().getName(), car.getMake(), car.getModel(), car.getStatus(), car.getPlateNumber());
            System.out.print("Press any key to continue...>");
            sc.nextLine();

            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
           //     doUpdateCar(car);
            } else if (response == 2) {
                doDeleteCar(car);
            } else {
                System.out.println("Invalid Option");
            }

        } catch (CarNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doDeleteCar(CarEntity carToDelete) {

        Scanner sc = new Scanner(System.in);

        try {
            Long carId = carEntitySessionBeanRemote.deleteCar(carToDelete.getCarId());
            System.out.println("Car ID " + carId + " has been successfully deleted.");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();

        } catch (DeleteCarException ex) {
            System.out.println(ex.getMessage());
        }

    }

  /*  private void doUpdateCar(CarEntity car) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** :: View Car Details :: Update Car ***\n");
        System.out.print("Enter Make (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            car.setMake(input);
        }

        System.out.print("Enter Model (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            car.setModel(input);
        }
        //set the model as well
        System.out.print("Enter Plate Number (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            car.setPlateNumber(input);
        }
        System.out.print("Enter Car status (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            car.setStatus(CarStatusEnum.valueOf(input));
        }
        System.out.println("Enter Outlet (blank if no change)> ");
        List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveAllOutlet();
        for (OutletEntity o : outlets) {
            System.out.println(o.getOutletId() + " " + o.getName());
        }
        input = scanner.nextLine().trim();
        OutletEntity outlet;
        try {
            outlet = outletEntitySessionBeanRemote.retrieveOutletByOutletId(Long.parseLong(input));
        } catch (OutletNotFoundException ex) {
            System.out.println("outlet not found!");
        }
        car.setOutletEntity(outlet);

        Set<ConstraintViolation<CarEntity>> constraintViolations = validator.validate(carEntity);

        if (constraintViolations.isEmpty()) {
            try {
                carEntitySessionBeanRemote.updateCar(car);
                System.out.println("Product updated successfully!\n");
            } catch (CarNotFoundException | UpdateCarException ex) {
                System.out.println("An error has occurred while updating car: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCarEntity(constraintViolations);
        }
    }*/

    private void doAllocateCars() {
        carAllocationSessionBeanRemote.carAllocationCheckTimer();
    }
}
