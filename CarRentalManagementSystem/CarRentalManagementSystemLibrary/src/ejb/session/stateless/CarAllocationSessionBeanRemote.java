/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.time.LocalDateTime;
import javax.ejb.Remote;

/**
 *
 * @author admin
 */
@Remote
public interface CarAllocationSessionBeanRemote {

    public void carAllocationTimer(LocalDateTime triggerDateTime);
}
