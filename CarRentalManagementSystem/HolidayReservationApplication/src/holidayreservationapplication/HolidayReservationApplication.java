/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationapplication;

import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.PartnerEntity;

/**
 *
 * @author admin
 */
public class HolidayReservationApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }

//    private static PartnerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.login(arg0, arg1);
//    }

//    private static ReservationRecordEntity cancelReservation(java.lang.Long arg0) throws CancelReservationFailureException_Exception {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.cancelReservation(arg0);
//    }
//
//    private static Boolean checkCarAvailability(ws.client.LocalDateTime arg0, ws.client.LocalDateTime arg1, java.lang.Long arg2, java.lang.Long arg3, java.lang.Long arg4, java.lang.Long arg5) {
//        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
//        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
//        return port.checkCarAvailability(arg0, arg1, arg2, arg3, arg4, arg5);
//    }
//
//    private static double checkForExistenceOfRentalRate(java.lang.Long arg0, ws.client.LocalDateTime arg1, ws.client.LocalDateTime arg2) throws RentalRateNotFoundException_Exception, CategoryNotFoundException_Exception {
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
//    private static java.util.List<ws.client.OutletEntity> retrieveOutletByPickupDateTime(ws.client.LocalDateTime arg0) {
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
//    
}
