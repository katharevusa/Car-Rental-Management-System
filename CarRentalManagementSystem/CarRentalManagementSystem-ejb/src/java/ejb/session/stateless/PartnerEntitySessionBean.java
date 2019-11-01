/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
@Local(PartnerEntitySessionBeanLocal.class)
@Remote(PartnerEntitySessionBeanRemote.class)
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createPartner(PartnerEntity newPartner){
        em.persist(newPartner);
        em.flush();       
        return newPartner.getPartnerId();
    }
    @Override
    public PartnerEntity retrievePartnerByPartnerId(Long partnerId){
        PartnerEntity partner = em.find(PartnerEntity.class, partnerId);
        return partner;
    }
    @Override
    public void updatePartner(PartnerEntity partner)
    {
        em.merge(partner);
    }
     @Override
    public void deletePartner(Long partnerId)
    {
        PartnerEntity partner = retrievePartnerByPartnerId(partnerId);
        em.remove(partner);
    }


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
