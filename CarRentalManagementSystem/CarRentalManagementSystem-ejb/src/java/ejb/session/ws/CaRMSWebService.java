package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.PartnerEntity;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;

@WebService(serviceName = "CaRMSWebService")
@Stateless()

public class CaRMSWebService {

    @EJB(name = "PartnerEntitySessionBeanLocal")
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @WebMethod
    public PartnerEntity login(String username, String password) throws InvalidLoginCredentialException{
        return partnerEntitySessionBeanLocal.login(username, password);
    }
}
