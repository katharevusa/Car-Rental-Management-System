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
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

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
    public Long createPartner(PartnerEntity newPartner) {
        em.persist(newPartner);
        em.flush();
        return newPartner.getPartnerId();
    }

    @Override
    public PartnerEntity retrievePartnerByPartnerId(Long partnerId) {
        PartnerEntity partner = em.find(PartnerEntity.class, partnerId);
        return partner;
    }

    @Override
    public void updatePartner(PartnerEntity partner) {
        em.merge(partner);
    }

    @Override
    public void deletePartner(Long partnerId) {
        PartnerEntity partner = retrievePartnerByPartnerId(partnerId);
        em.remove(partner);
    }

    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {

        try {
            Query query = em.createQuery("SELECT p FROM PartnerEntity p where p.username = :inUsername");
            query.setParameter("inUsername", username);

            return (PartnerEntity) query.getSingleResult();
        } catch (PersistenceException ex) {
            System.out.println("yes");
            throw new PartnerNotFoundException("Partner Username " + username + "does not exist.");
        }

    }

    @Override
    public PartnerEntity login(String username, String password) throws InvalidLoginCredentialException {

        try {
            PartnerEntity partner = retrievePartnerByUsername(username);

            if (partner.getPassword().equals(password)) {

                //to preload the assoicated entities before returning
                return partner;

            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }

        } catch (PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }
}
