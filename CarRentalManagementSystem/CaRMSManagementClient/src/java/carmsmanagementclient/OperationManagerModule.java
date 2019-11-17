/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarAllocationSessionBeanRemote;
import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordEntitySessionBeanRemote;
import entity.CarEntity;
import entity.CategoryEntity;
import entity.EmployeeEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;
import util.enumeration.DispatchRecordEnum;
import util.exception.AssignTDDRFailureException;
import util.exception.CarNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewModelFailureException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
import util.exception.OutletNotFoundException;
import util.exception.UpdateCarFailureException;
import util.exception.UpdateDispatchRecordFailureException;
import util.exception.UpdateModelFailureException;

/**
 *
 * @author admin
 */
class OperationManagerModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private EmployeeEntity currentEmployee;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private CarAllocationSessionBeanRemote carAllocationSessionBeanRemote;
    private TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;

    public OperationManagerModule() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationManagerModule(EmployeeEntity currentEmployee, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote,
            CarAllocationSessionBeanRemote carAllocationSessionBeanRemote, TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote,
            EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote,ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.carAllocationSessionBeanRemote = carAllocationSessionBeanRemote;
        this.transitDriverDispatchRecordEntitySessionBeanRemote = transitDriverDispatchRecordEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
        this.reservationRecordEntitySessionBeanRemote = reservationRecordEntitySessionBeanRemote;
    }

    public void menuOperationManagerModule() throws InvalidAccessRightException {

        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.OPERATIONSMANAGER) {
            throw new InvalidAccessRightException("You don't have EMPLOYEE rights to access the sales manager module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Car Rental Management System :: Operations manager module ***\n");
            System.out.println("1: Create New Make And Model");
            System.out.println("2: View All Make And Models");
            System.out.println("3: Update Make and Models");
            System.out.println("4: Delete Make and Models");
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

    private void doViewTDDR() {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter date(yyyy-MM-dd HH:mm:ss))>");
        String currDateTimeString = sc.nextLine().trim();
        LocalDate currDate = LocalDateTime.parse(currDateTimeString, formatter).toLocalDate();
        List<ReservationRecordEntity> reservations = reservationRecordEntitySessionBeanRemote.retrieveReservationRecordByDate(currDate);
        
        List<ReservationRecordEntity> reservationsAtCurrentOutlet = new ArrayList<>();
        for (ReservationRecordEntity reservation:reservations){
            if(reservation.getPickUpOutlet().getOutletId().equals(currentEmployee.getOutletEntity().getOutletId())){
                reservationsAtCurrentOutlet.add(reservation);
            }
        }
        
        List<TransitDriverDispatchRecordEntity> dispatchRecords = new ArrayList<>();
        for(ReservationRecordEntity reservation:reservationsAtCurrentOutlet){
            if(reservation.getTddr()!=null){
                dispatchRecords.add(reservation.getTddr());
            }
        }
        
        System.out.printf("%8s%20s%20s%20s\n", "Id", "Status", "Reservation Id", "Assigned Driver Id");
        String assignedDriverIdString;
        for(TransitDriverDispatchRecordEntity record:dispatchRecords){
            
            if(record.getEmployee() == null){
                assignedDriverIdString = "not assigned yet";
            } else {
                assignedDriverIdString = ""+record.getEmployee().getEmployeeId();
            }
            
            System.out.printf("%8s%20s%20s%20s\n",record.getDispatchRecordId(),record.getDispatchRecordStatus(),
                    record.getReservationRecord().getReservationRecordId(),assignedDriverIdString);
        }


        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private void doAssignTD() {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Dispatch Record ID>");
        Long dispatchRecordId = sc.nextLong();
        sc.nextLine();
        
        List<EmployeeEntity> allEmployees = employeeEntitySessionBeanRemote.retrieveAllEmployee();
        System.out.printf("%8s%20s%20s\n", "employee id", "employee username", "employee role");
        for(EmployeeEntity e:allEmployees){
            if(e.getOutletEntity().getOutletId().equals(currentEmployee.getOutletEntity().getOutletId())){
                System.out.printf("%8s%20s%20s\n", e.getEmployeeId(), e.getUsername(), e.getRole());
            }
        }
        System.out.println("Please assign a driver to this reservation");
        System.out.print("Enter the employee ID>");
        Long employeeId = sc.nextLong();
        sc.nextLine();
        
        try{
            employeeEntitySessionBeanRemote.assignEmployeeToTDDR(employeeId, dispatchRecordId);
            System.out.println("Employee with ID of " + employeeId + " is successfully assigned to the dispatch record " + dispatchRecordId);
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        
        } catch (AssignTDDRFailureException ex){
            System.out.println(ex.getMessage());
        }

    }
    
    private void doUpdateTransitAsComplete(){
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the dispatch record id>");
        Long dispatchId = sc.nextLong();
        sc.nextLine();
        
        try{
            transitDriverDispatchRecordEntitySessionBeanRemote.updateDispatchRecordStatusAsCompleted(dispatchId);
        } catch (UpdateDispatchRecordFailureException ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    
//    private void doAssignTD() {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter the Dispatch Record ID>");
//        TransitDriverDispatchRecordEntity dispatchRecord = transitDriverDispatchRecordEntitySessionBeanRemote.retrieveDispatchRecordById(sc.nextLong());
//        sc.nextLine();
////if the dispatch record driver is not null, throw exception
//        OutletEntity outlet = dispatchRecord.getOutlet();
//        List<EmployeeEntity> employees = outletEntitySessionBeanRemote.retrieveAllEmployee(outlet);
//        System.out.printf("%8s%20s%20s\n", "employee id", "employee username", "employee role");
//        for (EmployeeEntity e : employees) {
//            System.out.printf("%8s%20s%20s\n", e.getEmployeeId(), e.getUsername(), e.getRole());
//        }
//        System.out.println("Please assign a driver to this reservation");
//        System.out.println("Enter the employee ID>");
//        EmployeeEntity e = employeeEntitySessionBeanRemote.retrieveEmployeeByEmployeeId(sc.nextLong());
//        sc.nextLine();
//        dispatchRecord.setEmployee(e);
//        dispatchRecord.setDispatchRecordEnum(DispatchRecordEnum.ASSIGNED);
//        e.getDispatchRecord().add(dispatchRecord);
//        System.out.println("Employee with ID of " + e.getEmployeeId() + "is successfully assigned to the reservation");
//    }

    
    
//    private void doUpdateTransitAsComplete() {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter the dispatch record id>");
//        TransitDriverDispatchRecordEntity dispatchRecord = transitDriverDispatchRecordEntitySessionBeanRemote.retrieveDispatchRecordById(sc.nextLong());
//        sc.nextLine();
//        dispatchRecord.setDispatchRecordStatus(DispatchRecordEnum.COMPLETE);
//    }

    private void showInputDataValidationErrorsForCarEntity(Set<ConstraintViolation<CarEntity>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doCreateNewModel() {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter new make name>");
            String makeName = sc.nextLine();
            System.out.print("Enter new model name>");
            String modelName = sc.nextLine();
            printAllCategory();

            System.out.print("Enter category ID>");
            Long categoryId = sc.nextLong();
            categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(categoryId);
            sc.nextLine();
            ModelEntity newModelEntity = new ModelEntity(makeName, modelName);
            modelEntitySessionBeanRemote.createNewModel(newModelEntity, categoryId);
            System.out.println("New model " + modelName + " successfully created!");
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
        } catch (CreateNewModelFailureException ex) {
            System.out.println(ex.getMessage());
        } catch (CategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doUpdateModel() {

        Scanner sc = new Scanner(System.in);
        String input;

        System.out.print("Enter model ID>");
        Long modelId = sc.nextLong();
        sc.nextLine();

        try {
            ModelEntity model = modelEntitySessionBeanRemote.retrieveModelByModelId(modelId);
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** ::Update Model ***\n");
            System.out.print("Enter Make Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                model.setMake(input);
            }
            System.out.print("Enter Model Name (blank if no changes>) ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                model.setModelName(input);
            }
            System.out.print("Enter Category Id (blank if no changes>) ");
            printAllCategory();
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                CategoryEntity category = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(input));
                model.setCategoryEntity(category);
            }
            Set<ConstraintViolation<ModelEntity>> constraintViolations = validator.validate(model);

            if (constraintViolations.isEmpty()) {
                try {
                    modelEntitySessionBeanRemote.updateModel(model);
                    System.out.println("Model updated successfully!\n");

                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                } catch (UpdateModelFailureException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else {
                showInputDataValidationErrorsForModelEntity(constraintViolations);
            }

        } catch (ModelNotFoundException ex) {
            System.out.println(ex.getMessage());

        } catch (CategoryNotFoundException ex) {
            System.out.println("No category found");
        }

    }

    private void doViewAllModels() {
        Scanner sc = new Scanner(System.in);
        List<ModelEntity> models = modelEntitySessionBeanRemote.retrieveAllModel();
        System.out.printf("%8s%20s%20s%20s", "Model ID", "Make", "Model", "Category");
        for (ModelEntity modelEntity : models) {
            System.out.printf("%8s%20s%20s%20s", modelEntity.getModelId(), modelEntity.getMake(), modelEntity.getModelName(), modelEntity.getCategoryEntity().getName());
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

    private void doCreateNewCar() {

        try {

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
            outletEntitySessionBeanRemote.retrieveOutletByOutletId(outletId);
            sc.nextLine();
            /////
            boolean done = false;
            do {
                System.out.print("Enter status (Available/Repair)>");
                CarStatusEnum status;
                if (sc.nextLine().trim().equals("Available")) {
                    status = CarStatusEnum.AVAILABLE;
                    newCarEntity.setStatus(status);
                    done = true;
                } else if (sc.nextLine().trim().equals("Repair")) {
                    status = CarStatusEnum.REPAIR;
                    newCarEntity.setStatus(status);
                    done = true;
                } else {
                    System.out.print("Invalid input, press enter>>>>>>");
                    done = false;
                }

            } while (!done);

            printAllModels();
            System.out.print("Enter make and model id>");
            String id = sc.nextLine().trim();
            ModelEntity modelEntity = modelEntitySessionBeanRemote.retrieveModelByModelId(Long.valueOf(id));
            newCarEntity.setMake(modelEntity.getMake());
            newCarEntity.setModel(modelEntity.getModelName());
            String make = modelEntity.getMake();
            String model = modelEntity.getModelName();

            try {
                Long carId = carEntitySessionBeanRemote.createNewCar(newCarEntity, make, model, outletId);
                System.out.println("New Car " + carId + "has been successfully created.");

            } catch (NewCarCreationException ex) {
                System.out.println(ex.getMessage());
            } catch (ModelNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (ModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (OutletNotFoundException ex) {
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
            sc.nextLine();

            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                doUpdateCar(car);
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

    private void doUpdateCar(CarEntity car) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** :: View Car Details :: Update Car ***\n");

        try {
            System.out.print("Enter Plate Number (blank if no change)> ");
            input = sc.nextLine().trim();
            if (input.length() > 0) {
                car.setPlateNumber(input);
            }

            System.out.print("Enter Car status (AVAILABLE/REPAIR) (blank if no change)> ");
            input = sc.nextLine().trim();
            if (input.length() > 0) {
                car.setStatus(CarStatusEnum.valueOf(input));
            }

            System.out.println("Enter Model Id(blank if no change)>");
            printAllModels();
            input = sc.nextLine().trim();
            if (input.length() > 0) {
                ModelEntity modelEntity = modelEntitySessionBeanRemote.retrieveModelByModelId(Long.valueOf(input));
                car.setMake(modelEntity.getMake());
                car.setModel(modelEntity.getModelName());
                car.setModelEntity(modelEntity);
            }
            System.out.println("Enter Outlet Id(blank if no change)>");
            printAllOutlets();
            input = sc.nextLine().trim();
            if (input.length() > 0) {
                OutletEntity outletEntity = outletEntitySessionBeanRemote.retrieveOutletByOutletId(Long.valueOf(input));
                car.setOutletEntity(outletEntity);
            }

            Set<ConstraintViolation<CarEntity>> constraintViolations = validator.validate(car);

            if (constraintViolations.isEmpty()) {
                try {
                    carEntitySessionBeanRemote.updateCar(car);
                    System.out.println("Car updated successfully!\n");

                } catch (InputDataValidationException ex) {
                    System.out.println(ex.getMessage() + "\n");
                } catch (UpdateCarFailureException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else {
                showInputDataValidationErrorsForCarEntity(constraintViolations);
            }
            

        } catch (OutletNotFoundException ex) {
            System.out.println("outlets not found!");
        } catch (ModelNotFoundException ex) {
            System.out.println("models not found!");
        }
    }

    private void doAllocateCars() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a date to trigger the alloation of car(yyyy-MM-dd)>");
        String dateTimeString = sc.nextLine().trim();
        dateTimeString += " 02:00:00";
        LocalDateTime triggerDateTime = LocalDateTime.parse(dateTimeString, formatter);
        carAllocationSessionBeanRemote.carAllocationTimer(triggerDateTime);
    }

    private void printAllCategory() {

        System.out.println("List of Categories:");
        List<CategoryEntity> categories = categoryEntitySessionBeanRemote.retrieveAllCategory();
        System.out.printf("%10s%20s%n", "Category ID", "Category Name");
        for (CategoryEntity categoryEntity : categories) {
            System.out.printf("%10s%20s%n", categoryEntity.getCategoryId(), categoryEntity.getName());
        }
    }

    private void showInputDataValidationErrorsForModelEntity(Set<ConstraintViolation<ModelEntity>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void printAllModels() {
        System.out.println("List of Existing Models:");
        List<ModelEntity> models = modelEntitySessionBeanRemote.retrieveAllModel();
        System.out.printf("%10s%20s%20s%n", "Model ID", "Make", "Model");
        for (ModelEntity m : models) {
            System.out.printf("%10s%20s%20s%n", m.getModelId(), m.getMake(), m.getModelName());
        }
    }

    private void printAllOutlets() {
        System.out.println("List of Outlets:");
        List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveAllOutlet();
        System.out.printf("%10s%20s%n", "Outlet ID", "Outlet Name");
        for (OutletEntity o : outlets) {
            System.out.printf("%10s%20s%n", o.getOutletId(), o.getName());
        }
    }
}
