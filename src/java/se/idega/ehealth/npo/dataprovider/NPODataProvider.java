package se.idega.ehealth.npo.dataprovider;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.xml.soap.SOAPException;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.client.Stub;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idega.presentation.IWContext;
import se.carelink.webservices.npo.HamtaVarddokumentIndexReply;
import se.carelink.webservices.npo.HamtaVarddokumentReply;
import se.carelink.webservices.npo.Kuvert;
import se.carelink.webservices.npo.PDTLocator;
import se.carelink.webservices.npo.PDTSoap;
import se.carelink.webservices.npo.Personuppgifter;
import se.carelink.webservices.npo.VD_id;
import se.idega.ehealth.business.dataprovider.DataProvider;
import se.idega.ehealth.business.dataprovider.valueobj.PersonalInfo;
import se.idega.ehealth.npo.constants.Constants;
import se.idega.ehealth.npo.libraries.XmlDocumentParser;
import se.idega.ehealth.presentation.valueobjects.ContactsValueObject;

/**
 * 
 * <p>
 * TODO Maris_O Describe Type NPODataProvider
 * </p>
 *  Last modified: $Date: 2006/01/16 09:35:49 $ by $Author: mariso $
 * 
 * @author <a href="mailto:Maris.Orbidans@idega.lv">Maris.Orbidans</a>
 * @version $Revision: 1.4 $
 */
public class NPODataProvider implements se.idega.ehealth.business.dataprovider.DataProvider
{
    private static Log log = LogFactory.getLog(NPODataProvider.class);
    
    protected PDTSoap service;
    protected String context="";
    protected String ticket="";
    
    public static final String WS_ENDPOINT_ADDRESS= "NPO_WS_ENDPOINT_ADDRESS";
    
    public NPODataProvider()
    {
        try
        {
            PDTLocator locator = new PDTLocator();
            
            String address = readNPOEndpointAddress();
            
            if (address==null)
            {
                // for testing purposes
                address = "http://195.22.83.100:8888/axis/services/PDTSoap";
                //address = "http://62.119.136.53/axis/services/PDTSoap";                
            }
            
            locator.setPDTSoapEndpointAddress(address);
            service = locator.getPDTSoap(); 
            setSoapHeader();
        } catch (Exception ex)
        {
            log.fatal("NPODataProvider initialization failed", ex);
        }
    }
    
    public Object readReferalls(String personId, String contactId)
    {
        List result = new ArrayList();
        try
        {
            HamtaVarddokumentIndexReply r = service.hamtaVarddokumentIndex(personId);
            Kuvert[] k = r.getKuvert();
            int t=0;
            t++;
        } catch (RemoteException ex)
        {
            log.fatal("readReferalls failed", ex);            
        }        
        return result;
    }     
    
    
    public void readall(String personId)
    {
        try
        {
            HamtaVarddokumentIndexReply r = service.hamtaVarddokumentIndex(personId);            
            Kuvert[] k = r.getKuvert();
                       
            List vdList = new ArrayList();
            
            for (int i=0; i<k.length ; i++)     // select indexes of proper type
            {            
                vdList.add(k[i].getVd_id());                   
            }            
            
            VD_id [] vdids = (VD_id []) vdList.toArray(new VD_id [vdList.size()]);  
            
            HamtaVarddokumentReply reply =  service.hamtaVarddokument(vdids);    
            
            Kuvert[] docs = reply.getKuvert();
                        
            
            BufferedWriter out = new BufferedWriter(new FileWriter("outfilename.txt"));
            
            for (int i=0; i<docs.length ; i++)
            {

                
                String xx = docs[0].getContent();        // read document's content as xml
                
               out.write(xx);
                
            }
            out.close();
            
        } catch (Exception ex)
        {
            log.fatal("readContacts failed", ex);            
        }
        
    }
    
    public ContactsValueObject[] readContacts(String personId)
    {
        //readall(personId);
        
        ContactsValueObject[] result = new ContactsValueObject[0];
        try
        {
            HamtaVarddokumentIndexReply r = service.hamtaVarddokumentIndex(personId);            
            Kuvert[] k = r.getKuvert();
                       
            List vdList = new ArrayList();
            
            for (int i=0; i<k.length ; i++)     // select indexes of proper type
            {
                if (k[i].getNpo_typ().equals(Constants.DOC_TYPE_CONTACTS))
                {
                    vdList.add(k[i].getVd_id());       
                }
            }            
            
            VD_id [] vdids = (VD_id []) vdList.toArray(new VD_id [vdList.size()]);  
            
            HamtaVarddokumentReply reply =  service.hamtaVarddokument(vdids);    
            
            Kuvert[] docs = reply.getKuvert();
            
            ArrayList contacts = new ArrayList();            
            
            for (int i=0; i<docs.length ; i++)
            {
                Kuvert doc = docs[i];                
                
                XmlDocumentParser parser = new XmlDocumentParser(doc.getContent(),"c","urn:carelink"); 

                String provider = parser.selectNodeText("/c:NPORapportPrimarvardsKontakt/c:NPORapportDel/c:NPOPrimarvardsKontakt/c:NPOVardkontakt/c:NPOHealthcareProfessionalRole/c:NPOPerson/c:name");
                String unit = parser.selectNodeText("/c:NPORapportPrimarvardsKontakt/c:NPORapportDel/c:NPOPrimarvardsKontakt/c:NPOVardkontakt/c:NPOHealthcareProfessionalRole/c:NPOOrganisation/c:name");
                
                if (provider ==null || unit == null) 
                {
                    continue;
                }
                
                ContactsValueObject con = new ContactsValueObject();
                con.setDate(doc.getTidsstampel().getTime());
                con.setProvider(provider);
                con.setUnit(unit);               
                
                contacts.add(con);
                
            }   
            result = (ContactsValueObject[]) contacts.toArray(new ContactsValueObject[]{});
        } catch (Exception ex)
        {
            log.fatal("readContacts failed", ex);            
        }
        return result;
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
        try
        {
            final SOAPHeaderElement npoElement = new SOAPHeaderElement("http://carelink.se/webservices/npo", "NPOMessageHeader");
            final SOAPHeaderElement ticketElement = new SOAPHeaderElement("http://carelink.se/webservices/npo", "ticket");
            
            
            npoElement.addChildElement(ticketElement);
            
            final SOAPHeaderElement contextElement = new SOAPHeaderElement("http://carelink.se/webservices/npo", "context");
            contextElement.setValue("");
            npoElement.addChildElement(contextElement);
            
            ((Stub) service).setHeader(npoElement);

        }
        catch (Exception ex)
        {
            log.fatal("setSoapHeader failed", ex);
        }

/*        
        PDTMessageHeader npoHeader;
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
        }*/
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
            log.warn("Unable to read WS_ENDPOINT_ADDRESS from SystemProperties");
        }
        return result;
    }
    
        
    
}
