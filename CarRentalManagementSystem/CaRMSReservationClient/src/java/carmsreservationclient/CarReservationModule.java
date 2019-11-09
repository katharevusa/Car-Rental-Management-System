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
//                }
//            }
//            
//            if (response == 4){
//                break;
//            }
//        }
//        
//    }
//    
//    public void doSearchCar(){
//        Scanner sc =new Scanner(System.in);
//        String pickupDateTime = "";
//        String pickupOutlet = "";
//        String returnDateTime = "";
//        String returnOutlet = "";
//        String confirmReservation = "";
//        
//        do{
//            
//            
//            System.out.println("The available outlets are:");
//            int idxNumber = 1;
//
//            List<OutletEntity> outlets = outletEntitySessionBeanRemote.retrieveAllOutlet();
//            for (OutletEntity outletEntity : outlets) {
//                System.out.println("" + idxNumber + ". " + "Outlet " + outletEntity.getOutletId());
//                System.out.println("Address: " + outletEntity.getAddress());
//                System.out.println("Open from " + df.format(outletEntity.getOpeningTime()) + " to " + df.format(outletEntity.getClosingTime()));
//                idxNumber++;
//            }
//
//            System.out.println("Please select a pickup outlet: ");
//            int selectedPickupOutlet = sc.nextInt();
//            System.out.println("Please select a return outlet: ");
//            int selectedReturnOutlet = sc.nextInt();
//       
//            if (selectedPickupOutlet < 1 || selectedPickupOutlet > outlets.size()){
//                break;
//            } else if (selectedReturnOutlet < 1 || selectedReturnOutlet > outlets.size()){
//                break;
//            }
//            
//            try{
//                System.out.print("Please specify the Date you need a car(YYYY-MM-DD):");
//                Date pickupDate = df.parse(sc.next().trim());
//                System.out.println("");
//                System.out.print("Please specify the time you want to collect the car(HH:MM:SS):");
//                Date pickupTime = df.parse(sc.next().trim());
//                System.out.print("Please specify the Date you return the car(YYYY-MM-DD):");
//                Date returnDate = df.parse(sc.next().trim());
//                System.out.println("");
//                System.out.print("Please specify the time you can return the car(HH:MM:SS):");
//                Date returnTime = df.parse(sc.next().trim());
//                
//            } catch(ParseException ex){
//                System.out.println("Invalid inputs.");
//                break;
//            }
//            
//            
//            /*
//            //pre-condition:
//            out of X available car, at least one free car
//            
//            //post-condition
//            car A is marked as reservaed and tagged to a reservation record
//            pre-condition violated  - throw insufficentavailablecarexception
//            
//            //invariant condition
//            number of reservation for a particular caregory or model cannot exceed the capacity of the rental company
//            
//            */
//           
//            
////            List<ReservationRecordEntity> resevReservationRecords = 
////            
////            List<CarEntity> suitableCars = new ArrayList<>();
////            List<ReservationRecordEntity> reservations;
////            List<CarEntity> cars = carEntitySessionBeanRemote.retrieveAllCars();
////            for (CarEntity car : cars){
////                if (car.getOutletEntity().getOutletId() == outlets.get(selectedPickupOutlet - 1).getOutletId()){
////                    
////                }
////            }
//            
//            
//            
////            System.out.println("The following cars are available :");
////            if (cars.size() == 0){
////                System.out.println("Oops, there is currently no car available for the selected duration");
////                break;
////            } else {
////                for (CarEntity car : cars){
////                    
////                }
////            }
//            
//            
//        } while(confirmReservation.equals("Yes"));
//    }
//    
//    
//}
