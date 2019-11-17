package carmsreservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.CancelReservationFailureException;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.NoOutletIsOpeningException;
import util.exception.NoResultFoundException;
import util.exception.RegistrationFailureException;
import util.exception.RentalRateNotFoundException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.UnsuccessfulReservationException;

public class MainApp {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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
            RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote) {

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
            System.out.println("1: Search Car & Reserve Car");
            System.out.println("2: View Reservation Details");
            System.out.println("3: View all my reservation");
            System.out.println("4: Cancel reservation");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {

                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doViewReservationDetails();
                } else if (response == 3) {
                    doViewAllReservation();
                } else if (response == 4) {
                    doViewReservationDetails();
                } else if (response == 5) {
                    currentCustomerEntity = null;
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

    private void doSearchCar() {
        Scanner sc = new Scanner(System.in);
        String confirmReservation;
        String continueConfirmation;
        String option;

        do {

            confirmReservation = "";
            continueConfirmation = "";
            try {

                printAllCategory();
                System.out.print("Do you want to choose a specific category? (Y/any key)>");
                option = sc.nextLine().trim();

                long selectedCategoryId;
                if (option.equals("Y")) {
                    System.out.print("Please select a Category>");
                    selectedCategoryId = sc.nextLong();
                    sc.nextLine();
                } else {
                    selectedCategoryId = -1;
                }

                long selectedModelId;
                printAvailableModel(selectedCategoryId);
                if (selectedCategoryId == -1) {
                    System.out.print("Models under this category>");
                    selectedModelId = sc.nextLong();
                    sc.nextLine();
                    selectedCategoryId = modelEntitySessionBeanRemote.retrieveModelByModelId(selectedModelId).getCategoryEntity().getCategoryId();
                } else {
                    System.out.println("Do you want to choose a specific model? (Y/any key)>");
                    System.out.println("(Enter Y to choose or");
                    System.out.println("Enter any key to skip this step!)");
                    option = sc.nextLine().trim();

                    if (option.equals("Y")) {
                        System.out.print("Please select a Model>");
                        selectedModelId = sc.nextLong();
                        sc.nextLine();
                    } else {
                        selectedModelId = -1;
                    }
                }

                System.out.print("Enter pick up date/time(dd/MM/yyyy HH:mm:ss)>");

                boolean isCorrectFormat = false;
                LocalDateTime pickupDateTime = LocalDateTime.now();
                while (!isCorrectFormat) {
                    try {
                        String pickupDateTimeString = sc.nextLine().trim();
                        pickupDateTime = LocalDateTime.parse(pickupDateTimeString, formatter);
                        isCorrectFormat = true;
                    } catch (DateTimeParseException ex) {
                        System.out.print("Incorrect date format!");
                        System.out.print("Enter pick up date/time(dd/MM/yyyy HH:mm:ss)>");
                    }
                }
                
                

                try {
                    printAvailableOutlet(pickupDateTime);
                } catch (NoOutletIsOpeningException ex) {
                    System.out.println(ex.getMessage()+"You cannot make this reservation!");
                }
                System.out.print("Please select a pickup outlet>");
                long selectedPickupOutletId = sc.nextLong();
                sc.nextLine();

                System.out.print("Enter return date/time(dd/MM/yyyy HH:mm:ss))>");

                isCorrectFormat = false;
                LocalDateTime returnDateTime = LocalDateTime.now();
                while (!isCorrectFormat) {
                    try {
                        String returnDateTimeString = sc.nextLine().trim();
                        returnDateTime = LocalDateTime.parse(returnDateTimeString, formatter);
                        isCorrectFormat = true;
                    } catch (DateTimeParseException ex) {
                        System.out.print("Incorrect date format!");
                        System.out.print("Enter pick up date/time(dd/MM/yyyy HH:mm:ss)>");
                    }
                }


                try {
                    printAvailableOutlet(returnDateTime);
                } catch (NoOutletIsOpeningException ex) {
                    System.out.println(ex.getMessage()+"You cannot make this reservation!");
                }
                System.out.print("Please select a return outlet>");
                long selectedReturnOutletId = sc.nextLong();
                sc.nextLine();

                //first check availability of rental rate
                double totalRentalRate = rentalRateEntitySessionBeanRemote.checkForExistenceOfRentalRate(selectedCategoryId, pickupDateTime, returnDateTime);
                //second check availability of cars
                
                System.out.println("category ID " + selectedCategoryId + " + " + "model ID" + selectedModelId);
                Boolean canReserve = searchCar(selectedCategoryId, selectedModelId, pickupDateTime,
                        returnDateTime, selectedPickupOutletId, selectedReturnOutletId);

                if (canReserve) {
                    System.out.println("****************FOUND A CAR!*****************>");
                    System.out.print("Do you want to make a reservation on this car? (Y/N)");
                    confirmReservation = sc.nextLine().trim();
                    if (confirmReservation.equals("Y")) {
                        System.out.println("The total rental rate for the current reservation is " + totalRentalRate);
                        System.out.println("Please select the payment option:");
                        System.out.println("1: Pay now with your credit card");
                        System.out.println("2: Pay at the outlet");
                        System.out.print("> ");
                        Long paymentOption = sc.nextLong();
                        sc.nextLine();
                        String ccNumber;
                        double paidAmt;
                        if (paymentOption == 2) {
                            paidAmt = 0;
                            System.out.print("Please provide your creadit card number>");
                            ccNumber = sc.nextLine().trim();
                        } else {
                            System.out.println("Please provide your credit card number");
                            ccNumber = sc.nextLine().trim();
                            paidAmt = totalRentalRate;
                        }

                        Long reservationId = doReserveCar(totalRentalRate, selectedModelId, selectedCategoryId,
                                pickupDateTime, returnDateTime, selectedPickupOutletId, selectedReturnOutletId, ccNumber, paidAmt);

                        System.out.println("Your reservation with the ID of " + reservationId + " is successful!");

                    }
                } else {
                    System.out.println("******SORRY! There is currently no available cars for the specified period.*****");
                }

                System.out.print("Do you want to try with another period?(Y/N)>");
                continueConfirmation = sc.nextLine().trim();

            } catch (CategoryNotFoundException ex1) {
                System.out.println(ex1.getMessage());
            } catch (RentalRateNotFoundException ex2) {
                System.out.println("Rental Rate is unavailable for the specified period!");
            } catch (NoResultFoundException ex3) {
                System.out.println(ex3.getMessage());
            } catch (UnsuccessfulReservationException ex4) {
                System.out.println(ex4.getMessage());
            } catch (ModelNotFoundException ex5) {
                System.out.println(ex5.getMessage());
            }

        } while (continueConfirmation.equals("Y"));
    }

    private Long doReserveCar(double totalRentalRate, Long selectedModelId, Long selectedCategoryId,
            LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Long selectedPickupOutletId,
            Long selectedReturnOutletId, String ccNumber, double paidAmt) throws UnsuccessfulReservationException {

        try {

            ReservationRecordEntity reservationRecordEntity = new ReservationRecordEntity(totalRentalRate, pickupDateTime, returnDateTime, ccNumber, paidAmt);
            //associate reservation record with customer
            //with pickup and return outlet
            //with model
            //with category
            //is it fine if there is no relationship btween rental rate and reservation record?
            Long reservationId = reservationRecordEntitySessionBeanRemote.createNewReservationRecord(reservationRecordEntity,
                    currentCustomerEntity.getCustomerId(), selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId);
            return reservationId;
        } catch (ReservationCreationException ex) {
            throw new UnsuccessfulReservationException("Sorry! Your reservation is unsuccessful.");
        }

    }

    public Boolean searchCar(Long selectedCategoryId, Long selectedModelId, LocalDateTime pickupDateTime,
            LocalDateTime returnDateTime, Long selectedPickupOutletId, Long selectedReturnOutletId) throws NoResultFoundException, CategoryNotFoundException {

        //filter to get cars with specified category and model
        Boolean canReserve = carEntitySessionBeanRemote.checkCarAvailability(pickupDateTime, returnDateTime,
                selectedPickupOutletId, selectedReturnOutletId, selectedCategoryId, selectedModelId);

        return canReserve;

    }

    private void printAllCategory() {

        System.out.println("Categories");
        List<CategoryEntity> categories = categoryEntitySessionBeanRemote.retrieveAllCategory();
        System.out.printf("%10s%20s%n", "Category ID", "Category Name");
        for (CategoryEntity categoryEntity : categories) {
            System.out.printf("%10s%20s%n", categoryEntity.getCategoryId(), categoryEntity.getName());
        }
    }

    private void printAvailableModel(long selectedCategoryId) throws CategoryNotFoundException {

        if (selectedCategoryId == -1) {
            List<ModelEntity> models = modelEntitySessionBeanRemote.retrieveAllModel();
            System.out.printf("%10s%20s%n", "Model ID", "Model Name");
            //get list of models under the category
            // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
            for (ModelEntity model : models) {
                System.out.printf("%10s%20s%n", model.getModelId(), model.getModelName());
            }
        } else {
            try {
                CategoryEntity category = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(selectedCategoryId);
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

    }

    private void printAvailableOutlet(LocalDateTime pickupDateTime) throws NoOutletIsOpeningException{

        List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveOutletByPickupDateTime(pickupDateTime);
        if (outlets.isEmpty()) {
            throw new NoOutletIsOpeningException("No outlet is opening at this hour!");
        } else {
            System.out.printf("%10s%100s%20s%20s%n", "Outlet ID", "Address", "Open At", "Close At");
            for (OutletEntity outlet : outlets) {
                if (outlet.getOpeningTime() == null) {
                    System.out.printf("%10d%100s%20s%20s%n", outlet.getOutletId(), outlet.getAddress(), "(opens 24 hours)", "(opens 24 hours)");
                } else {
                    System.out.printf("%10d%100s%20s%20s%n", outlet.getOutletId(), outlet.getAddress(), outlet.getOpeningTime().toString(), outlet.getClosingTime().toString());
                }
            }
        }
    }

    public void doCancelReservation(Long reservationId) {

        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("*** :: View Rental Rate Details :: Cancel Reservation ***\n");
        System.out.printf("Confirm Cancel Reservation ID %s (Enter 'Y' to Delete)> ", reservationId);
        input = sc.nextLine().trim();
        //if the reservation has already been cancelled, they cannot cancel it again:SOLVED
        //if the reservation is in the past, they cannot be cancelled:
       
        if (input.equals("Y")) {
            try {
                ReservationRecordEntity reservationToCancel = reservationRecordEntitySessionBeanRemote.cancelReservation(reservationId);
                if (reservationToCancel.getRefund() < 0) {
                    System.out.println("Your reservation " + reservationId + " has been cancelled");
                    System.out.println("Your credit card " + reservationToCancel.getCcNumber() + " has been charged for " + reservationToCancel.getRefund() + " for cancellation penalty.");
                } else {
                    System.out.println("Your reservation " + reservationId + " has been cancelled with the refund of " + reservationToCancel.getRefund() + " to your credit card!");
                }

            } catch (CancelReservationFailureException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }

    }

    private void doViewAllReservation() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** :: View All Rental Rate ***\n");

        List<ReservationRecordEntity> reservationRecordEntities = reservationRecordEntitySessionBeanRemote.retrieveAllReservationRecord();
        System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation id", "pickup date/time", "pickup outlet", "return date/time", "return outlet");

        List<ReservationRecordEntity> reservations = new ArrayList<>();
        for (ReservationRecordEntity reservation : reservationRecordEntities) {
            if (reservation.getCustomerEntity().getCustomerId().equals(currentCustomerEntity.getCustomerId())) {
                reservations.add(reservation);
            }
        }

        for (ReservationRecordEntity reservationRecordEntity : reservations) {
            System.out.printf("%8s%20s%20s%20s%20s\n", reservationRecordEntity.getReservationRecordId(),
                    reservationRecordEntity.getPickUpDateTime(),
                    reservationRecordEntity.getPickUpOutlet().getName(),
                    reservationRecordEntity.getReturnDateTime(),
                    reservationRecordEntity.getReturnOutlet().getName());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewReservationDetails() {

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** :: View Reservation Details ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = sc.nextLong();

        try {
            ReservationRecordEntity reservationRecordEntity = reservationRecordEntitySessionBeanRemote.retrieveReservationBylId(reservationId);
            System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation id", "pickup date/time", "pickup outlet", "return date/time", "return outlet");
            System.out.printf("%8s%20s%20s%20s%20s\n", reservationRecordEntity.getReservationRecordId(),
                    reservationRecordEntity.getPickUpDateTime(),
                    reservationRecordEntity.getPickUpOutlet().getName(),
                    reservationRecordEntity.getReturnDateTime(),
                    reservationRecordEntity.getReturnOutlet().getName());

            System.out.println("------------------------");
            System.out.println("1: Cancel Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                doCancelReservation(reservationId);
            }

        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
        }
    }
}
