///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ejb.session.stateless;
//
//import entity.ReservationRecordEntity;
//import entity.TransitDriverDispatchRecordEntity;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.ejb.EJB;
//import javax.ejb.Local;
//import javax.ejb.Remote;
//import javax.ejb.Schedule;
//import javax.ejb.Stateless;
//
///**
// *
// * @author admin
// */
//@Stateless
//@Remote(EjbTimerSessionBeanRemote.class)
//@Local(EjbTimerSessionBeanLocal.class)
//public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {
//
//    @EJB(name = "ReservationRecordEntitySessionBeanLocal")
//    private ReservationRecordEntitySessionBeanLocal reservationRecordEntitySessionBeanLocal;
//
//    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
//    public void carAllocationCheckTimer() {
//        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//        System.out.println("********** EjbTimerSession.carAllocationCheckTimer(): Timeout at " + timeStamp);
//        List<ReservationRecordEntity> currentDayReservation = new ArrayList<>();
//        List<ReservationRecordEntity> reservationRecordEntities = reservationRecordEntitySessionBeanLocal.retrieveAllReservationRecord();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        for (ReservationRecordEntity rr : reservationRecordEntities) {
//            if (currentDateTime.equals(rr.getPickUpDateTime())) {
//                currentDayReservation.add(rr);
//            }
//            // }triggerCarAllocation(currentDayReservation);
//        }
//
////    private void triggerCarAllocation(List<ReservationRecordEntity> currentDayReservation) {
////        //go through reservation list
////        //if the car is available in the pickup outlet, assign the car to the reservation
////        //else assign the car to reservation and generate dispatch record
////        for(ReservationRecordEntity rr : currentDayReservation){
////            if(rr.getPickUpOutlet().contains(car))
////        }
////        TransitDriverDispatchRecordEntity tddr = new TransitDriverDispatchRecordEntity();
////    }
//    }
//}
