/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.RentalRateEntitySessionBeanRemote;
import entity.CategoryEntity;
import entity.EmployeeEntity;
import entity.RentalRateEntity;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.CategoryNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidAccessRightException;
import java.lang.String;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author admin
 */
class SalesManagerModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private EmployeeEntity currentEmployee;
    private CategoryEntity enteredCategory;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote;
    //   private SimpleDateFormat dateFormatter;
    private DateTimeFormatter formatter;

    public SalesManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        this.enteredCategory = enteredCategory;
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

    public SalesManagerModule(EmployeeEntity currentEmployee, RentalRateEntitySessionBeanRemote rentalRateEntitySessionBeanRemote,
            CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.rentalRateEntitySessionBeanRemote = rentalRateEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;

    }

    public void menuSalesManagerModule() throws InvalidAccessRightException {

        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.SALESMANAGER) {
            throw new InvalidAccessRightException("You don't have EMPLOYEE rights to access the sales manager module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Car Rental Management System :: Sales manager module ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View Rental Rate Details");
            System.out.println("3: View All Rental Rate");
            System.out.println("4: Log out\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewRentalRate();
                } else if (response == 2) {
                    doViewRentalRateDetails();
                } else if (response == 3) {
                    doViewAllRentalRate();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    private void doCreateNewRentalRate() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** CaRMS :: Sales manager :: Create New Rental Rate ***\n");
            System.out.println("Choose Car Category> \n");
            System.out.println("1. Luxury Sedan");
            System.out.println("2. Family Sedan");
            System.out.println("3. Standard Sedan");
            System.out.println("4. SUV/Minivan Sedan");
            System.out.println("5. Back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(1));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("Category cannot be found");
                    }
                    enterRentalRateDetails(enteredCategory);
                } else if (response == 2) {
                    try {
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(2));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("Category cannot be found");
                    }
                    enterRentalRateDetails(enteredCategory);
                } else if (response == 3) {
                    try {
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(3));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("Category cannot be found");
                    }
                    enterRentalRateDetails(enteredCategory);
                } else if (response == 4) {
                    try {
                        enteredCategory = categoryEntitySessionBeanRemote.retrieveCategoryByCategoryId(Long.valueOf(4));
                    } catch (CategoryNotFoundException ex) {
                        System.out.println("Category cannot be found");
                    }
                    enterRentalRateDetails(enteredCategory);
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

    private void enterRentalRateDetails(CategoryEntity enteredCategory) {

        Scanner scanner = new Scanner(System.in);
        RentalRateEntity newRentalRateEntity = new RentalRateEntity();
        System.out.println("*** CaRMS sales manager terminal :: Create rental rate under " + enteredCategory.getName() + " ***\n\n");
        System.out.print("Enter name of rental rate> ");
        newRentalRateEntity.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter rental rate per day> ");
        newRentalRateEntity.setRatePerDay(scanner.nextDouble());
        scanner.nextLine();
   
        System.out.print("Enter validity period: enter starting date time (dd/MM/yyyy HH:mm) > ");
        String startDate = scanner.nextLine().trim();
        LocalDateTime dateTime1;
        if (startDate.equals(null)) {
            dateTime1 = LocalDateTime.parse("01/01/1990 00:00", formatter);
        } else {
            dateTime1 = LocalDateTime.parse(startDate, formatter);
        }
        newRentalRateEntity.setStartDateTime(dateTime1);

        //the ending must be after the starting date
        System.out.print("Enter validity period: enter ending date (dd/mm/yyyy HH:mm) > ");
        String endDate = scanner.nextLine().trim();
        LocalDateTime dateTime2;
        if (endDate.equals(null)) {
            dateTime2 = LocalDateTime.parse("01/01/2100 00:00", formatter);
        } else {
            dateTime2 = LocalDateTime.parse(endDate, formatter);
        }
        newRentalRateEntity.setEndDateTime(dateTime2);

        Set<ConstraintViolation<RentalRateEntity>> constraintViolations = validator.validate(newRentalRateEntity);

        if (constraintViolations.isEmpty()) {

            try {
                newRentalRateEntity = rentalRateEntitySessionBeanRemote.createNewRentalRate(enteredCategory.getCategoryId(), newRentalRateEntity);
                System.out.println("New rental rate of " + newRentalRateEntity.getRatePerDay() + " is created under " + newRentalRateEntity.getCategory().getName() + "\n");
            } catch (CategoryNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (GeneralException ex) {
                System.out.println("An unknown error has occurred while creating the new rental rate!: " + ex.getMessage() + "\n");
            } catch (RentalRateExistException ex) {
                Logger.getLogger(SalesManagerModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showInputDataValidationErrorsForRentalRateEntity(constraintViolations);
        }
    }

    private void showInputDataValidationErrorsForRentalRateEntity(Set<ConstraintViolation<RentalRateEntity>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doViewAllRentalRate() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** :: View All Rental Rate ***\n");

        List<RentalRateEntity> rentalRateEntities = rentalRateEntitySessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%10s%20s%20s%20s%20s%20s\n", "Rental rate id", "name", "rate per day", "start date", "end date", "category");

        for (RentalRateEntity rentalRateEntity : rentalRateEntities) {
            System.out.printf("%10s%20s%20f%20s%20s%20s\n", rentalRateEntity.getRentalRateId(), rentalRateEntity.getRentalRateName(),
                    rentalRateEntity.getRatePerDay(),
                    rentalRateEntity.getStartDateTime(),
                    rentalRateEntity.getEndDateTime(),
                    rentalRateEntity.getCategory().getName());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** :: View RentalRate Details ***\n");
        System.out.print("Enter Rental Rate ID> ");
        Long rentalRateId = scanner.nextLong();

        try {
            RentalRateEntity rentalRateEntity = rentalRateEntitySessionBeanRemote.retrieveRentalRateByRentalId(rentalRateId);
            System.out.printf("%10s%20s%20s%20s%20s%20s\n", "Rental rate id", "name", "rate per day", "start date", "end date", "category");
            System.out.printf("%10s%20s%20f%20s%20s%20s\n", rentalRateEntity.getRentalRateId(), rentalRateEntity.getRentalRateName(),
                    rentalRateEntity.getRatePerDay(),
                    rentalRateEntity.getStartDateTime(),
                    rentalRateEntity.getEndDateTime(),
                    rentalRateEntity.getCategory().getName());
            System.out.println("------------------------");
            System.out.println("1: Update Rental Rate");
            System.out.println("2: Delete Rental Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateRentalRate(rentalRateEntity);
            } else if (response == 2) {
                doDeleteRentalRate(rentalRateEntity);
            }
        } catch (RentalRateNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateRentalRate(RentalRateEntity rentalRateEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** :: View Rental Rate Details :: Update Rental Rate ***\n");
        System.out.print("Enter Rental Rate Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            rentalRateEntity.setRentalRateName(input);
        }

        System.out.print("Enter Rate Per Day (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            rentalRateEntity.setRatePerDay(Double.valueOf(input));
        }
        System.out.print("Enter Starting date dd/mm/yyyy hh:mm (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            LocalDateTime date1 = LocalDateTime.parse(input, formatter);
            rentalRateEntity.setStartDateTime(date1);
        }
        System.out.print("Enter Ending date dd/mm/yyyy hh:mm (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            LocalDateTime date2 = LocalDateTime.parse(input, formatter);
            rentalRateEntity.setEndDateTime(date2);
        }

        //should i allow user to change the category as well?
        Set<ConstraintViolation<RentalRateEntity>> constraintViolations = validator.validate(rentalRateEntity);

        if (constraintViolations.isEmpty()) {
            try {
                rentalRateEntitySessionBeanRemote.updateRentalRate(rentalRateEntity);
                System.out.println("Product updated successfully!\n");
            } catch (RentalRateNotFoundException | UpdateRentalRateException ex) {
                System.out.println("An error has occurred while updating rental rate: " + ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForRentalRateEntity(constraintViolations);
        }

    }

    private void doDeleteRentalRate(RentalRateEntity rentalRateEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** :: View Rental Rate Details :: Delete Rental Rate ***\n");
        System.out.printf("Confirm Delete Rental Rate %s ID %s) (Enter 'Y' to Delete)> ", rentalRateEntity.getRentalRateId(), rentalRateEntity.getRentalRateName());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                rentalRateEntitySessionBeanRemote.deleteRentalRate(rentalRateEntity.getRentalRateId());
                System.out.println("Product deleted successfully!\n");
            } catch (RentalRateNotFoundException ex) {
                System.out.println("An error has occurred while deleting rental rate: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Rental rate NOT deleted!\n");
        }
    }

}
