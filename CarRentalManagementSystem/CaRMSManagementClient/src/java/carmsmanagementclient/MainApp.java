package carmsmanagementclient;

import ejb.session.stateless.CarAllocationSessionBeanRemote;
import ejb.session.stateless.CarEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.ModelEntitySessionBeanRemote;
import ejb.session.stateless.OutletEntitySessionBeanRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;

import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

class MainApp {

    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    private EmployeeEntity currentEmployee;
    private AccessRightEnum currentAccessRight;
    private SalesManagerModule salesManagerModule;
    private OperationManagerModule operationManagerModule;
    private CustomerServiceExecutiveModule customerServiceExecutiveModule;
    private ModelEntitySessionBeanRemote modelEntitySessionBeanRemote;
    private ReservationRecordEntitySessionBeanRemote reservationRecordSessionBeanEntityRemote;
    private CarAllocationSessionBeanRemote carAllocationSessionBeanRemote;
    private TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote;

    public MainApp() {
    }

    public MainApp(OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote,
            CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote, ModelEntitySessionBeanRemote modelEntitySessionBeanRemote, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote,
            CarEntitySessionBeanRemote carEntitySessionBeanRemote,
            ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote,
            CarAllocationSessionBeanRemote carAllocationSessionBeanRemote,
            TransitDriverDispatchRecordEntitySessionBeanRemote transitDriverDispatchRecordEntitySessionBeanRemote) {

        this();

        this.modelEntitySessionBeanRemote = modelEntitySessionBeanRemote;
        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
        this.reservationRecordSessionBeanEntityRemote = reservationRecordEntitySessionBeanRemote;
        this.carAllocationSessionBeanRemote = carAllocationSessionBeanRemote;
        this.transitDriverDispatchRecordEntitySessionBeanRemote = transitDriverDispatchRecordEntitySessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Car Rental Management System  ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");

                        if (currentEmployee.getAccessRightEnum() == AccessRightEnum.SALESMANAGER) {
                            salesManagerModule = new SalesManagerModule(currentEmployee, rentalRateEntitySessionBeanRemote, categoryEntitySessionBeanRemote);
                            try {
                                salesManagerModule.menuSalesManagerModule();
                            } catch (InvalidAccessRightException ex) {
                                System.out.println("invalid access");
                            }
                        } else if (currentEmployee.getAccessRightEnum() == AccessRightEnum.OPERATIONSMANAGER) {
                            operationManagerModule = new OperationManagerModule(currentEmployee, modelEntitySessionBeanRemote, 
                                    outletEntitySessionBeanRemote, carEntitySessionBeanRemote, carAllocationSessionBeanRemote, 
                                    transitDriverDispatchRecordEntitySessionBeanRemote, employeeEntitySessionBeanRemote, 
                                    categoryEntitySessionBeanRemote,reservationRecordSessionBeanEntityRemote);
                            try {
                                operationManagerModule.menuOperationManagerModule();
                            } catch (InvalidAccessRightException ex) {
                                System.out.println("invalid access");
                            }
                        } else if (currentEmployee.getAccessRightEnum() == AccessRightEnum.CUSTOMERSERVICEEXECUTIVE) {
                            customerServiceExecutiveModule = new CustomerServiceExecutiveModule(currentEmployee, reservationRecordSessionBeanEntityRemote);
                            try {
                                customerServiceExecutiveModule.menuCustomerServiceExecutiveModule();
                            } catch (InvalidAccessRightException ex) {
                                System.out.println("invalid access");
                            }
                        }

                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
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

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMSystem :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeEntitySessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
}
