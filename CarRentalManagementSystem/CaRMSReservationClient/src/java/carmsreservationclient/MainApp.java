package carmsreservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import entity.CarEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.NoResultFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RegistrationFailureException;
import util.exception.RentalRateNotFoundException;
import util.exception.ReservationAlreadyCancelledException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.UnsuccessfulReservationException;

public class MainApp {

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private CustomerEntity currentCustomerEntity;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;

    public MainApp() {
    }

    public MainApp(ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote,
            OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote,
            CarEntitySessionBeanRemote carEntitySessionBeanRemote, CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote,
            RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote,ModelEntitySessionBeanRemote modelEntitySessionBeanRemote) {

        this();

        this.reservationRecordEntitySessionBeanRemote = reservationRecordEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;

    }

    public void runApp() {

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {

            System.out.println("*** Welcome to Merlion Car Rental System(v1.0) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Exit\n");

            response = sc.nextInt();

            if (response == 1) {
                try {
                    doLogin();
                    System.out.println("Login successful!\n");

                    menuMain();

                } catch (InvalidLoginCredentialException ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (response == 2) {

                try {
                    doRegister();
                    System.out.println("You have successfully registered.");
                } catch (RegistrationFailureException ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (response == 3) {
                break;
            } else {
                System.out.println("Invalid login credential. Please try again.\n");
            }
        }

    }

    public void doRegister() throws RegistrationFailureException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        String email = "";
        String mobileNumber = "";

        System.out.println("*** Merlion System :: Register ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        System.out.print("Enter email> ");
        email = sc.nextLine().trim();
        System.out.print("Enter mobileNumber> ");
        mobileNumber = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0 && email.length() > 0 && mobileNumber.length() > 0) {
            customerEntitySessionBeanRemote.register(username, password, email, mobileNumber);
        } else {
            throw new RegistrationFailureException("Missing login caredential!");
        }

    }

    public void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Merlion System :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomerEntity = customerEntitySessionBeanRemote.login(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login caredential!");
        }

    }

    public void menuMain() {

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Car Rental System (v1.0) ***\n");
            System.out.println("You are login as " + currentCustomerEntity.getUsername() + "\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View all my reservation");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {

                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                      doSearchCar();
                } else if (response == 2) {
                    //  doReserveCar();
                } else if (response == 3) {
//                    doViewReservationDetails();
                } else if (response == 4) {
//                    doViewAllReservation();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
    }

    /*
            //pre-condition:
            out of X available car, at least one free car
            
            //post-condition
            car A is marked as reservaed and tagged to a reservation record
            pre-condition violated  - throw insufficentavailablecarexception
            
            //invariant condition
            number of reservation for a particular caregory or model cannot exceed the capacity of the rental company
     */

    private void doSearchCar() {
        Scanner sc = new Scanner(System.in);
        String confirmReservation;

        do {

            confirmReservation = "";
            try {
                printAllCategory();
                System.out.print("Please select a category>");
                long selectedCategoryId = sc.nextLong();
                System.out.println("");

                printAvailableModel(selectedCategoryId);
                System.out.print("Do you want to skip selecting a specific car model?(Y/N)>");
                String option = sc.nextLine().trim();
                long selectedModelId;
                if (option.equals("Y")) {
                    selectedModelId = -1;
                } else {
                    System.out.print("Please select a category>");
                    selectedModelId = sc.nextLong();
                }

                System.out.print("Enter pick up date/time>");
                String pickupDateTimeString = sc.nextLine().trim();
                LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDateTimeString);
                printAvailableOutlet(pickupDateTime);

                printAvailableOutlet(pickupDateTime);
                System.out.print("Please select a pickup outlet>");
                long selectedPickupOutletId = sc.nextLong();
                sc.nextLine();

                System.out.print("Enter return date/time>");
                String returnDateTimeString = sc.nextLine().trim();
                LocalDateTime returnDateTime = LocalDateTime.parse(returnDateTimeString);
                printAvailableOutlet(returnDateTime);

                System.out.print("Please select a return outlet>");
                long selectedReturnOutletId = sc.nextLong();
                sc.nextLine();

                
                double totalRentalRate = rentalRateEntitySessionBeanRemote.checkForExistenceOfRentalRate(selectedCategoryId, pickupDateTime, returnDateTime);
                CarEntity car = searchCar(selectedCategoryId, selectedModelId, pickupDateTime,
                        returnDateTime, selectedPickupOutletId, selectedReturnOutletId);
                System.out.print("Found a car!Do you want to reserve it?(Y/N)");
                confirmReservation = sc.nextLine().trim();
                if (confirmReservation.equals("Y")) {
                    doReserveCar(car,totalRentalRate,selectedModelId,selectedCategoryId,
                            pickupDateTime,returnDateTime,selectedPickupOutletId,selectedReturnOutletId);
                }

            } catch (CategoryNotFoundException ex1) {
                System.out.println(ex1.getMessage());
            } catch (RentalRateNotFoundException ex2) {
                System.out.println("Rental Rate is unavailable for the specified period!");
            } catch (NoResultFoundException ex3) {
                System.out.println(ex3.getMessage());
            } catch (UnsuccessfulReservationException ex4){
                System.out.println(ex4.getMessage());
            }

        } while (confirmReservation.equals("Y"));
    }

    public CarEntity searchCar(Long selectedCategoryId, Long selectedModelId, LocalDateTime pickupDateTime,
            LocalDateTime returnDateTime, Long selectedPickupOutletId, Long selectedReturnOutletId) throws NoResultFoundException, CategoryNotFoundException {

        //filter to get cars with specified category and model
        List<CarEntity> availableCars = carEntitySessionBeanRemote.retrieveAvailableCars(pickupDateTime, returnDateTime, selectedPickupOutletId, selectedReturnOutletId);
        availableCars = carEntitySessionBeanRemote.filterCarsBasedOnCategoryId(availableCars, selectedCategoryId);
        availableCars = carEntitySessionBeanRemote.filterCarsBasedOnModelId(availableCars, selectedModelId);

        if (availableCars.isEmpty()) {
            throw new NoResultFoundException("No result found!");
        } else {
            return availableCars.get(0);
        }

    }

    private void printAllCategory() {

        System.out.println("Categories");
        List<CategoryEntity> categories = categoryEntitySessionBeanRemote.retrieveAllCategory();
        System.out.printf("%10s%20s%n", "Category ID", "Category Name");
        for (CategoryEntity categoryEntity : categories) {
            System.out.printf("%10s%20s%n", categoryEntity.getCategoryId(), categoryEntity.getName());
        }
    }

    private void printAvailableModel(long selectedCategory) throws CategoryNotFoundException {
        try {
            CategoryEntity category = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(selectedCategory);
            System.out.printf("%10s%20s%n", "Model ID", "Model Name");
            //get list of models under the category
           // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
            for (ModelEntity model : category.getModels()) {
                System.out.printf("%10s%20s%n", model.getModelId(), model.getModelName());
            }

        } catch (CategoryNotFoundException ex) {
            throw new CategoryNotFoundException("The selected category does not exist.");
        }

    }

    private void printAvailableOutlet(LocalDateTime pickupDateTime) {
        List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveOutletByPickupDateTime(pickupDateTime);
        System.out.println("According to your selected pick up time, the following outlets are available...");
        System.out.printf("%10s%100s%10s%10s%n", "Outlet ID", "Address", "Open At", "Close At");
        for (OutletEntity outletEntity : outlets) {
            System.out.printf("%10d%100s%10s%10s%n", outletEntity.getOutletId(), outletEntity.getAddress(), outletEntity.getOpeningTime().toString(), outletEntity.getClosingTime().toString());
        }
    }

    private void doReserveCar(CarEntity car,double totalRentalRate, Long selectedModelId, Long selectedCategoryId, 
            LocalDateTime pickupDateTime,LocalDateTime returnDateTime, Long selectedPickupOutletId, Long selectedReturnOutletId) throws UnsuccessfulReservationException{

        try{
            
            ReservationRecordEntity reservationRecordEntity = new ReservationRecordEntity(totalRentalRate, pickupDateTime, returnDateTime);
            reservationRecordEntitySessionBeanRemote.createNewReservationRecord(reservationRecordEntity, selectedModelId, selectedModelId, selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId);
            
        } catch(ReservationCreationException ex){
            throw new UnsuccessfulReservationException("Sorry! Your reservation is unsuccessful.");
        }
        
        
    }
//
//    private void doViewReservationDetails() {
//        Scanner scanner = new Scanner(System.in);
//        Integer response = 0;
//
//        System.out.println("*** :: View Reservation Details ***\n");
//        System.out.print("Enter Reservation ID> ");
//        Long reservationId = scanner.nextLong();
//
//        try {
//            ReservationRecordEntity reservationRecordEntity = reservationRecordEntitySessionBeanRemote.retrieveReservationBylId(reservationId);
//            System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation id", "pickup date/time", "pickup outlet", "return date/time", "return outlet");
//            System.out.printf("%8s%20s%20s%20s%20s\n", reservationRecordEntity.getReservationRecordId(),
//                    reservationRecordEntity.getPickUpDateTime(),
//                    reservationRecordEntity.getPickUpOutlet().getName(),
//                    reservationRecordEntity.getReturnDateTime(),
//                    reservationRecordEntity.getReturnOutlet().getName());
//
//            System.out.println("------------------------");
//            System.out.println("1: Cancel Reservation");
//            System.out.println("2: Back\n");
//            System.out.print("> ");
//            response = scanner.nextInt();
//
//            if (response == 1) {
//                doCancelReservation(reservationRecordEntity);
//            }
//        } catch (ReservationRecordNotFoundException ex) {
//            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
//        }
//    }
//
//    private void doViewAllReservation() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("*** :: View All Rental Rate ***\n");
//
//        List<ReservationRecordEntity> reservationRecordEntities = reservationRecordEntitySessionBeanRemote.retrieveAllReservationRecord();
//        System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation id", "pickup date/time", "pickup outlet", "return date/time", "return outlet");
//
//        for (ReservationRecordEntity reservationRecordEntity : reservationRecordEntities) {
//            System.out.printf("%8s%20s%20s%20s%20s\n", reservationRecordEntity.getReservationRecordId(),
//                    reservationRecordEntity.getPickUpDateTime(),
//                    reservationRecordEntity.getPickUpOutlet().getName(),
//                    reservationRecordEntity.getReturnDateTime(),
//                    reservationRecordEntity.getReturnOutlet().getName());
//        }
//        System.out.print("Press any key to continue...> ");
//        scanner.nextLine();
//    }
//
//    private void doCancelReservation(ReservationRecordEntity reservationRecordEntity) {
//
//        Scanner scanner = new Scanner(System.in);
//        String input;
//
//        System.out.println("*** :: View Rental Rate Details :: Cancel Reservation ***\n");
//        System.out.printf("Confirm Cancel Reservation %s ID  (Enter 'Y' to Delete)> ", reservationRecordEntity.getReservationRecordId());
//        input = scanner.nextLine().trim();
//
//        if (input.equals("Y")) {
//            try {
//                reservationRecordEntitySessionBeanRemote.cancelReservation(reservationRecordEntity.getReservationRecordId());
//                if (reservationRecordEntity.getRefund() < 0) {
//                    System.out.println(-1 * (reservationRecordEntity.getRefund()) + " is being charged to your credit card for cancellation penalty!");
//                } else {
//                    System.out.println("Your reservation is being cancelled with the refund of " + reservationRecordEntity.getRefund() + " to your credit card!");
//                }
//                System.out.println("Reservation cancelled successfully!\n");
//
//            } catch (ReservationAlreadyCancelledException ex) {
//                System.out.println(ex.getMessage());
//            }
//        } else {
//            System.out.println("Reservation NOT cancelled!\n");
//        }
//    }
}
