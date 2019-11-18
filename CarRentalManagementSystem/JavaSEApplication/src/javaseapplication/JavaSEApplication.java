///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package javaseapplication;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.InputMismatchException;
//import java.util.List;
//import java.util.Scanner;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.xml.datatype.DatatypeConfigurationException;
//import javax.xml.datatype.DatatypeFactory;
//import javax.xml.datatype.XMLGregorianCalendar;
//import ws.client.CancelReservationFailureException;
//import ws.client.CancelReservationFailureException_Exception;
//import ws.client.CategoryEntity;
//import ws.client.CategoryNotFoundException;
//import ws.client.CategoryNotFoundException_Exception;
//import ws.client.CustomerEntity;
//import ws.client.InvalidLoginCredentialException_Exception;
//import ws.client.ModelEntity;
//import ws.client.ModelNotFoundException_Exception;
//import ws.client.OutletEntity;
//import ws.client.PartnerEntity;
//import ws.client.RegistrationFailureException;
//import ws.client.RegistrationFailureException_Exception;
//import ws.client.RentalRateNotFoundException_Exception;
//import ws.client.ReservationCreationException;
//import ws.client.ReservationCreationException_Exception;
//import ws.client.ReservationRecordEntity;
//import ws.client.ReservationRecordNotFoundException_Exception;
//
///**
// *
// * @author admin
// */
//public class JavaSEApplication {
//
//    public static PartnerEntity currentPartner;
//    public static CustomerEntity currentCustomer;
//    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        Integer response = 0;
//
//        while (true) {
//
//            System.out.println("*** Welcome to Merlion Car Rental System(v1.0) ***\n");
//            System.out.println("1: Login");
//            System.out.println("2: Exit\n");
//
//            response = sc.nextInt();
//
//            if (response == 1) {
//                try {
//                    doLogin();
//                    System.out.println("Login successful!\n");
//                    doRegister();
//                    menuMain();
//                } catch (InvalidLoginCredentialException_Exception ex) {
//                    System.out.println(ex.getMessage());
//                } catch (RegistrationFailureException_Exception ex) {
//                    Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            } else if (response == 2) {
//                break;
//            } else {
//                System.out.println("Invalid login credential. Please try again.\n");
//            }
//        }
//    }
//
//    public static void doLogin() throws InvalidLoginCredentialException_Exception {
//        Scanner sc = new Scanner(System.in);
//        String username = "";
//        String password = "";
//
//        System.out.println("*** Merlion System :: Login ***\n");
//        System.out.print("Enter username> ");
//        username = sc.nextLine().trim();
//        System.out.print("Enter password> ");
//        password = sc.nextLine().trim();
//
//        if (username.length() > 0 && password.length() > 0) {
//            currentPartner = login(username, password);
//        } else {
//            throw new InvalidLoginCredentialException_Exception("Missing login caredential!");
//        }
//    }
//
//    public static void doRegister() throws RegistrationFailureException_Exception {
//        Scanner sc = new Scanner(System.in);
//        String username = "";
//        String password = "";
//        String email = "";
//        String mobileNumber = "";
//
//        System.out.println("*** Merlion System :: Register ***\n");
//        System.out.print("Enter username> ");
//        username = sc.nextLine().trim();
//        System.out.print("Enter password> ");
//        password = sc.nextLine().trim();
//        System.out.print("Enter email> ");
//        email = sc.nextLine().trim();
//        System.out.print("Enter mobileNumber> ");
//        mobileNumber = sc.nextLine().trim();
//        if (username.length() > 0 && password.length() > 0 && email.length() > 0 && mobileNumber.length() > 0) {
//            currentCustomer = registerationInWeb(currentPartner.getPartnerId(), username, password, email, mobileNumber);
//            menuMain();
//        } else {
//            throw new RegistrationFailureException_Exception("Missing login caredential!");
//        }
//
//    }
//
//    public static void menuMain() {
//
//        Scanner sc = new Scanner(System.in);
//        Integer response = 0;
//
//        while (true) {
//            System.out.println("*** Merlion Car Rental System (v1.0) ***\n");
//            System.out.println("You are login as " + currentPartner.getUsername());
//            System.out.println("You are doing the reservation for " + currentCustomer.getUsername() + "\n");
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
//    private static void doSearchCar() {
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
//                System.out.print("Do you want to choose a specific category? (Y/any key for No)>");
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
//                    System.out.println("Do you want to choose a specific model? >");
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
//
//                System.out.print("Please select a return outlet>");
//                long selectedReturnOutletId = sc.nextLong();
//                sc.nextLine();
//
//                String isop = pickupDateTime.toString();
//                if (pickupDateTime.getSecond() == 0 && pickupDateTime.getNano() == 0) {
//                    isop += ":00"; // necessary hack because the second part is not optional in XML
//                }
//                XMLGregorianCalendar xmlpickup = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​p);
//
//                String isor = returnDateTime.toString();
//                if (returnDateTime.getSecond() == 0 && returnDateTime.getNano() == 0) {
//                    isor += ":00"; // necessary hack because the second part is not optional in XML
//                }
//                XMLGregorianCalendar xmlreturn = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​r);
//
//                //first check availability of rental rate
//                double totalRentalRate = checkForExistenceOfRentalRate(selectedCategoryId, xmlpickup, xmlreturn);
//                //second check availability of cars
//
//                System.out.println("category ID " + selectedCategoryId + " + " + "model ID" + selectedModelId);
//                Boolean canReserve = searchCar(selectedCategoryId, selectedModelId, xmlpickup,
//                        xmlreturn, selectedPickupOutletId, selectedReturnOutletId);
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
//
//                        Long reservationId = doReserveCar(totalRentalRate, selectedModelId, selectedCategoryId,
//                                xmlpickup, xmlreturn, selectedPickupOutletId, selectedReturnOutletId, ccNumber, paidAmt);
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
//            } catch (ModelNotFoundException_Exception ex) {
//                Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (DatatypeConfigurationException ex) {
//                Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (CategoryNotFoundException_Exception ex) {
//                Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (RentalRateNotFoundException_Exception ex) {
//                Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } while (continueConfirmation.equals("Y"));
//
//    }
//
//    private static Long doReserveCar(double totalRentalRate, long selectedModelId, long selectedCategoryId, XMLGregorianCalendar pickupDateTime, XMLGregorianCalendar returnDateTime, long selectedPickupOutletId, long selectedReturnOutletId, String ccNumber, double paidAmt) {
//
//        Long reservationId = null;
//        try {
////            LocalDateTime dt = LocalDate.now().atStartOfDay();
//// 
////            XMLGregorianCalendar xmlDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(dt.format(DateTimeFormatter.ISO_DATE_TIME));
//
//            String isop = pickupDateTime.toString();
////            if (pickupDateTime.getSecond() == 0 && pickupDateTime.getNano() == 0) {
////                isop += ":00"; // necessary hack because the second part is not optional in XML
////            }
//            XMLGregorianCalendar xmlpickup = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​p);
//
//            String isor = returnDateTime.toString();
////            if (returnDateTime.getSecond() == 0 && returnDateTime.getNano() == 0) {
////                isor += ":00"; // necessary hack because the second part is not optional in XML
////            }
//            XMLGregorianCalendar xmlreturn = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso‌​r);
//
//            ReservationRecordEntity reservationRecordEntity = createReservationInWebService(currentPartner.getPartnerId(), selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId, xmlpickup, xmlreturn, totalRentalRate, ccNumber, paidAmt);
//
////            reservationId = createNewReservationRecord(reservationRecordEntity,
////                    currentPartner.getPartnerId(), selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnOutletId);
//        } catch (ReservationRecordNotFoundException_Exception ex) {
//            System.out.println("Sorry! Your reservation is unsuccessful.");
//        } catch (DatatypeConfigurationException ex) {
//            Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return reservationId;
//
//    }
//
//    public static Boolean searchCar(long selectedCategoryId, long selectedModelId, XMLGregorianCalendar pickupDateTime, XMLGregorianCalendar returnDateTime, long selectedPickupOutletId, long selectedReturnOutletId) {
//
//        //filter to get cars with specified category and model
//        Boolean canReserve = checkCarAvailability(pickupDateTime, returnDateTime,
//                selectedPickupOutletId, selectedReturnOutletId, selectedCategoryId, selectedModelId);
//
//        return canReserve;
//
//    }
//
//    private static void printAllCategory() {
//
//        System.out.println("Categories");
//        List<CategoryEntity> categories = retrieveAllCategory();
//        System.out.printf("%10s%20s%n", "Category ID", "Category Name");
//        for (CategoryEntity categoryEntity : categories) {
//            System.out.printf("%10s%20s%n", categoryEntity.getCategoryId(), categoryEntity.getName());
//        }
//    }
//
//    private static void printAvailableModel(long selectedCategoryId) throws CategoryNotFoundException_Exception {
//
//        if (selectedCategoryId == -1) {
//            List<ModelEntity> models = retrieveAllModel();
//            System.out.printf("%10s%20s%20s%n", "Make and Model ID", "Make", "Model");
//            //get list of models under the category
//            // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
//            for (ModelEntity model : models) {
//                System.out.printf("%10s%20s%20s%n", model.getModelId(), model.getMake(), model.getModelName());
//            }
//        } else {
//            try {
//                CategoryEntity category = retrieveCategoryByCategoryId(selectedCategoryId);
//                System.out.printf("%10s%20s%20s%n", "Make and Model ID", "Make", "Model");
//                //get list of models under the category
//                // <ModelEntity> models = categoryEntitySessionBeanRemote.retrieveAllModelsUnderCategory(category);
//                for (ModelEntity model : category.getModels()) {
//                    System.out.printf("%10s%20s%20s%n", model.getModelId(), model.getMake(), model.getModelName());
//                }
//
//            } catch (CategoryNotFoundException_Exception ex) {
//                throw new CategoryNotFoundException_Exception("The selected category does not exist.");
//            }
//        }
//
//    }
//
//    private static void printAvailableOutlet(LocalDateTime pickupDateTime) {
//        try {
//
//            //XMLGregorianCalendar xmlreturn = DatatypeFactory.newInstance().newXMLGregorianCalendar(pickupDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
//
//            Date date = Date.from(pickupDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            GregorianCalendar c = new GregorianCalendar();
//            c.setTime(date);
//            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
//       
//            List<OutletEntity> outlets = retrieveOutletByPickupDateTime(date2);
//            if (outlets.isEmpty()) {
//                System.out.println("No outlet is opening at this hour!");
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
//            Logger.getLogger(JavaSEApplication.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static void doCancelReservation(Long reservationId) throws CancelReservationFailureException_Exception {
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
//            ReservationRecordEntity reservationToCancel = cancelReservation(reservationId);
//            if (reservationToCancel.getRefund() < 0) {
//                System.out.println("Your reservation " + reservationId + " has been cancelled");
//                System.out.println("Your credit card " + reservationToCancel.getCcNumber() + " has been charged for " + reservationToCancel.getRefund() + " for cancellation penalty.");
//            } else {
//                System.out.println("Your reservation " + reservationId + " has been cancelled with the refund of " + reservationToCancel.getRefund() + " to your credit card!");
//            }
//        }
//
//    }
//
//    private static void doViewAllReservation() {
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
//    private static void doViewReservationDetails() {
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
//        } catch (CancelReservationFailureException_Exception ex) {
//            System.out.println("Cancel failure");
//        }
//    }
//
//    private static PartnerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.login(arg0, arg1);
//    }
//
//    private static ModelEntity retrieveModelByModelId(java.lang.Long arg0) throws ModelNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveModelByModelId(arg0);
//    }
//
//    private static double checkForExistenceOfRentalRate(long arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2) throws CategoryNotFoundException_Exception, RentalRateNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.checkForExistenceOfRentalRate(arg0, arg1, arg2);
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
//    private static CategoryEntity retrieveCategoryByCategoryId(java.lang.Long arg0) throws CategoryNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveCategoryByCategoryId(arg0);
//    }
//
//    private static java.util.List<ws.client.OutletEntity> retrieveOutletByPickupDateTime(javax.xml.datatype.XMLGregorianCalendar arg0) {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveOutletByPickupDateTime(arg0);
//    }
//
//    private static ReservationRecordEntity cancelReservation(java.lang.Long arg0) throws CancelReservationFailureException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.cancelReservation(arg0);
//    }
//
//    private static java.util.List<ws.client.ReservationRecordEntity> retrieveAllReservationRecord() {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveAllReservationRecord();
//    }
//
//    private static ReservationRecordEntity retrieveReservationBylId(java.lang.Long arg0) throws ReservationRecordNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.retrieveReservationBylId(arg0);
//    }
//
//    private static Boolean checkCarAvailability(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1, long arg2, long arg3, long arg4, long arg5) {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.checkCarAvailability(arg0, arg1, arg2, arg3, arg4, arg5);
//    }
//
//    private static Long createNewReservationRecord(ws.client.ReservationRecordEntity arg0, java.lang.Long arg1, java.lang.Long arg2, java.lang.Long arg3, java.lang.Long arg4, java.lang.Long arg5) throws ReservationCreationException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.createNewReservationRecord(arg0, arg1, arg2, arg3, arg4, arg5);
//    }
//
//    private static CustomerEntity registerationInWeb(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4) throws RegistrationFailureException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.registerationInWeb(arg0, arg1, arg2, arg3, arg4);
//    }
//
//    private static ReservationRecordEntity createReservationInWebService(Long arg0, long arg1, long arg2, long arg3, long arg4, javax.xml.datatype.XMLGregorianCalendar arg5, javax.xml.datatype.XMLGregorianCalendar arg6, double arg7, String arg8, double arg9) throws ReservationRecordNotFoundException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.createReservationInWebService(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
//    }
//
//}
