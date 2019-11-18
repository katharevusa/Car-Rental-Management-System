/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.Scanner;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.PartnerEntity;

/**
 *
 * @author CHEN BINGSEN
 */
public class Main {

    private static PartnerEntity currentPartner;
    
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {

            System.out.println("*** Welcome to Merlion Car Rental System(v1.0) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");

            response = sc.nextInt();

            if (response == 1) {
                try {
                    doLogin();
                    System.out.println("Login successful!\n");
                    menuMain();
                } catch (InvalidLoginCredentialException_Exception ex) {
                    System.out.println(ex.getMessage());
                }

            } else if (response == 2) {
                break;
            } else {
                System.out.println("Invalid login credential. Please try again.\n");
            }
        }

    }
    
    
    public static void doLogin() throws InvalidLoginCredentialException_Exception {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Merlion System :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentPartner = login(username, password);
        } else {
            throw new InvalidLoginCredentialException_Exception("Missing login caredential!");
        }
    }

    
    public static void menuMain() {

        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Merlion Car Rental System (v1.0) ***\n");
            System.out.println("You are login as " + currentPartner.getUsername());
//            System.out.println("You are doing the reservation for " + currentCustomer.getUsername() + "\n");
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
//                    doSearchCar();
                } else if (response == 2) {
//                    doViewReservationDetails();
                } else if (response == 3) {
//                    doViewAllReservation();
                } else if (response == 4) {
//                    doViewReservationDetails();
                } else if (response == 5) {
                    currentPartner = null;
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
    
    
    
    
    
    private static PartnerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.login(arg0, arg1);
    }

    

    
    
}
