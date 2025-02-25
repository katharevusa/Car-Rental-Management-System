/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;


public interface PartnerEntitySessionBeanLocal {
    public Long createPartner(PartnerEntity newPartner);
    
    public PartnerEntity retrievePartnerByPartnerId(Long partnerId);
    
    public void updatePartner(PartnerEntity partner);
    
    public void deletePartner(Long partnerId);
    public PartnerEntity login(String username, String password) throws InvalidLoginCredentialException;
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException ;
}
