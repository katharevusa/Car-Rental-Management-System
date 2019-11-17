/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationapplication;

import java.net.NoRouteToHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.CancelReservationFailureException_Exception;
import ws.client.CategoryEntity;
import ws.client.CategoryNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ModelEntity;
import ws.client.ModelNotFoundException_Exception;
import ws.client.OutletEntity;
import ws.client.PartnerEntity;
import ws.client.RentalRateNotFoundException;
import ws.client.RentalRateNotFoundException_Exception;
import ws.client.ReservationCreationException_Exception;
import ws.client.ReservationRecordEntity;
import ws.client.ReservationRecordNotFoundException_Exception;

/**
 *
 * @author admin
 */
class MainApp {

    private PartnerEntity currentPartner;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    void runApp() {
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

                    //menuMain();

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

    public void doLogin() throws InvalidLoginCredentialException_Exception {
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
//
//    public void menuMain() {
//
//        Scanner sc = new Scanner(System.in);
//        Integer response = 0;
//
//        while (true) {
//            System.out.println("*** Merlion Car Rental System (v1.0) ***\n");
//            System.out.println("You are login as " + currentPartner.getUsername() + "\n");
//            System.out.println("1: Search Car & Reserve Car");
//            System.out.println("2: View Reservation Details");
//            System.out.println("3: View all my reservation");
//            System.out.println("4: Cancel reservation");
//            System.out.println("5: Logout\n");
//            response = 0;
//
//            while (response < 1 || response > 5) {
//
//                System.out.print("> ");
//                response = sc.nextInt();
//
//                if (response == 1) {
//                    doSearchCar();
//                } else if (response == 2) {
//                    doViewReservationDetails();
//                } else if (response == 3) {
//                    doViewAllReservation();
//                } else if (response == 4) {
//                    doViewReservationDetails();
//                } else if (response == 5) {
//                    currentPartner = null;
//                    break;
//                } else {
//                    System.out.println("Invalid option, please try again!\n");
//                }
//            }
//
//            if (response == 5) {
//                break;
//            }
//        }
//    }
//
//    private void doSearchCar() {
//        Scanner sc = new Scanner(System.in);
//        String confirmReservation;
//        String continueConfirmation;
//        String option;
//
//        do {
//
//            confirmReservation = "";
//            continueConfirmation = "";
//            try {
//
//                printAllCategory();
//                System.out.print("Do you want to choose a specific category? (Y/any key)>");
//                option = sc.nextLine().trim();
//
//                long selectedCategoryId;
//                if (option.equals("Y")) {
//                    System.out.print("Please select a Category>");
//                    selectedCategoryId = sc.nextLong();
//                    sc.nextLine();
//                } else {
//                    selectedCategoryId = -1;
//                }
//
//                long selectedModelId;
//                printAvailableModel(selectedCategoryId);
//                if (selectedCategoryId == -1) {
//                    System.out.print("Models under this category>");
//                    selectedModelId = sc.nextLong();
//                    sc.nextLine();
//                    selectedCategoryId = retrieveModelByModelId(selectedModelId).getCategoryEntity().getCategoryId();
//                } else {
//                    System.out.println("Do you want to choose a specific model? (Y/any key)>");
//                    System.out.println("(Enter Y to choose or");
//                    System.out.println("Enter any key to skip this step!)");
//                    option = sc.nextLine().trim();
//
//                    if (option.equals("Y")) {
//                        System.out.print("Please select a Model>");
//                        selectedModelId = sc.nextLong();
//                        sc.nextLine();
//                    } else {
//                        selectedModelId = -1;
//                    }
//                }
//
//                System.out.print("Enter pick up date/time(dd/MM/yyyy HH:mm:ss)>");
//
//                boolean isCorrectFormat = false;
//                LocalDateTime pickupDateTime = LocalDateTime.now();
//                while (!isCorrectFormat) {
//                    try {
//                        String pickupDateTimeString = sc.nextLine().trim();
//                        pickupDateTime = LocalDateTime.parse(pickupDateTimeString, formatter);
//                        isCorrectFormat = true;
//                    } catch (DateTimeParseException ex) {
//                        System.out.print("Incorrect date format!");
//                        System.out.print("Enter pick up date/time(dd/MM/yyyy HH:mm:ss)>");
//                    }
//                }
//                printAvailableOutlet(pickupDateTime);
//                System.out.print("Please select a pickup outlet>");
//                long selectedPickupOutletId = sc.nextLong();
//                sc.nextLine();
//
//                System.out.print("Enter return date/time(dd/MM/yyyy HH:mm:ss))>");
//
//                isCorrectFormat = false;
//                LocalDateTime returnDateTime = LocalDateTime.now();
//                while (!isCorrectFormat) {
//                    try {
//                        String returnDateTimeString = sc.nextLine().trim();
//                        returnDateTime = LocalDateTime.parse(returnDateTimeString, formatter);
//                        isCorrectFormat = true;
//                    } catch (DateTimeParseException ex) {
//                        System.out.print("Incorrect date format!");
//                        System.out.print("Enter pick up date/time(dd/MM/yyyy HH:mm:ss)>");
//                    }
//                }
//                printAvailableOutlet(returnDateTime);
//                System.out.print("Please select a return outlet>");
//                long selectedReturnOutletId = sc.nextLong();
//                sc.nextLine();
//
//                String iso = pickupDateTime.toString();
//                String iso2 = returnDateTime.toString();
//                if (pickupDateTime.getSecond() == 0 && pickupDateTime.getNano() == 0) {
//                    iso += ":00"; // necessary hack because the second part is not optional in XML
//                }
//                if (returnDateTime.getSecond() == 0 && returnDateTime.getNano() == 0) {
//                    iso += ":00"; // necessary hack because the second part is not optional in XML
//                }
//                XMLGregorianCalendar xml
//                        = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​);
//                XMLGregorianCalendar xml2
//                        = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​2);
//
//                //first check availability of rental rate
//                double totalRentalRate = checkForExistenceOfRentalRate(selectedCategoryId, xml, xml2);
//                //second check availability of cars
//                Boolean canReserve = searchCar(selectedCategoryId, selectedModelId, pickupDateTime,
//                        returnDateTime, selectedPickupOutletId, selectedReturnOutletId);
//
//                if (canReserve) {
//                    System.out.println("****************FOUND A CAR!*****************>");
//                    System.out.print("Do you want to make a reservation on this car? (Y/N)");
//                    confirmReservation = sc.nextLine().trim();
//                    if (confirmReservation.equals("Y")) {
//                        System.out.println("The total rental rate for the current reservation is " + totalRentalRate);
//                        System.out.println("Please select the payment option:");
//                        System.out.println("1: Pay now with your credit card");
//                        System.out.println("2: Pay at the outlet");
//                        System.out.print("> ");
//                        Long paymentOption = sc.nextLong();
//                        sc.nextLine();
//                        String ccNumber;
//                        double paidAmt;
//                        if (paymentOption == 2) {
//                            paidAmt = 0;
//                            System.out.print("Please provide your creadit card number>");
//                            ccNumber = sc.nextLine().trim();
//                        } else {
//                            System.out.println("Please provide your credit card number");
//                            ccNumber = sc.nextLine().trim();
//                            paidAmt = totalRentalRate;
//                        }
//                        ReservationRecordEntity reservationRecordEntity = new ReservationRecordEntity(totalRentalRate, pickupDateTime, returnDateTime, ccNumber, paidAmt);
//                        //associate reservation record with customer
//                        //with pickup and return outlet
//                        //with model
//                        //with category
//                        //is it fine if there is no relationship btween rental rate and reservation record?
//                        Long reservationId = createNewReservationRecord(reservationRecordEntity,
//                                currentPartner.getPartnerId(), selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId);
//                        
//
//                        System.out.println("Your reservation with the ID of " + reservationId + " is successful!");
//
//                    }
//                } else {
//                    System.out.println("******SORRY! There is currently no available cars for the specified period.*****");
//                }
//
//                System.out.print("Do you want to try with another period?(Y/N)>");
//                continueConfirmation = sc.nextLine().trim();
//
//            } catch (CategoryNotFoundException_Exception ex1) {
//                System.out.println(ex1.getMessage());
//            } catch (ModelNotFoundException_Exception ex5) {
//                System.out.println(ex5.getMessage());
//            } catch (RentalRateNotFoundException_Exception ex2) {
//                System.out.println("Rental Rate is unavailable for the specified period!");
//            } catch (ReservationCreationException_Exception ex) {
//                System.out.println(ex.getMessage());
//            } catch (DatatypeConfigurationException ex) {
//                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } while (continueConfirmation.equals("Y"));
//    }
//
////    private Long doReserveCar(double totalRentalRate, Long selectedModelId, Long selectedCategoryId,
////            LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Long selectedPickupOutletId,
////            Long selectedReturnOutletId, String ccNumber, double paidAmt) throws ReservationCreationException_Exception {
////
////        try {
////            ReservationRecordEntity reservationRecordEntity = new ReservationRecordEntity(totalRentalRate, pickupDateTime, returnDateTime, ccNumber, paidAmt);
////            //associate reservation record with customer
////            //with pickup and return outlet
////            //with model
////            //with category
////            //is it fine if there is no relationship btween rental rate and reservation record?
////            Long reservationId = createNewReservationRecord(reservationRecordEntity,
////                    currentPartner.getPartnerId(), selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId);
////            return reservationId;
////        } catch (ReservationCreationException_Exception ex) {
////            throw new ReservationCreationException_Exception("Cannot make this reservation");
////        }
////    }
//
//    public Boolean searchCar(Long selectedCategoryId, Long selectedModelId, LocalDateTime pickupDateTime,
//            LocalDateTime returnDateTime, Long selectedPickupOutletId, Long selectedReturnOutletId) throws DatatypeConfigurationException {
//
//        String iso = pickupDateTime.toString();
//        String iso2 = returnDateTime.toString();
//        if (pickupDateTime.getSecond() == 0 && pickupDateTime.getNano() == 0) {
//            iso += ":00"; // necessary hack because the second part is not optional in XML
//        }
//        if (returnDateTime.getSecond() == 0 && returnDateTime.getNano() == 0) {
//            iso += ":00"; // necessary hack because the second part is not optional in XML
//        }
//        XMLGregorianCalendar xml
//                = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​);
//        XMLGregorianCalendar xml2
//                = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​2);
//        //filter to get cars with specified category and model
//        Boolean canReserve = checkCarAvailability(xml, xml2, selectedPickupOutletId, selectedReturnOutletId, selectedCategoryId, selectedModelId);
//        return canReserve;
//    }
//
//    private void printAllCategory() {
//
//        System.out.println("Categories");
//        List<CategoryEntity> categories = retrieveAllCategory();
//        System.out.printf("%10s%20s%n", "Category ID", "Category Name");
//        for (CategoryEntity categoryEntity : categories) {
//            System.out.printf("%10s%20s%n", categoryEntity.getCategoryId(), categoryEntity.getName());
//        }
//    }
//
//    private void printAvailableModel(long selectedCategoryId) throws CategoryNotFoundException_Exception {
//
//        if (selectedCategoryId == -1) {
//            List<ModelEntity> models = retrieveAllModel();
//            System.out.printf("%10s%20s%n", "Model ID", "Model Name");
//            //get list of models under the category
//            // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
//            for (ModelEntity model : models) {
//                System.out.printf("%10s%20s%n", model.getModelId(), model.getModelName());
//            }
//        } else {
//            try {
//                CategoryEntity category = retrieveCategoryByCategoryId(selectedCategoryId);
//                System.out.printf("%10s%20s%n", "Model ID", "Model Name");
//                //get list of models under the category
//                // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
//                for (ModelEntity model : category.getModels()) {
//                    System.out.printf("%10s%20s%n", model.getModelId(), model.getModelName());
//                }
//
//            } catch (CategoryNotFoundException_Exception ex) {
//                throw new CategoryNotFoundException_Exception("The selected category does not exist.");
//            }
//        }
//
//    }
//
//    private void printAvailableOutlet(LocalDateTime pickupDateTime) {
//        try {
//            String iso = pickupDateTime.toString();
//            if (pickupDateTime.getSecond() == 0 && pickupDateTime.getNano() == 0) {
//                iso += ":00"; // necessary hack because the second part is not optional in XML
//            }
//            XMLGregorianCalendar xml
//                    = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​);
//            List<OutletEntity> outlets = retrieveOutletByPickupDateTime(xml);
//            if (outlets.isEmpty()) {
//                System.out.println("No Outlet is Opening at this Hour!");
//            } else {
//                System.out.printf("%10s%100s%20s%20s%n", "Outlet ID", "Address", "Open At", "Close At");
//                for (OutletEntity outlet : outlets) {
//                    if (outlet.getOpeningTime() == null) {
//                        System.out.printf("%10d%100s%20s%20s%n", outlet.getOutletId(), outlet.getAddress(), "(opens 24 hours)", "(opens 24 hours)");
//                    } else {
//                        System.out.printf("%10d%100s%20s%20s%n", outlet.getOutletId(), outlet.getAddress(), outlet.getOpeningTime().toString(), outlet.getClosingTime().toString());
//                    }
//                }
//            }
//        } catch (DatatypeConfigurationException ex) {
//            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void doCancelReservation(Long reservationId) {
//
//        Scanner sc = new Scanner(System.in);
//        String input;
//
//        System.out.println("*** :: View Rental Rate Details :: Cancel Reservation ***\n");
//        System.out.printf("Confirm Cancel Reservation ID %s (Enter 'Y' to Delete)> ", reservationId);
//        input = sc.nextLine().trim();
//        //if the reservation has already been cancelled, they cannot cancel it again:SOLVED
//        //if the reservation is in the past, they cannot be cancelled:
//
//        if (input.equals("Y")) {
//            try {
//                ReservationRecordEntity reservationToCancel = cancelReservation(reservationId);
//                if (reservationToCancel.getRefund() < 0) {
//                    System.out.println("Your reservation " + reservationId + " has been cancelled");
//                    System.out.println("Your credit card " + reservationToCancel.getCcNumber() + " has been charged for " + reservationToCancel.getRefund() + " for cancellation penalty.");
//                } else {
//                    System.out.println("Your reservation " + reservationId + " has been cancelled with the refund of " + reservationToCancel.getRefund() + " to your credit card!");
//                }
//
//            } catch (CancelReservationFailureException_Exception ex) {
//                System.out.println(ex.getMessage() + "\n");
//            }
//        }
//
//    }
//
//    private void doViewAllReservation() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("*** :: View All Rental Rate ***\n");
//
//        List<ReservationRecordEntity> reservationRecordEntities = retrieveAllReservationRecord();
//        System.out.printf("%8s%20s%20s%20s%20s\n", "Reservation id", "pickup date/time", "pickup outlet", "return date/time", "return outlet");
//
//        List<ReservationRecordEntity> reservations = new ArrayList<>();
//        for (ReservationRecordEntity reservation : reservationRecordEntities) {
//            if (reservation.getCustomerEntity().getCustomerId().equals(currentPartner.getPartnerId())) {
//                reservations.add(reservation);
//            }
//        }
//
//        for (ReservationRecordEntity reservationRecordEntity : reservations) {
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
//    private void doViewReservationDetails() {
//
//        Scanner sc = new Scanner(System.in);
//        Integer response = 0;
//
//        System.out.println("*** :: View Reservation Details ***\n");
//        System.out.print("Enter Reservation ID> ");
//        Long reservationId = sc.nextLong();
//
//        try {
//            ReservationRecordEntity reservationRecordEntity = retrieveReservationBylId(reservationId);
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
//            response = sc.nextInt();
//
//            if (response == 1) {
//                doCancelReservation(reservationId);
//            }
//
//        } catch (ReservationRecordNotFoundException_Exception ex) {
//            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
//        }
//    }

    private static PartnerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.login(arg0, arg1);
    }

//    private static ReservationRecordEntity cancelReservation(java.lang.Long arg0) throws CancelReservationFailureException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.cancelReservation(arg0);
//    }
//
//    private static Boolean checkCarAvailability(XMLGregorianCalendar arg0, XMLGregorianCalendar arg1, Long arg2, Long arg3, Long arg4, Long arg5) {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.checkCarAvailability(arg0, arg1, arg2, arg3, arg4, arg5);
//    }
//    private static double checkForExistenceOfRentalRate(long arg0, XMLGregorianCalendar arg1, XMLGregorianCalendar arg2) throws RentalRateNotFoundException_Exception, CategoryNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.checkForExistenceOfRentalRate(arg0, arg1, arg2);
//    }
//
//    private static Long createNewReservationRecord(ws.client.ReservationRecordEntity arg0, java.lang.Long arg1, java.lang.Long arg2, java.lang.Long arg3, java.lang.Long arg4, java.lang.Long arg5) throws ReservationCreationException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.createNewReservationRecord(arg0, arg1, arg2, arg3, arg4, arg5);
//    }
//
//    private static java.util.List<ws.client.CategoryEntity> retrieveAllCategory() {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveAllCategory();
//    }
//
//    private static java.util.List<ws.client.ModelEntity> retrieveAllModel() {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveAllModel();
//    }
//
//    private static java.util.List<ws.client.ReservationRecordEntity> retrieveAllReservationRecord() {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveAllReservationRecord();
//    }
//
//    private static CategoryEntity retrieveCategoryByCategoryId(java.lang.Long arg0) throws CategoryNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveCategoryByCategoryId(arg0);
//    }
//
//    private static ModelEntity retrieveModelByModelId(java.lang.Long arg0) throws ModelNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveModelByModelId(arg0);
//    }
//
//    private static java.util.List<ws.client.OutletEntity> retrieveOutletByPickupDateTime(javax.xml.datatype.XMLGregorianCalendar arg0) {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveOutletByPickupDateTime(arg0);
//    }
//
//    private static ReservationRecordEntity retrieveReservationBylId(java.lang.Long arg0) throws ReservationRecordNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveReservationBylId(arg0);
//    }

}
