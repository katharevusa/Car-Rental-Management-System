/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.OutletEntity;
import entity.TransitDriverDispatchRecordEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.AccessRightEnum;
import util.enumeration.DispatchRecordEnum;
import util.exception.AssignTDDRFailureException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;

/**
 *
 * @author admin
 */
@Stateless
@Local(EmployeeEntitySessionBeanLocal.class)
@Remote(EmployeeEntitySessionBeanRemote.class)
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @EJB
    private TransitDriverDispatchRecordEntitySessionBeanLocal transitDriverDispatchRecordEntitySessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    @EJB
    private OutletEntitySessionBeanLocal outletEntitySessionBeanLocal;
    
    
    
    @Override
    public Long createEmployee(Long outletId, EmployeeEntity newEmployee) throws OutletNotFoundException{
        
        try {
            em.persist(newEmployee);
            OutletEntity outlet = outletEntitySessionBeanLocal.retrieveOutletByOutletId(outletId);
            newEmployee.setOutletEntity(outlet);
            outlet.getEmployees().add(newEmployee);
            em.flush();
            em.refresh(newEmployee);
            return newEmployee.getEmployeeId();
        } catch (OutletNotFoundException outletNotFoundException) {
            throw new OutletNotFoundException("Outlet ID " + outletId + "does not exist!");
        }
        
    }
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployee(){
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e");
        return query.getResultList();
    }

    @Override
    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) {

        EmployeeEntity employee = em.find(EmployeeEntity.class, employeeId);
        return employee;

    }

    @Override
    public void updateEmployee(EmployeeEntity employee) {
        em.merge(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        EmployeeEntity employee = retrieveEmployeeByEmployeeId(employeeId);
        em.remove(employee);
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException
    {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            
            return (EmployeeEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }
    
    @Override
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException{
        try
        {
            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);
           
            if(employeeEntity.getPassword().equals(password))
            {
                //also need to return the enum               
                return employeeEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void assignEmployeeToTDDR(Long employeeId,Long dispatchRecordId) throws AssignTDDRFailureException{
        
        try{
            
            EmployeeEntity employee = retrieveEmployeeByEmployeeId(employeeId);
            if(employee == null){
                throw new AssignTDDRFailureException("Employee Id " + employeeId + " does not exist");
            }
            
            TransitDriverDispatchRecordEntity record = transitDriverDispatchRecordEntitySessionBeanLocal.retrieveDispatchRecordById(dispatchRecordId);
            if(record == null){
                throw new AssignTDDRFailureException("Dispatch record Id " + dispatchRecordId + " does not exist!");
            }
            
            record.setEmployee(employee);
            record.setDispatchRecordStatus(DispatchRecordEnum.ASSIGNED);
            employee.getDispatchRecords().add(record);
            em.flush();
            
        } catch (AssignTDDRFailureException ex){
            throw new AssignTDDRFailureException(ex.getMessage());
        }
        
    }

}
