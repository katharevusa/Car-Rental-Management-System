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
import java.time.LocalDateTime;
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
    public PartnerEntity login(@WebParam String username, String password) throws InvalidLoginCredentialException {
        return partnerEntitySessionBeanLocal.login(username, password);
    }

    @WebMethod
    public CustomerEntity registerationInWeb(Long partnerId, String username, String password, String email, String mobileNumber) throws RegistrationFailureException {
        return customerEntitySessionBeanLocal.registerationInWeb(partnerId, username, password, email, mobileNumber);
    }

    @WebMethod
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        return customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
    }

    @WebMethod
    public ModelEntity retrieveModelByModelId(@WebParam Long modelId) throws ModelNotFoundException {
        return modelEntitySessionBeanLocal.retrieveModelByModelId(modelId);
    }

    @WebMethod

    public double checkForExistenceOfRentalRate(@WebParam Long selectedCategoryId, LocalDateTime pickupDateTime,
            LocalDateTime returnDateTime) throws CategoryNotFoundException, RentalRateNotFoundException {
        return rentalRateEntitySessionBeanLocal.checkForExistenceOfRentalRate(selectedCategoryId, pickupDateTime, returnDateTime);
    }

    @WebMethod
    public Long createNewReservationRecord(@WebParam ReservationRecordEntity reservationRecordEntity, Long customerId,
            Long modelId, Long categoryId, Long pickupOutletId, Long returnOutletId) throws ReservationCreationException {
        return reservationRecordEntitySessionBeanLocal.createNewReservationRecord(reservationRecordEntity, customerId, modelId, categoryId, pickupOutletId, returnOutletId);
    }

    @WebMethod
    public Boolean checkCarAvailability(@WebParam LocalDateTime pickupDateTime, LocalDateTime returnDateTime,
            Long selectedPickupOutletId, Long selectedReturnOutletId, Long selectedCategoryId, Long selectedModelId) {
        return carEntitySessionBeanLocal.checkCarAvailability(pickupDateTime, returnDateTime, selectedPickupOutletId, selectedReturnOutletId, selectedCategoryId, selectedModelId);
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
    public ReservationRecordEntity createReservationInWebService(Long partnerId, Long selectedModelId, Long selectedCategoryId, Long selectedPickupOutletId, Long selectedReturnedOutletId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Double totalRentalRate, String ccNumber,Double paidAmount) throws ReservationRecordNotFoundException {
        return reservationRecordEntitySessionBeanLocal.createReservationInWebService(partnerId, selectedModelId, selectedCategoryId, selectedPickupOutletId, selectedReturnedOutletId, pickupDateTime, returnDateTime, totalRentalRate, ccNumber,paidAmount);
    }
    @WebMethod
    public CategoryEntity retrieveCategoryByCategoryId(@WebParam Long categoryId) throws CategoryNotFoundException {
        return categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
    }

    @WebMethod
    public List<OutletEntity> retrieveOutletByPickupDateTime(@WebParam LocalDateTime pickupDateTime) {
        return outletEntitySessionBeanLocal.retrieveOutletByPickupDateTime(pickupDateTime);
    }

    @WebMethod
    public ReservationRecordEntity cancelReservation(@WebParam Long reservationId) throws CancelReservationFailureException {
        return reservationRecordEntitySessionBeanLocal.cancelReservation(reservationId);
    }

    @WebMethod
    public List<ReservationRecordEntity> retrieveAllReservationRecord() {
        return reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
    }

    @WebMethod
    public ReservationRecordEntity retrieveReservationBylId(@WebParam Long reservationId) throws ReservationRecordNotFoundException {
        return reservationRecordEntitySessionBeanLocal.retrieveReservationBylId(reservationId);
    }

}
