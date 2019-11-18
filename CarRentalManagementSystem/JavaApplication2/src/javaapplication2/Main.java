/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.CategoryEntity;
import ws.client.CategoryNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ModelEntity;
import ws.client.ModelNotFoundException_Exception;
import ws.client.OutletEntity;
import ws.client.PartnerEntity;
import ws.client.RentalRateNotFoundException_Exception;
import ws.client.ReservationCreationException_Exception;
import ws.client.ReservationRecordEntity;

/**
 *
 * @author CHEN BINGSEN
 */
public class Main {

    private static PartnerEntity currentPartner;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static GregorianCalendar gCalendar = new GregorianCalendar();
    
    
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
                    doSearchCar();
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
    
    
    private static void doSearchCar() {
        Scanner sc = new Scanner(System.in);
        String confirmReservation;
        String continueConfirmation;
        String option;

        do {

            confirmReservation = "";
            continueConfirmation = "";
            try {

                printAllCategory();
                System.out.print("Do you want to choose a specific category? (Y/any key for No)>");
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
                    selectedCategoryId = retrieveCategoryIdByModelId(selectedModelId);
                } else {
                    System.out.println("Do you want to choose a specific model? >");
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
                
                Date pickupDateTimeInDateFormat = Date.from(pickupDateTime.atZone(ZoneId.systemDefault()).toInstant());
                printAvailableOutlet(pickupDateTimeInDateFormat);
               
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
                
                Date returnDateTimeInDateFormat = Date.from(pickupDateTime.atZone(ZoneId.systemDefault()).toInstant());
                printAvailableOutlet(returnDateTimeInDateFormat);
                
                System.out.print("Please select a pickup outlet>");
                long selectedReturnOutletId = sc.nextLong();
                sc.nextLine();

                //first check availability of rental rate
                gCalendar.setTime(pickupDateTimeInDateFormat);
                XMLGregorianCalendar xmlPickupDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
                gCalendar.setTime(returnDateTimeInDateFormat);
                XMLGregorianCalendar xmlReturnDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);

                double totalRentalRate = checkForExistenceOfRentalRate(selectedCategoryId, xmlPickupDateTime, xmlReturnDateTime);

                //second check availability of cars
                Boolean canReserve = checkCarAvailability(xmlPickupDateTime,xmlReturnDateTime, selectedPickupOutletId, selectedReturnOutletId,selectedCategoryId, selectedModelId);

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

                        Long reservationId = createNewReservationRecord(totalRentalRate, selectedModelId, selectedCategoryId,
                                xmlPickupDateTime, xmlReturnDateTime, selectedPickupOutletId, selectedReturnOutletId, ccNumber, paidAmt,1l);

                        System.out.println("Your reservation with the ID of " + reservationId + " is successful!");

                    }
                } else {
                    System.out.println("******SORRY! There is currently no available cars for the specified period.*****");
                }

                System.out.print("Do you want to try with another period?(Y/N)>");
                continueConfirmation = sc.nextLine().trim();

            } 
            catch (CategoryNotFoundException_Exception ex1) {
                System.out.println(ex1.getMessage());
            }
            catch (ReservationCreationException_Exception ex4) {
                System.out.println(ex4.getMessage());
            } 
            catch (ModelNotFoundException_Exception ex5) {
                System.out.println(ex5.getMessage());
            } 
            catch (RentalRateNotFoundException_Exception ex6){
                System.out.println("Rental Rate is unavailable for the specified period!");
            }
            catch (DatatypeConfigurationException ex){
                System.out.println("Unknown error.");
            }

        } while (continueConfirmation.equals("Y"));
    }
    
    
    
    
    
    
    public static void printAllCategory(){
        System.out.println("Categories");
        List<CategoryEntity> categories = retrieveAllCategory();
        System.out.printf("%10s%20s%n", "Category ID", "Category Name");
        for (CategoryEntity categoryEntity : categories) {
            System.out.printf("%10s%20s%n", categoryEntity.getCategoryId(), categoryEntity.getName());
        }
    }
    
    public static void printAvailableModel(long selectedCategoryId){

        if (selectedCategoryId == -1) {
            List<ModelEntity> models = retrieveAllModel();
            System.out.printf("%10s%20s%20s%n", "Make and Model ID", "Make","Model");
            //get list of models under the category
            // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
            for (ModelEntity model : models) {
                System.out.printf("%10s%20s%20s%n", model.getModelId(), model.getMake(),model.getModelName());
            }
        } else {
            try {
                CategoryEntity category = retrieveCategoryByCategoryId(selectedCategoryId);
                System.out.printf("%10s%20s%20s%n", "Make and Model ID","Make", "Model");
                for(ModelEntity model : category.getModels()) {
                    System.out.printf("%10s%20s%20s%n", model.getModelId(), model.getMake(),model.getModelName());
                }

            } catch (CategoryNotFoundException_Exception ex) {
                System.out.println("Category not found.");
            }
        }

    }
    
    
    public static void printAvailableOutlet(Date pickupDateTime) {
        
        List<OutletEntity> outlets = new ArrayList<>();
        try{
            gCalendar.setTime(pickupDateTime);
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            outlets = retrieveOutletByPickupDateTime(xmlDate);
        } catch (DatatypeConfigurationException ex){
            System.out.println("Unknown error.");
        }
        
        
        if (outlets.isEmpty()) {
            System.out.println("No outlet is opening at this hour!");
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
    
    private static PartnerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.login(arg0, arg1);
    }

    private static java.util.List<ws.client.CategoryEntity> retrieveAllCategory() {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveAllCategory();
    }

    private static java.util.List<ws.client.ModelEntity> retrieveAllModel() {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveAllModel();
    }

    private static CategoryEntity retrieveCategoryByCategoryId(java.lang.Long arg0) throws CategoryNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveCategoryByCategoryId(arg0);
    }

    private static ModelEntity retrieveModelByModelId(java.lang.Long arg0) throws ModelNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveModelByModelId(arg0);
    }

    private static Long retrieveCategoryIdByModelId(java.lang.Long arg0) throws ModelNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveCategoryIdByModelId(arg0);
    }

    private static java.util.List<ws.client.OutletEntity> retrieveOutletByPickupDateTime(javax.xml.datatype.XMLGregorianCalendar arg0) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveOutletByPickupDateTime(arg0);
    }

    private static double checkForExistenceOfRentalRate(java.lang.Long arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2) throws RentalRateNotFoundException_Exception, CategoryNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.checkForExistenceOfRentalRate(arg0, arg1, arg2);
    }

    private static Boolean checkCarAvailability(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1, java.lang.Long arg2, java.lang.Long arg3, java.lang.Long arg4, java.lang.Long arg5) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.checkCarAvailability(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    private static Long createNewReservationRecord(double arg0, java.lang.Long arg1, java.lang.Long arg2, javax.xml.datatype.XMLGregorianCalendar arg3, javax.xml.datatype.XMLGregorianCalendar arg4, java.lang.Long arg5, java.lang.Long arg6, java.lang.String arg7, double arg8, java.lang.Long arg9) throws ReservationCreationException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.createNewReservationRecord(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    

    

    
    
}
