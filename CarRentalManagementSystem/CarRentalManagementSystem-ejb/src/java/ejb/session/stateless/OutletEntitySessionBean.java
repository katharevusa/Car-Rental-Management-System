/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;


@Local(OutletEntitySessionBeanLocal.class)
@Remote(OutletEntitySessionBeanRemote.class)
@Stateless
public class OutletEntitySessionBean implements OutletEntitySessionBeanRemote, OutletEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    
    @Override
    public Long createOutlet(OutletEntity newOutlet) {
        em.persist(newOutlet);
        em.flush();       
        return newOutlet.getOutletId(); 
    }

    @Override
    public OutletEntity retrieveOutletByOutletId(Long outletId) throws OutletNotFoundException{
        
        OutletEntity outlet = em.find(OutletEntity.class, outletId);
        
        if (outlet != null){
            outlet.getDispatchRecord().size();
            return outlet;
        } else {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
    }

    @Override
    public void updateOutlet(OutletEntity outlet) {
        em.merge(outlet);
    }

    @Override
    public void deleteOutlet(Long outletId) throws OutletNotFoundException{
        
        try {
            OutletEntity outlet = retrieveOutletByOutletId(outletId);
            em.remove(outlet);
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
        
    }
    
    @Override
    public List<OutletEntity> retrieveAllOutlet(){
        
        Query query = em.createQuery("SELECT o FROM OutletEntity o");
        return query.getResultList();
    }  
    
    @Override
    public List<OutletEntity> retrieveOutletByPickupDateTime(LocalDateTime pickupDateTime){
        
        List<OutletEntity> allOutlets = retrieveAllOutlet();
        List<OutletEntity> availableOutlets = new ArrayList<>();
        for(OutletEntity outlet:allOutlets){
            if (outlet.getOpeningTime() == null){
                availableOutlets.add(outlet);
            } else if(outlet.getOpeningTime().isBefore(pickupDateTime.toLocalTime()) || outlet.getOpeningTime().compareTo(pickupDateTime.toLocalTime()) == 0){
                availableOutlets.add(outlet);
            }
        }
        return availableOutlets;
    }
    @Override
    public List<TransitDriverDispatchRecordEntity> retrieveAllDispatchRecord(OutletEntity outletEntity){
        
      //  em.merge(outletEntity);
    
        List<TransitDriverDispatchRecordEntity> list = outletEntity.getDispatchRecord();
        list.size();
        return list;
    }
    public List<EmployeeEntity> retrieveAllEmployee(OutletEntity outlet){
        em.merge(outlet);
        return outlet.getEmployees();
    }
}