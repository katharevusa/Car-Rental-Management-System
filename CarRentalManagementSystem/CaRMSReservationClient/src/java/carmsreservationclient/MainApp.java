package carmsreservationclient;

import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import entity.CustomerEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegistrationFailureException;

public class MainApp {

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private CustomerEntity currentCustomerEntity;
    private CarReservationModule carReservationModule;
    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
    
    
    public MainApp() {
    }
    
    
    public MainApp(ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote,CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote,CarEntitySessionBeanRemote carEntitySessionBeanRemote) {
    
        this();
        
        this.reservationRecordEntitySessionBeanRemote = reservationRecordEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        
    }
    
    public void runApp(){
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            
            System.out.println("*** Welcome to Merlion Car Rental System(v1.0) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Exit\n");
            
            
            response = sc.nextInt();
            
            if (response == 1){
                try{
                    doLogin();
                    System.out.println("Login successful!\n");
                    
                    carReservationModule = new CarReservationModule(reservationRecordEntitySessionBeanRemote,outletEntitySessionBeanRemote,carEntitySessionBeanRemote);
                    menuMain();
                    
                } catch(InvalidLoginCredentialException ex){
                    System.out.println(ex.getMessage());
                }
              
            } else if (response == 2){
                
                try{
                    doRegister();
                    System.out.println("You have successfully registered.");
                } catch(RegistrationFailureException ex) {
                    System.out.println(ex.getMessage());
                }
                
            }else if (response == 3){
                break;
            } else {
                System.out.println("Invalid login credential. Please try again.\n");
            }
        }
        
    
    }
    
    public void doRegister() throws RegistrationFailureException{
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
            customerEntitySessionBeanRemote.register(username, password,email,mobileNumber);
        } else {
            throw new RegistrationFailureException("Missing login caredential!");
        }
        
    }
    
    public void doLogin() throws InvalidLoginCredentialException{
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
        
        while(true){
            System.out.println("*** Merlion Car Rental System (v1.0) ***\n");
            System.out.println("You are login as " + currentCustomerEntity.getUsername() + "\n");
            System.out.println("1: Car Reservation");
            System.out.println("2: Logout\n");
            response = 0;

            while(response < 1 || response > 2) {

                System.out.print("> ");
                response = sc.nextInt();
                
                if (response == 1) {

                    carReservationModule.menuCarReservation();

                } else if (response == 2){
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 2){
                break;
            }
        }
    }

    
}
