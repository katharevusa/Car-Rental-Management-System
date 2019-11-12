package ejb.session.singleton;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.OutletEntity;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import entity.CategoryEntity;
import entity.RentalRateEntity;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import util.enumeration.AccessRightEnum;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidFieldEnteredException;
import util.exception.OutletNotFoundException;

@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @EJB(name = "EmployeeEntitySessionBeanLocal")
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    private SimpleDateFormat dateFormatter;

    public DataInitializationSessionBean() {
        dateFormatter = new SimpleDateFormat();

    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("manager1");
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {

//        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        LocalTime opening = LocalTime.parse("04:30");
        LocalTime closing = LocalTime.parse("11:30");

        try {
            OutletEntity outlet1 = new OutletEntity("T1", "Changi Airport Terminal 1, 80 Airport Boulevard 819642", opening, closing);
            OutletEntity outlet2 = new OutletEntity("T2", "Changi Airport Terminal 2, 60 Airport Boulevard 819643", opening, closing);
            OutletEntity outlet3 = new OutletEntity("T3", "Changi Airport Terminal 3, 65 Airport Boulevard 819663", opening, closing);
            OutletEntity outlet4 = new OutletEntity("T4", "Changi Airport Terminal 4 , 10 Airport Boulevard 819665", opening, closing);
            outletEntitySessionBeanLocal.createOutlet(outlet1);
            outletEntitySessionBeanLocal.createOutlet(outlet2);
            outletEntitySessionBeanLocal.createOutlet(outlet3);
            outletEntitySessionBeanLocal.createOutlet(outlet4);

            employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Kaixin", "Zhu", "manager", AccessRightEnum.SALESMANAGER, "manager1", "password"));
            employeeEntitySessionBeanLocal.createEmployee(outlet2.getOutletId(), new EmployeeEntity("Kaixin", "Zhu", "manager", AccessRightEnum.OPERATIONSMANAGER, "manager2", "password"));
            employeeEntitySessionBeanLocal.createEmployee(outlet3.getOutletId(), new EmployeeEntity("Kaixin", "Zhu", "manager", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, "manager3", "password"));
            employeeEntitySessionBeanLocal.createEmployee(outlet4.getOutletId(), new EmployeeEntity("Kaixin", "Zhu", "manager", AccessRightEnum.SALESMANAGER, "manager4", "password"));
        } catch (OutletNotFoundException ex) {
            ex.getMessage();
        }

        //set up the relationship between outlet and employee in the sb
        partnerEntitySessionBeanLocal.createPartner(new PartnerEntity("partner","password"));
        categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Luxury Sedan"));
        categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Family Sedan"));
        categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Standard Sedan"));
        categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("SUV/Minivan"));
        //empty list of rental rate is initialised in each car category

        //initialise partner
    }

}
