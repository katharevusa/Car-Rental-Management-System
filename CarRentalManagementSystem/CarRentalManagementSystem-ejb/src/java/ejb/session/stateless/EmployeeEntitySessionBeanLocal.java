/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;
import entity.EmployeeEntity;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;


public interface EmployeeEntitySessionBeanLocal {
    
    Long createEmployee(Long outletId, EmployeeEntity newEmployee) throws OutletNotFoundException;
    
    EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId);
    
    void updateEmployee(EmployeeEntity employee);
    
    void deleteEmployee(Long employeeId);

    EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
    EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
}
