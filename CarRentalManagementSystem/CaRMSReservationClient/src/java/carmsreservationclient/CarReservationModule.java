
//package carmsreservationclient;
//
//import ejb.session.stateless.CarEntitySessionBeanRemote;
//import ejb.session.stateless.OutletEntitySessionBeanRemote;
//import ejb.session.stateless.ReservationRecordEntitySessionBeanRemote;
//import entity.CarEntity;
//import entity.OutletEntity;
//import entity.ReservationRecordEntity;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Scanner;
//
//
//public class CarReservationModule {
//
//    private OutletEntitySessionBeanRemote outletEntitySessionBeanRemote;
//    private CarEntitySessionBeanRemote carEntitySessionBeanRemote;
//    private ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote;
//   
//    
//    
//    public CarReservationModule() {
//    }
//
//    public CarReservationModule(ReservationRecordEntitySessionBeanRemote reservationRecordEntitySessionBeanRemote, OutletEntitySessionBeanRemote outletEntitySessionBeanRemote, CarEntitySessionBeanRemote carEntitySessionBeanRemote) {
//        
//        this.reservationRecordEntitySessionBeanRemote = reservationRecordEntitySessionBeanRemote;
//        this.outletEntitySessionBeanRemote = outletEntitySessionBeanRemote;
//        this.carEntitySessionBeanRemote = carEntitySessionBeanRemote;
//        df = new SimpleDateFormat("HH:mm:ss");
//    }
//    
//    
//    
//    public void menuCarReservation(){
//        
//        Scanner sc = new Scanner(System.in);
//        Integer response = 0;
//        
//        while (true){
//            System.out.println("*** CaRMS System :: Car Rental Reservation ***\n");
//            System.out.println("1: Search car");
////            System.out.println("2: Void/Refund");
////            System.out.println("3: View My Sale Transactions");
//            System.out.println("4: Back\n");
//            response = 0;
//            
//            while (response < 1 || response > 4){
//                
//                System.out.print("> ");
//                response = sc.nextInt();
//                
//                if (response == 1){
//                    doSearchCar();
//                } else if(response == 4){
//                    break;
//                } else {
//                    System.out.println("Invalid option, please try again!\n");
