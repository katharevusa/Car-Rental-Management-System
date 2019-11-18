package ejb.session.ws;

import ejb.session.stateless.CarEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ModelEntitySessionBeanLocal;
import ejb.session.stateless.OutletEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RentalRateEntitySessionBeanLocal;
import ejb.session.stateless.ReservationRecordEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.PartnerEntity;
import entity.ReservationRecordEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.CancelReservationFailureException;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.RegistrationFailureException;
import util.exception.RentalRateNotFoundException;
import util.exception.ReservationCreationException;
import util.exception.ReservationRecordNotFoundException;

@WebService(serviceName = "CaRMSWebService")
@Stateless()

public class CaRMSWebService {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "CarEntitySessionBeanLocal")
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @EJB(name = "ReservationRecordEntitySessionBeanLocal")
    private ReservationRecordEntitySessionBeanLocal reservationRecordEntitySessionBeanLocal;

    @EJB(name = "RentalRateEntitySessionBeanLocal")
    private RentalRateEntitySessionBeanLocal rentalRateEntitySessionBeanLocal;

    @EJB(name = "ModelEntitySessionBeanLocal")
    private ModelEntitySessionBeanLocal modelEntitySessionBeanLocal;

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @WebMethod
    public PartnerEntity login(@WebParam String username,@WebParam String password) throws InvalidLoginCredentialException {
        return partnerEntitySessionBeanLocal.login(username, password);
    }
    
    @WebMethod
    public List<CategoryEntity> retrieveAllCategory() {
        return categoryEntitySessionBeanLocal.retrieveAllCategory();
    }
    
    @WebMethod
    public List<ModelEntity> retrieveAllModel() {
        return modelEntitySessionBeanLocal.retrieveAllModel();
    }
    
    @WebMethod
    public CategoryEntity retrieveCategoryByCategoryId(@WebParam Long categoryId) throws CategoryNotFoundException {
        return categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
    }
    
    @WebMethod
    public ModelEntity retrieveModelByModelId(@WebParam Long modelId) throws ModelNotFoundException {
        return modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
    }
    
    @WebMethod
    public Long retrieveCategoryIdByModelId(Long modelId) throws ModelNotFoundException {
        return modelEntitySessionBeanLocal.retrieveCategoryIdByModelId(modelId);
    }

    @WebMethod
    public List<OutletEntity> retrieveOutletByPickupDateTime(@WebParam Date date) {
        
        LocalDateTime pickupDateTime = Instant.ofEpochMilli(date.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        return outletEntitySessionBeanLocal.retrieveOutletByPickupDateTime(pickupDateTime);
    }
    
    @WebMethod
    public double checkForExistenceOfRentalRate(@WebParam Long selectedCategoryId, @WebParam Date date1,
           @WebParam Date date2) throws CategoryNotFoundException, RentalRateNotFoundException {
        
        LocalDateTime pickupDateTime = Instant.ofEpochMilli(date1.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        LocalDateTime returnDateTime = Instant.ofEpochMilli(date2.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        return rentalRateEntitySessionBeanLocal.checkForExistenceOfRentalRate(selectedCategoryId, pickupDateTime, returnDateTime);
    }
    
    @WebMethod
    public Boolean checkCarAvailability(@WebParam Date date1, @WebParam Date date2,
           @WebParam  Long selectedPickupOutletId,@WebParam  Long selectedReturnOutletId,@WebParam  Long selectedCategoryId, @WebParam Long selectedModelId) {
        
        LocalDateTime pickupDateTime = Instant.ofEpochMilli(date1.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        LocalDateTime returnDateTime = Instant.ofEpochMilli(date2.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        
        return carEntitySessionBeanLocal.checkCarAvailability(pickupDateTime, returnDateTime, selectedPickupOutletId, selectedReturnOutletId, selectedCategoryId, selectedModelId);
    }

    @WebMethod
    public Long createNewReservationRecord(@WebParam double totalRentalRate, @WebParam Long selectedModelId, @WebParam Long selectedCategoryId,
            @WebParam Date date1, @WebParam Date date2, @WebParam Long selectedPickupOutletId,
            @WebParam Long selectedReturnOutletId, @WebParam String ccNumber, @WebParam double paidAmt, @WebParam Long customerId,
            @WebParam Long partnerId) throws ReservationCreationException {

        LocalDateTime pickupDateTime = Instant.ofEpochMilli(date1.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        LocalDateTime returnDateTime = Instant.ofEpochMilli(date2.getTime() )
                            .atZone(ZoneId.systemDefault() )
                            .toLocalDateTime();
        
        return reservationRecordEntitySessionBeanLocal.createReservationRecordForWebClient(totalRentalRate, selectedModelId, selectedCategoryId, pickupDateTime, returnDateTime, selectedPickupOutletId, selectedReturnOutletId, ccNumber, 0,customerId,partnerId);
    }
    
    @WebMethod
    public CustomerEntity retrieveCustomerByCustomerId(@WebParam Long customerId) throws CustomerNotFoundException {
        return customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
    }
    
    @WebMethod
    public List<ReservationRecordEntity> retrieveAllReservationRecord() {
        return reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
    }
    
    @WebMethod
    public Long retrievePartnerIdByReservationId(@WebParam Long reservationId) throws ReservationRecordNotFoundException{
        return reservationRecordEntitySessionBeanLocal.retrievePartnerIdByReservationId(reservationId);
    }

    @WebMethod
    public ReservationRecordEntity retrieveReservationBylId(@WebParam Long reservationId) throws ReservationRecordNotFoundException {
        return reservationRecordEntitySessionBeanLocal.retrieveReservationBylId(reservationId);
    }
    
    @WebMethod
    public ReservationRecordEntity cancelReservation(@WebParam Long reservationId) throws CancelReservationFailureException {
        return reservationRecordEntitySessionBeanLocal.cancelReservation(reservationId);
    }
    
//    @WebMethod
//    public CustomerEntity registerationInWeb(@WebParam Long partnerId,@WebParam String username,@WebParam String password, @WebParam String email, @WebParam String mobileNumber) throws RegistrationFailureException {
//        return customerEntitySessionBeanLocal.registerationInWeb(partnerId, username, password, email, mobileNumber);
//    }
//
//    @WebMethod
//    public CustomerEntity retrieveCustomerByCustomerId(@WebParam Long customerId) throws CustomerNotFoundException {
//        return customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
//    }
//
//    @WebMethod
//    public ModelEntity retrieveModelByModelId(@WebParam Long modelId) throws ModelNotFoundException {
//        return modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
//    }
//
//    @WebMethod
//
//    public double checkForExistenceOfRentalRate(@WebParam Long selectedCategoryId, @WebParam LocalDateTime pickupDateTime,
//           @WebParam LocalDateTime returnDateTime) throws CategoryNotFoundException, RentalRateNotFoundException {
//        return rentalRateEntitySessionBeanLocal.checkForExistenceOfRentalRate(selectedCategoryId, pickupDateTime, returnDateTime);
//    }
//
//    @WebMethod
//    public Long createNewReservationRecord(@WebParam ReservationRecordEntity reservationRecordEntity, @WebParam Long customerId,
//            @WebParam Long modelId, @WebParam Long categoryId, @WebParam Long pickupOutletId,@WebParam Long returnOutletId) throws ReservationCreationException {
//        return reservationRecordEntitySessionBeanLocal.createNewReservationRecord(reservationRecordEntity, customerId, modelId, categoryId, pickupOutletId, returnOutletId);
//    }
//
//    @WebMethod
//    public Boolean checkCarAvailability(@WebParam LocalDateTime pickupDateTime, @WebParam LocalDateTime returnDateTime,
//           @WebParam  Long selectedPickupOutletId,@WebParam  Long selectedReturnOutletId,@WebParam  Long selectedCategoryId, @WebParam Long selectedModelId) {
//        return carEntitySessionBeanLocal.checkCarAvailability(pickupDateTime, returnDateTime, selectedPickupOutletId, selectedReturnOutletId, selectedCategoryId, selectedModelId);
//    }
//
//    @WebMethod
//    public List<CategoryEntity> retrieveAllCategory() {
//        return categoryEntitySessionBeanLocal.retrieveAllCategory();
//    }
//
//    @WebMethod
//    public List<ModelEntity> retrieveAllModel() {
//        return modelEntitySessionBeanLocal.retrieveAllModel();
//    }
//    @WebMethod
//    public ReservationRecordEntity createReservationInWebService(@WebParam Long partnerId, @WebParam Long selectedModelId, @WebParam Long selectedCategoryId, @WebParam Long selectedPickupOutletId, @WebParam Long selectedReturnedOutletId,@WebParam  LocalDateTime pickupDateTime,@WebParam  LocalDateTime returnDateTime, @WebParam Double totalRentalRate,@WebParam  String ccNumber,@WebParam Double paidAmount) throws ReservationRecordNotFoundException {
//        return reservationRecordEntitySessionBeanLocal.createReservationInWebService(partnerId, selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnedOutletId, pickupDateTime, returnDateTime, totalRentalRate, ccNumber,paidAmount);
//    }
//    @WebMethod
//    public CategoryEntity retrieveCategoryByCategoryId(@WebParam Long categoryId) throws CategoryNotFoundException {
//        return categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
//    }
//
//    @WebMethod
//    public List<OutletEntity> retrieveOutletByPickupDateTime(@WebParam LocalDateTime pickupDateTime) {
//        return outletEntitySessionBeanLocal.retrieveOutletByPickupDateTime(pickupDateTime);
//    }
//
//    @WebMethod
//    public ReservationRecordEntity cancelReservation(@WebParam Long reservationId) throws CancelReservationFailureException {
//        return reservationRecordEntitySessionBeanLocal.cancelReservation(reservationId);
//    }
//
//    @WebMethod
//    public List<ReservationRecordEntity> retrieveAllReservationRecord() {
//        return reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
//    }
//
//    @WebMethod
//    public ReservationRecordEntity retrieveReservationBylId(@WebParam Long reservationId) throws ReservationRecordNotFoundException {
//        return reservationRecordEntitySessionBeanLocal.retrieveReservationBylId(reservationId);
//    }

}
