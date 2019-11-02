
package ejb.session.singleton;

import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import entity.OutletEntity;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import entity.CategoryEntity;
import entity.RentalRateEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import util.enumeration.AccessRightEnum;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidFieldEnteredException;



@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    private SimpleDateFormat dateFormatter;

    public DataInitializationSessionBean()
    {
        dateFormatter =  new SimpleDateFormat();

    }
    @PostConstruct
    public void postConstruct()
    {
      try
        {
            employeeEntitySessionBeanLocal.retrieveEmployeeByUsername("manager1");
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData()
    {
             try
        {
            String address = "1 Ah Hood Rd";
            String dataInString1 = "08:30:00";
            String dataInString2 = "20:30:00";
            Date openHour = dateFormatter.parse(dataInString1);
            Date closeHour = dateFormatter.parse(dataInString2);

            OutletEntity outlet1 = new OutletEntity( "T1", "Changi Airport Terminal 1, 80 Airport Boulevard 819642", openHour,closeHour);
            OutletEntity outlet2 = new OutletEntity( "T2", "Changi Airport Terminal 2, 60 Airport Boulevard 819643", openHour,closeHour);
            OutletEntity outlet3 = new OutletEntity("T3", "Changi Airport Terminal 3, 65 Airport Boulevard 819663", openHour,closeHour);
            OutletEntity outlet4 = new OutletEntity("T4", "Changi Airport Terminal 4 , 10 Airport Boulevard 819665", openHour,closeHour);
            outletEntitySessionBeanLocal.createOutlet(outlet1);
            outletEntitySessionBeanLocal.createOutlet(outlet2);
            outletEntitySessionBeanLocal.createOutlet(outlet3);
            outletEntitySessionBeanLocal.createOutlet(outlet4);
            
            //retrieve outlet by outletId
            employeeEntitySessionBeanLocal.createEmployee(outlet1.getOutletId(), new EmployeeEntity("Kaixin","Zhu","manager",AccessRightEnum.EMPLOYEE,"manager1","password"));
            employeeEntitySessionBeanLocal.createEmployee(outlet2.getOutletId(), new EmployeeEntity("Kaixin","Zhu","manager",AccessRightEnum.EMPLOYEE,"manager2","password"));
            employeeEntitySessionBeanLocal.createEmployee(outlet3.getOutletId(), new EmployeeEntity("Kaixin","Zhu","manager",AccessRightEnum.EMPLOYEE,"manager3","password"));
            employeeEntitySessionBeanLocal.createEmployee(outlet4.getOutletId(), new EmployeeEntity("Kaixin","Zhu","manager",AccessRightEnum.EMPLOYEE,"manager4","password"));
            //set up the relationship between outlet and employee in the sb
            
            
            partnerEntitySessionBeanLocal.createPartner(new PartnerEntity("Holiday.com"));
            
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Luxury Sedan"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Family Sedan"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("Standard Sedan"));
            categoryEntitySessionBeanLocal.createCategory(new CategoryEntity("SUV/Minivan"));
            //empty list of rental rate is initialised in each car category
        }
             catch(ParseException ex){
                 //invalidFieldEnteredException is being remobed
            ex.printStackTrace();
        }
    }
}
 