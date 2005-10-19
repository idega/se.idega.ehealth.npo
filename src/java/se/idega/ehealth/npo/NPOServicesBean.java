/*
 * Created on 2005-maj-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.NPOServiceLocator;
import se.carelink.webservices.npo.NPOServiceSoap;
import se.idega.ehealth.npo.constants.LogDomains;
/**
 * @author Magnus Öström
 * Keeper of the servicesoap and servicelocator object.
 */
public class NPOServicesBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	private NPOServiceSoap npoServiceSoap;
	private NPOServiceLocator npoLocator;
	
	/**
	 *Constructor
	 */
	public NPOServicesBean(){
	}
	/**
	 * @return Returns the npoLocator.
	 */
	public NPOServiceLocator getNpoLocator() {
		return npoLocator;
	}
	/**
	 * @param npoLocator The npoLocator to set.
	 */
	public void setNpoLocator(NPOServiceLocator npoLocator) {
		this.npoLocator = npoLocator;
	}
	/**
	 * @return Returns the npoServiceSoap. Retrives it if it dosen't exists.
	 */
	public NPOServiceSoap getNpoServiceSoap() {
		if(npoServiceSoap == null){
			try{
				this.setNpoServiceSoap(npoLocator.getNPOServiceSoap());
			}
			catch (javax.xml.rpc.ServiceException se){
				log.warn("Exception",se);
			}
		}
		return npoServiceSoap;
	}
	/**
	 * @param npoServiceSoap The npoServiceSoap to set.
	 */
	public void setNpoServiceSoap(NPOServiceSoap npoServiceSoap) {
		this.npoServiceSoap = npoServiceSoap;
	}
}
