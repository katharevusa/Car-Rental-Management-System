package ejb.session.singleton;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.ModelEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;

import ejb.session.stateless.RentalRateEntitySessionBeanLocal;
import entity.CarEntity;
import entity.OutletEntity;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import entity.CategoryEntity;
import entity.ModelEntity;

import entity.RentalRateEntity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import util.enumeration.AccessRightEnum;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CarStatusEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewModelFailureException;
import util.exception.EmployeeNotFoundException;
import util.exception.GeneralException;
import util.exception.ModelNotFoundException;
import util.exception.NewCarCreationException;
import util.exception.OutletNotFoundException;

@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB(name = "RentalRateEntitySessionBeanLocal")
    private RentalRateEntitySessionBeanLocal rentalRateEntitySessionBeanLocal;

    @EJB(name = "CarEntitySessionBeanLocal")
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB(name = "EmployeeEntitySessionBeanLocal")
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    private DateTimeFormatter formatter;
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("EmployeeA1");
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }

    }

    private void initializeData() {

        try {

            LocalTime opening = LocalTime.parse("10:00");
            LocalTime closing = LocalTime.parse("22:00");

            try {

                OutletEntity outlet1 = new OutletEntity("Outlet A", "Changi Airport Terminal 1, 80 Airport Boulevard 819642", null, null);
                OutletEntity outlet2 = new OutletEntity("Outlet B", "Changi Airport Terminal 2, 60 Airport Boulevard 819643", null, null);
                OutletEntity outlet3 = new OutletEntity("Outlet C", "Changi Airport Terminal 3, 65 Airport Boulevard 819663", opening, closing);

                outletEntitySessionBeanLocal.createOutlet(outlet1);
                outletEntitySessionBeanLocal.createOutlet(outlet2);
                outletEntitySessionBeanLocal.createOutlet(outlet3);

                employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Employee", "A1", "manager", AccessRightEnum.SALESMANAGER, "EmployeeA1", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Employee", "A2", "manager", AccessRightEnum.OPERATIONSMANAGER, "EmployeeA2", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Employee", "A3", "manager", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, "EmployeeA3", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Employee", "A4", "employee", AccessRightEnum.EMPLOYEE, "EmployeeA4", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Employee", "A5", "employee", AccessRightEnum.EMPLOYEE, "EmployeeA5", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet2.getOutletId(), new EmployeeEntity("Employee", "B1", "manager", AccessRightEnum.SALESMANAGER, "EmployeeB1", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet2.getOutletId(), new EmployeeEntity("Employee", "B2", "manager", AccessRightEnum.OPERATIONSMANAGER, "EmployeeB2", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet2.getOutletId(), new EmployeeEntity("Employee", "B3", "manager", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, "EmployeeB3", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet3.getOutletId(), new EmployeeEntity("Employee", "C1", "manager", AccessRightEnum.SALESMANAGER, "EmployeeC1", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet3.getOutletId(), new EmployeeEntity("Employee", "C2", "manager", AccessRightEnum.OPERATIONSMANAGER, "EmployeeC2", "password"));
                employeeEntitySessionBeanLocal.createEmployee(outlet3.getOutletId(), new EmployeeEntity("Employee", "C3", "manager", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, "EmployeeC3", "password"));
            } catch (OutletNotFoundException ex) {
                ex.getMessage();
            }

//set up the relationship between outlet and employee in the sb
            partnerEntitySessionBeanLocal.createPartner(new PartnerEntity("partner", "password"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Luxury Sedan"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Family Sedan"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Standard Sedan"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("SUV/Minivan"));

//preload model
            try {

                modelEntitySessionBeanLocal.createNewModel(new ModelEntity("Toyota", "Corolla"), Long.valueOf(3));
                modelEntitySessionBeanLocal.createNewModel(new ModelEntity("Honda", "Civic"), Long.valueOf(3));
                modelEntitySessionBeanLocal.createNewModel(new ModelEntity("Nissan", "Sunny"), Long.valueOf(3));
                modelEntitySessionBeanLocal.createNewModel(new ModelEntity("Mercedes", "E Class"), Long.valueOf(1));
                modelEntitySessionBeanLocal.createNewModel(new ModelEntity("BMW", "5 Series"), Long.valueOf(1));
                modelEntitySessionBeanLocal.createNewModel(new ModelEntity("Audi", "A6"), Long.valueOf(1));
            } catch (CreateNewModelFailureException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //preload car
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00A1TC", "Toyota", "Corolla", CarStatusEnum.AVAILABLE), "Toyota", "Corolla", Long.valueOf(1));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00A2TC", "Toyota", "Corolla", CarStatusEnum.AVAILABLE), "Toyota", "Corolla", Long.valueOf(1));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00A3TC", "Toyota", "Corolla", CarStatusEnum.AVAILABLE), "Toyota", "Corolla", Long.valueOf(1));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00B1HC", "Honda", "Civic", CarStatusEnum.AVAILABLE), "Honda", "Civic", Long.valueOf(2));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00B2HC", "Honda", "Civic", CarStatusEnum.AVAILABLE), "Honda", "Civic", Long.valueOf(2));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00B3HC", "Honda", "Civic", CarStatusEnum.AVAILABLE), "Honda", "Civic", Long.valueOf(2));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00C1NS", "Nissan", "Sunny", CarStatusEnum.AVAILABLE), "Nissan", "Sunny", Long.valueOf(3));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00C2NS", "Nissan", "Sunny", CarStatusEnum.AVAILABLE), "Nissan", "Sunny", Long.valueOf(3));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("SS00C3NS", "Nissan", "Sunny", CarStatusEnum.REPAIR), "Nissan", "Sunny", Long.valueOf(3));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("LS00A4ME", "Mercedes", "E Class", CarStatusEnum.AVAILABLE), "Mercedes", "E Class", Long.valueOf(1));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("LS00B4B5", "BMW", "5 Series", CarStatusEnum.AVAILABLE), "BMW", "5 Series", Long.valueOf(2));
                carEntitySessionBeanLocal.createNewCar(new CarEntity("LS00C4A6", "Audi", "A6", CarStatusEnum.AVAILABLE), "Audi", "A6", Long.valueOf(3));
            } catch (NewCarCreationException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OutletNotFoundException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ModelNotFoundException ex) {
                Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
//preload rental rate

            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(3), new RentalRateEntity("Default", 100.00, LocalDateTime.parse("01/01/1990 00:00", formatter), LocalDateTime.parse("01/01/2100 00:00", formatter)));
            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(3), new RentalRateEntity("Weekend Promo", 80.00, LocalDateTime.parse("06/12/2019 12:00", formatter), LocalDateTime.parse("08/12/2019 00:00", formatter)));
            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(2), new RentalRateEntity("Default", 200.00, LocalDateTime.parse("01/01/1990 00:00", formatter), LocalDateTime.parse("01/01/2100 00:00", formatter)));
            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(1), new RentalRateEntity("Monday", 310.00, LocalDateTime.parse("02/12/2019 00:00", formatter), LocalDateTime.parse("02/12/2019 23:59", formatter)));
            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(1), new RentalRateEntity("Tuesday", 320.00, LocalDateTime.parse("03/12/2019 00:00", formatter), LocalDateTime.parse("03/12/2019 23:59", formatter)));
            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(1), new RentalRateEntity("Wednesday", 330.00, LocalDateTime.parse("04/12/2019 00:00", formatter), LocalDateTime.parse("04/12/2019 23:59", formatter)));
            rentalRateEntitySessionBeanLocal.createNewRentalRate(Long.valueOf(1), new RentalRateEntity("Weekday Promo", 250.00, LocalDateTime.parse("04/12/2019 12:00", formatter), LocalDateTime.parse("05/12/2019 12:00", formatter)));

        } catch (CategoryNotFoundException | GeneralException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
