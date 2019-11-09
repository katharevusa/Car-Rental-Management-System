package carmsreservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import entity.CustomerEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegistrationFailureException;
import util.exception.ReservationRecordNotFoundException;

public class MainApp {

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private CustomerEntity currentCustomerEntity;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    private SimpleDateFormat df;

    public MainApp() {
    }

    public MainApp(ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote) {

        this();

        this.reservationRecordEntitySessionBeanRemote = reservationRecordEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        df = new SimpleDateFormat("HH:mm:ss");

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
                    doReserveCar();
                } else if (response == 3) {
                    doViewReservationDetails();
                } else if (response == 4) {
                    doViewAllReservation();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doSearchCar() {
        Scanner sc = new Scanner(System.in);
        String pickupDateTime = "";
        String pickupOutlet = "";
        String returnDateTime = "";
        String returnOutlet = "";
        String confirmReservation = "";

        do {

            System.out.println("The available outlets are:");
            int idxNumber = 1;

            List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveAllOutlet();
            for (OutletEntity outletEntity : outlets) {
                System.out.println("" + idxNumber + ". " + "Outlet " + outletEntity.getOutletId());
                System.out.println("Address: " + outletEntity.getAddress());
                System.out.println("Open from " + df.format(outletEntity.getOpeningTime()) + " to " + df.format(outletEntity.getClosingTime()));
                idxNumber++;
            }

            System.out.println("Please select a pickup outlet: ");
            int selectedPickupOutlet = sc.nextInt();
            System.out.println("Please select a return outlet: ");
            int selectedReturnOutlet = sc.nextInt();

            if (selectedPickupOutlet < 1 || selectedPickupOutlet > outlets.size()) {
                break;
            } else if (selectedReturnOutlet < 1 || selectedReturnOutlet > outlets.size()) {
                break;
            }

            try {
                System.out.print("Please specify the Date you need a car(YYYY-MM-DD):");
                Date pickupDate = df.parse(sc.next().trim());
                System.out.println("");
                System.out.print("Please specify the time you want to collect the car(HH:MM:SS):");
                Date pickupTime = df.parse(sc.next().trim());
                System.out.print("Please specify the Date you return the car(YYYY-MM-DD):");
                Date returnDate = df.parse(sc.next().trim());
                System.out.println("");
                System.out.print("Please specify the time you can return the car(HH:MM:SS):");
                Date returnTime = df.parse(sc.next().trim());

            } catch (ParseException ex) {
                System.out.println("Invalid inputs.");
                break;
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
//            List<ReservationRecordEntity> resevReservationRecords = 
//            
//            List<CarEntity> suitableCars = new ArrayList<>();
//            List<ReservationRecordEntity> reservations;
//            List<CarEntity> cars = carEntitySessionBeanRemote.retrieveAllCars();
//            for (CarEntity car : cars){
//                if (car.getOutletEntity().getOutletId() == outlets.get(selectedPickupOutlet - 1).getOutletId()){
//                    
//                }
//            }
//            System.out.println("The following cars are available :");
//            if (cars.size() == 0){
//                System.out.println("Oops, there is currently no car available for the selected duration");
//                break;
//            } else {
//                for (CarEntity car : cars){
//                    
//                }
//            }
        } while (confirmReservation.equals("Yes"));
    }

    private void doReserveCar() {

    }

    private void doViewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** :: View Reservation Details ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();

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
            response = scanner.nextInt();

            if (response == 1) {
                doCancelReservation(reservationRecordEntity);
            } 
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllReservation() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** :: View All Rental Rate ***\n");

        List<ReservationRecordEntity> reservationRecordEntities = reservationRecordEntitySessionBeanRemote.retrieveAllReservationRecord();
        System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation id", "pickup date/time", "pickup outlet", "return date/time", "return outlet");

        for (ReservationRecordEntity reservationRecordEntity : reservationRecordEntities) {
            System.out.printf("%8s%20s%20s%20s%20s\n", reservationRecordEntity.getReservationRecordId(),
                    reservationRecordEntity.getPickUpDateTime(),
                    reservationRecordEntity.getPickUpOutlet().getName(),
                    reservationRecordEntity.getReturnDateTime(),
                    reservationRecordEntity.getReturnOutlet().getName());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCancelReservation(ReservationRecordEntity reservationRecordEntity) {
        
               
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** :: View Rental Rate Details :: Cancel Reservation ***\n");
        System.out.printf("Confirm Cancel Reservation %s ID  (Enter 'Y' to Delete)> ", reservationRecordEntity.getReservationRecordId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
//            try {
//           //     reservationRecordEntitySessionBeanRemote.cancelReservation(reservationRecordEntity.getReservationRecordId());
//                System.out.println("Reservation cancelled successfully!\n");
//                }
//            } catch (ReservationRecordNotFoundException ex) {
//                System.out.println("An error has occurred while cancelling reservation: " + ex.getMessage() + "\n");
//            }
        } else {
            System.out.println("Reservation NOT cancelled!\n");
        }
    }
}


