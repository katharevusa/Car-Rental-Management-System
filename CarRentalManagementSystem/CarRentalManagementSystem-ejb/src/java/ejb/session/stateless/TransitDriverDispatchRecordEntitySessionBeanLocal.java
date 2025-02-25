/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OutletEntity;
import entity.ReservationRecordEntity;
import entity.TransitDriverDispatchRecordEntity;

public interface TransitDriverDispatchRecordEntitySessionBeanLocal {

    public void createNewDispatchRecord(ReservationRecordEntity rr, TransitDriverDispatchRecordEntity newDispatchRecord);

    public TransitDriverDispatchRecordEntity retrieveDispatchRecordById(Long id);
}
