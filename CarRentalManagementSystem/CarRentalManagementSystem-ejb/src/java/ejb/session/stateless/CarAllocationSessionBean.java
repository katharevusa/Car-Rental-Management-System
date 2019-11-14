/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarEntity;
import entity.CategoryEntity;
import entity.ModelEntity;
import entity.OutletEntity;
import entity.ReservationRecordEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author admin
 */
@Stateless
@Remote(CarAllocationSessionBeanRemote.class)
@Local(CarAllocationSessionBeanLocal.class)
public class CarAllocationSessionBean implements CarAllocationSessionBeanRemote, CarAllocationSessionBeanLocal {

    @EJB
    private TransitDriverDispatchRecordEntitySessionBeanLocal transitDriverDispatchRecordEntitySessionBeanLocal;
    @EJB
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;
    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    @EJB
    private ReservationRecordEntitySessionBeanLocal reservationRecordEntitySessionBeanLocal;
    
    
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public void carAllocationCheckTimer(LocalDateTime triggerDateTime) {

        LocalDate currDate = triggerDateTime.toLocalDate();

        List<ReservationRecordEntity> currentDayReservationList = new ArrayList<>();
        List<ReservationRecordEntity> reservations = reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
        for (ReservationRecordEntity reservation : reservations) {
            if (reservation.getPickUpDateTime().toLocalDate().isEqual(currDate) && reservation.getIsCancelled()==false) {
                currentDayReservationList.add(reservation);
            }
        }

        triggerCarAllocation(currentDayReservationList);
        
    }

//        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//        System.out.println("********** EjbTimerSession.carAllocationCheckTimer(): Timeout at " + timeStamp);
//        List<ReservationRecordEntity> currentDayReservationList = new ArrayList<>();
//        List<ReservationRecordEntity> reservationRecordEntities = reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        for (ReservationRecordEntity rr : reservationRecordEntities) {
//            if (currentDateTime.equals(rr.getPickUpDateTime())) {
//                currentDayReservationList.add(rr);
//            }
//        }
//        triggerCarAllocation(currentDayReservationList);
    
    

    private void triggerCarAllocation(List<ReservationRecordEntity> currentDayReservationList) {
        
        

        for (ReservationRecordEntity reservation : currentDayReservationList) {
            OutletEntity pickUpOutlet = reservation.getPickUpOutlet();//must have
            CategoryEntity category = reservation.getCategory();//must have
            ModelEntity model = reservation.getModel();//may be null
         
            
            if(model != null){
                List<CarEntity> carsWithMatchingCategoryAndModel = retrieveAllCarsByModel(model.getModelId());
                for (CarEntity car:carsWithMatchingCategoryAndModel){
                    
                    if (!car.isDisabled() && !(car.getStatus().equals(CarStatusEnum.RESERVED))){
                        //prioritise those already in the pickup outlet (available && car.outlet == reservation.pickupoutlet)
                        if (car.getStatus()==CarStatusEnum.AVAILABLE && car.getOutletEntity().getOutletId().equals(reservation.getPickUpOutlet().getOutletId())){
                            //assign car to reservation
                            car.setReservationRecordEntity(reservation);
                            reservation.setCarEntity(car);
                            car.setStatus(CarStatusEnum.RESERVED);
                        }
                        //second prioritise those return to the pickup outlet in time
                        else if (car.getStatus() == CarStatusEnum.ONRENTAL){
                            if (!(car.getReservationRecordEntity().getReturnDateTime().isAfter(reservation.getPickUpDateTime())) 
                                    && car.getReservationRecordEntity().getReturnOutlet().getOutletId().equals(reservation.getReturnOutlet().getOutletId())){
                                //assign car to reservation
                                car.setReservationRecordEntity(reservation);
                                reservation.setCarEntity(car);
                                car.setStatus(CarStatusEnum.RESERVED);
                            }
                        } else if (car.getStatus()==CarStatusEnum.AVAILABLE && !(car.getOutletEntity().getOutletId().equals(reservation.getPickUpOutlet().getOutletId()))){
                            //dispatch record
                            
                            
                            //assign car to reservation
                            car.setReservationRecordEntity(reservation);
                            reservation.setCarEntity(car);
                            car.setStatus(CarStatusEnum.RESERVED);
                        } else if (car.getStatus() == CarStatusEnum.ONRENTAL && !(car.getReservationRecordEntity().getReturnDateTime().isAfter(reservation.getPickUpDateTime()))){
                            if (!(car.getReservationRecordEntity().getReturnOutlet().getOutletId().equals(reservation.getReturnOutlet().getOutletId()))){
                                //dispatch record
                                
                                
                                //assign car to reservation
                                car.setReservationRecordEntity(reservation);
                                reservation.setCarEntity(car);
                                car.setStatus(CarStatusEnum.RESERVED);
                            }
                        }
                    }
                }
                
            } else {
                
                List<CarEntity> carsWithMatchingCategory = retrieveAllCarsByCategory(category.getCategoryId());
                for (CarEntity car:carsWithMatchingCategory){
                    
                    if (!car.isDisabled() && !(car.getStatus().equals(CarStatusEnum.RESERVED))){
                        //prioritise those already in the pickup outlet (available && car.outlet == reservation.pickupoutlet)
                        if (car.getStatus()==CarStatusEnum.AVAILABLE && car.getOutletEntity().getOutletId().equals(reservation.getPickUpOutlet().getOutletId())){
                            //assign car to reservation
                            car.setReservationRecordEntity(reservation);
                            reservation.setCarEntity(car);
                            car.setStatus(CarStatusEnum.RESERVED);
                            
                        }
                        //second prioritise those return to the pickup outlet in time
                        else if (car.getStatus() == CarStatusEnum.ONRENTAL){
                            if (!(car.getReservationRecordEntity().getReturnDateTime().isAfter(reservation.getPickUpDateTime())) 
                                    && car.getReservationRecordEntity().getReturnOutlet().getOutletId().equals(reservation.getReturnOutlet().getOutletId())){
                                //assign car to reservation
                                car.setReservationRecordEntity(reservation);
                                reservation.setCarEntity(car);
                                car.setStatus(CarStatusEnum.RESERVED);
                            }
                        } else if (car.getStatus()==CarStatusEnum.AVAILABLE && !(car.getOutletEntity().getOutletId().equals(reservation.getPickUpOutlet().getOutletId()))){
                            //need a dispatch
                            //assign car to reservation
                            car.setReservationRecordEntity(reservation);
                            reservation.setCarEntity(car);
                            car.setStatus(CarStatusEnum.RESERVED);
                        } else if (car.getStatus() == CarStatusEnum.ONRENTAL && !(car.getReservationRecordEntity().getReturnDateTime().isAfter(reservation.getPickUpDateTime()))){
                            if (!(car.getReservationRecordEntity().getReturnOutlet().getOutletId().equals(reservation.getReturnOutlet().getOutletId()))){
                                //need a dispatch
                                //assign car to reservation
                                car.setReservationRecordEntity(reservation);
                                reservation.setCarEntity(car);
                                car.setStatus(CarStatusEnum.RESERVED);
                            }
                        }
                    }
                }
            }
            
        }
        
        
        //assume that all reservation is assigned with a car
        for (ReservationRecordEntity reservation : currentDayReservationList){
            //extra checking for reservation having a car
            if (reservation.getCarEntity() != null){
                if (!(reservation.getPickUpOutlet().getOutletId().equals(reservation.getCarEntity().getOutletEntity()))){
                    
                }
            }
            
        }
        
        
//        //every outlet has a dispatch record
//        for (ReservationRecordEntity rr : currentDayReservationList) {
//            OutletEntity pickUpOutlet = rr.getPickUpOutlet();
//            CategoryEntity category = rr.getCategory();
//            ModelEntity model = rr.getModel();
//
//            //找list of cars under this category and model
//            //找list of cars under this outlet
//            //如果没有的话找list of cars under other outlets
//            //顾客输入了model
//            if (model != null) {
//                List<CarEntity> carsWithMatchingCategoryAndModel = retrieveAllCarsByModel(model);
//
//                for (CarEntity car : carsWithMatchingCategoryAndModel) {
//                    if (car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() != null && !car.isDisabled()) {
//                        car.setReservationRecordEntity(rr);
//                        rr.setCarEntity(car);
//                        break;
//                    } else if (!car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() != null && !car.isDisabled()) {
//                        car.setReservationRecordEntity(rr);
//                        rr.setCarEntity(car);
//                        TransitDriverDispatchRecordEntity newDispatchRecord = new TransitDriverDispatchRecordEntity();
//                        //generate new dispatch record
//                        transitDriverDispatchRecordEntitySessionBeanLocal.createNewDispatchRecord(car.getOutletEntity(), rr, newDispatchRecord);
//                    }
//                }
//            } //顾客没有输入model
//            else {
//                List<CarEntity> carsWithMatchingCategory = retrieveAllCarsByCategory(category);
//                for (CarEntity car : carsWithMatchingCategory) {
//                    if (car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() != null && !car.isDisabled()) {
//                        car.setReservationRecordEntity(rr);
//                        rr.setCarEntity(car);
//                        break;
//                    } else if (!car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() != null && !car.isDisabled()) {
//                        car.setReservationRecordEntity(rr);
//                        rr.setCarEntity(car);
//                        //generate new dispatch record
//                        TransitDriverDispatchRecordEntity newDispatchRecord = new TransitDriverDispatchRecordEntity();
//                        transitDriverDispatchRecordEntitySessionBeanLocal.createNewDispatchRecord(car.getOutletEntity(), rr, newDispatchRecord);
//                    }
//                }
//            }

    }

    private List<CarEntity> retrieveAllCarsByModel(Long modelId) {
        List<CarEntity> allcars = carEntitySessionBeanLocal.retrieveAllCars();
        List<CarEntity> modelcars = new ArrayList<>();
        for (CarEntity car : allcars) {
            if (car.getModelEntity().getModelId().equals(modelId)) {
                modelcars.add(car);
            }
        }
        return modelcars;
    }

    private List<CarEntity> retrieveAllCarsByCategory(Long categoryId) {
        List<CarEntity> allcars = carEntitySessionBeanLocal.retrieveAllCars();
        List<CarEntity> categorycars = new ArrayList<>();
        for (CarEntity car : allcars) {
            if (car.getModelEntity().getCategoryEntity().getCategoryId().equals(categoryId)) {
                categorycars.add(car);
            }
        }
        return categorycars;
    }
    
    

}
