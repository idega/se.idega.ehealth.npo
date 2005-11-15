package se.idega.ehealth.npo.dataprovider;

import java.rmi.RemoteException;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idega.presentation.IWContext;
import se.carelink.webservices.npo.ArrayOfKuvert;
import se.carelink.webservices.npo.HamtaVarddokumentIndexReply;
import se.carelink.webservices.npo.Kuvert;
import se.carelink.webservices.npo.NPOMessageHeader;
import se.carelink.webservices.npo.NPOServiceLocator;
import se.carelink.webservices.npo.NPOServiceSoap;
import se.carelink.webservices.npo.Personuppgifter;
import se.idega.ehealth.business.dataprovider.DataProvider;
import se.idega.ehealth.business.dataprovider.valueobj.PersonalInfo;

/**
 * 
 * <p>
 * TODO Maris_O Describe Type NPODataProvider
 * </p>
 *  Last modified: $Date: 2005/11/15 13:32:37 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris.Orbidans@idega.lv">Maris.Orbidans</a>
 * @version $Revision: 1.2 $
 */
public class NPODataProvider implements se.idega.ehealth.business.dataprovider.DataProvider
{
    private static Log log = LogFactory.getLog(NPODataProvider.class);
    
    protected NPOServiceSoap service;
    protected String context="";
    protected String ticket="";
    
    public static final String WS_ENDPOINT_ADDRESS= "NPO_WS_ENDPOINT_ADDRESS";
    
    public NPODataProvider()
    {
        try
        {
            NPOServiceLocator locator = new NPOServiceLocator();
            
            String address = readNPOEndpointAddress();
            
            if (address==null)
            {
                // for testing purposes
                address = "http://195.22.83.100:8888/axis/services/PDTSoap";
            }
            
            locator.setNPOServiceSoapEndpointAddress(address);
            service = locator.getNPOServiceSoap(); 
            setSoapHeader();
        } catch (Exception ex)
        {
            log.fatal("NPODataProvider initialization failed", ex);
        }
        
    }

    
    public void readReferalls(String personId)
    {
        PersonalInfo result = new PersonalInfo();
        try
        {
            HamtaVarddokumentIndexReply r = service.hamtaVarddokumentIndex(personId);
            ArrayOfKuvert a = r.getKuvert();
            //Kuvert[] k = r.getKuvert().getKuvert();
            int e=9;
        } catch (RemoteException ex)
        {
            log.fatal("readReferalls failed", ex);            
        }
        
    }    
    
    public PersonalInfo readPersonalInfo(String personId)
    {
        PersonalInfo result = new PersonalInfo();
        try
        {
            Personuppgifter personuppgifter = service.hamtaPersonuppgifter(personId).getPersonuppgifter();
            
            result.setAddress(personuppgifter.getUtadr());
            result.setCity(personuppgifter.getLan());
            result.setFirstname(personuppgifter.getF_namn());
            result.setMiddlename(personuppgifter.getM_namn());
            result.setPersonalId(personuppgifter.getPnr());
            result.setPostcode(personuppgifter.getPostnr());
            result.setSurname(personuppgifter.getE_namn());
        } catch (RemoteException ex)
        {
            log.fatal("readPersonalInfo failed", ex);            
        }
        
        return result;
    }
    
    private void setSoapHeader()
    {

        NPOMessageHeader npoHeader;
        npoHeader = new NPOMessageHeader();
        npoHeader.setContext(context);
        npoHeader.setTicket(ticket);
        log.debug(ticket);
        try
        {
            org.apache.axis.client.Stub s = (org.apache.axis.client.Stub) service;
            s.setHeader("http://carelink.se/webservices/npo", "NPOMessageHeader", npoHeader);
        }
        catch (Exception ex)
        {
            log.fatal("setSoapHeader failed", ex);
        }
    }
    
    private String readNPOEndpointAddress()
    {
        String result = null;
        try
        {
            FacesContext context = FacesContext.getCurrentInstance();
            IWContext iwContext = IWContext.getIWContext(context);
            result = iwContext.getIWMainApplication().getSystemProperties().getIWProperty("WS_ENDPOINT_ADDRESS").getValue();
        }
        catch (Throwable ex)
        {
            log.warn("Unable to read WS_ENDPOINT_ADDRESS from SystemProperties", ex);
        }
        return result;
    }
    
        
    
}
