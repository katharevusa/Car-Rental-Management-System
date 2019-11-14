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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author admin
 */
@Stateless
@Remote(CarAllocationSessionBeanRemote.class)
@Local(CarAllocationSessionBeanLocal.class)
public class CarAllocationSessionBean implements CarAllocationSessionBeanRemote, CarAllocationSessionBeanLocal {
 @EJB(name = "TransitDriverDispatchRecordEntitySessionBeanLocal")
    private TransitDriverDispatchRecordEntitySessionBeanLocal transitDriverDispatchRecordEntitySessionBeanLocal;

    @EJB(name = "CarEntitySessionBeanLocal")
    private CarEntitySessionBeanLocal carEntitySessionBeanLocal;

    @EJB(name = "OutletEntitySessionBeanLocal")
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;

    @EJB(name = "ReservationRecordEntitySessionBeanLocal")
    private ReservationRecordEntitySessionBeanLocal reservationRecordEntitySessionBeanLocal;

    @Override
    public void carAllocationCheckTimer() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSession.carAllocationCheckTimer(): Timeout at " + timeStamp);
        List<ReservationRecordEntity> currentDayReservationList = new ArrayList<>();
        List<ReservationRecordEntity> reservationRecordEntities = reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
        LocalDateTime currentDateTime = LocalDateTime.now();
        for (ReservationRecordEntity rr : reservationRecordEntities) {
            if (currentDateTime.equals(rr.getPickUpDateTime())) {
                currentDayReservationList.add(rr);
            }
        }
        triggerCarAllocation(currentDayReservationList);
    }

    private void triggerCarAllocation(List<ReservationRecordEntity> currentDayReservationList) {
        //every outlet has a dispatch record
        for (ReservationRecordEntity rr : currentDayReservationList) {
            OutletEntity pickUpOutlet = rr.getPickUpOutlet();
            CategoryEntity category = rr.getCategory();
            ModelEntity model = rr.getModel();

            //找list of cars under this category and model
            //找list of cars under this outlet
            //如果没有的话找list of cars under other outlets
            //顾客输入了model
            if (model != null) {
                List<CarEntity> carsWithMatchingCategoryAndModel = retrieveAllCarsByModel(model);
               

                for (CarEntity car : carsWithMatchingCategoryAndModel) {
                    if (car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() !=null && !car.isDisabled()) {
                        car.setReservationRecordEntity(rr);
                        rr.setCarEntity(car);
                        break;
                    } else if (!car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() != null && !car.isDisabled()) {
                        car.setReservationRecordEntity(rr);
                        rr.setCarEntity(car);
                        TransitDriverDispatchRecordEntity newDispatchRecord = new TransitDriverDispatchRecordEntity();
                        //generate new dispatch record
                        transitDriverDispatchRecordEntitySessionBeanLocal.createNewDispatchRecord(car.getOutletEntity(), rr, newDispatchRecord);
                    }
                }
            } //顾客没有输入model
            else {
                List<CarEntity> carsWithMatchingCategory = retrieveAllCarsByCategory(category);
                for (CarEntity car : carsWithMatchingCategory) {
                    if (car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() !=null && !car.isDisabled()) {
                        car.setReservationRecordEntity(rr);
                        rr.setCarEntity(car);
                        break;
                    } else if (!car.getOutletEntity().equals(pickUpOutlet) && car.getOutletEntity() !=null && !car.isDisabled()) {
                        car.setReservationRecordEntity(rr);
                        rr.setCarEntity(car);
                        //generate new dispatch record
                        TransitDriverDispatchRecordEntity newDispatchRecord = new TransitDriverDispatchRecordEntity();
                        transitDriverDispatchRecordEntitySessionBeanLocal.createNewDispatchRecord(car.getOutletEntity(), rr, newDispatchRecord);
                    }
                }
            }
        }

    }

    private List<CarEntity> retrieveAllCarsByModel(ModelEntity model) {
       List<CarEntity> allcars = carEntitySessionBeanLocal.retrieveAllCars();
       List<CarEntity> modelcars = new ArrayList<>();
       for(CarEntity car:allcars){
           if(car.getModelEntity().equals(model)){
               modelcars.add(car);
           }
       }return modelcars;
    }

    private List<CarEntity> retrieveAllCarsByCategory(CategoryEntity category) {
       List<CarEntity> allcars = carEntitySessionBeanLocal.retrieveAllCars();
       List<CarEntity> categorycars = new ArrayList<>();
       for(CarEntity car:allcars){
           if(car.getModelEntity().getCategoryEntity().equals(category)){
               categorycars.add(car);
           }
       }return categorycars;
    }
    
}
