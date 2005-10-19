/*
 * Created on 2005-jun-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.util.logging.Logger;

import se.carelink.webservices.npo.security.*;
import se.carelink.webservices.npo.ArrayOfSecurityServiceConfig;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;

import javax.xml.rpc.*;

import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
/**
 * @author Magnus Öström
 *
 * SecurityBean.
 */
public class SecurityBean {

	private GetTicketByRefReply hamtaTicketByRefReply;
	private NPOSecurityMessageReply npoSecurityMessageReply;
	private SecurityLocator securityLocator;
	private ErrorHandler errorHandler;
	private SecuritySoap securitySoap;
	private String securityLoactorURL;
	private ArrayOfSecurityServiceConfig arrayOfSecurityServiceConfig;
	private String securityConfigID;
	private MessageContext msgc;
	private Call call;
	private NodeList nodList;
	private String ticket;
	private String context;
	private String pID; //patient personal ID.
	
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_SECURITY);

	
	public SecurityBean(){
		this.securityLocator = new SecurityLocator();
		this.errorHandler = new ErrorHandler();
	}
	
	/**
	 * @return Returns the securityLocator.
	 */
	public SecurityLocator getSecurityLocator() {
		return securityLocator;
	}
	/**
	 * @param securityLocator The securityLocator to set.
	 */
	public void setSecurityLocator(SecurityLocator securityLocator) {
		this.securityLocator = securityLocator;
	}
	/**
	 * @return Returns the securitySoap.
	 */
	public SecuritySoap getSecuritySoap() {
		return securitySoap;
	}
	/**
	 * @param securitySoap The securitySoap to set.
	 */
	public void setSecuritySoap(SecuritySoap securitySoap) {
		this.securitySoap = securitySoap;
	}
	/**
	 * @return Returns the hamtaTicketByRefReply.
	 */
	public GetTicketByRefReply getHamtaTicketByRefReply() {
		return hamtaTicketByRefReply;
	}
	/**
	 * @param hamtaTicketByRefReply The hamtaTicketByRefReply to set.
	 */
	public void setHamtaTicketByRefReply(GetTicketByRefReply hamtaTicketByRefReply) {
		errorHandler.setClearTextError(hamtaTicketByRefReply.getService_result().getMajor(),hamtaTicketByRefReply.getService_result().getMinor(),hamtaTicketByRefReply.getService_result().getLog_line_ref(),"security");
		errorHandler.logSecurityError();
		this.hamtaTicketByRefReply = hamtaTicketByRefReply;
	}
	/**
	 * @return Returns the securityLoactorURL.
	 */
	public String getSecurityLoactorURL() {
		return securityLoactorURL;
	}
	/**
	 * @param securityLoactorURL The securityLoactorURL to set.
	 */
	public void setSecurityLoactorURL(String securityLoactorURL) {
		this.securityLoactorURL = securityLoactorURL;
	}
	/**
	 * @return Returns the securityConfigID.
	 */
	public String getSecurityConfigID() {
		return securityConfigID;
	}
	/**
	 * @param securityConfigID The securityConfigID to set.
	 */
	public void setSecurityConfigID(String securityConfigID) {
		this.securityConfigID = securityConfigID;
	}
	
	/**
	 * @param securityLoactorURL The securityLoactorURL to set.
	 */
	public void setSecurityLoactorURL(ArrayOfSecurityServiceConfig arrayOfSecurityServiceConfig,String securityConfigID) {
		for(int i = 0;i< arrayOfSecurityServiceConfig.getSecurityServiceConfig().length;i++)
		{
		 if(arrayOfSecurityServiceConfig.getSecurityServiceConfig(i).getId().equalsIgnoreCase(securityConfigID))
			securityLoactorURL = arrayOfSecurityServiceConfig.getSecurityServiceConfig(i).getSecurity_service_url();
		}
		if(securityLoactorURL==null){
			log.debug("Not Setting securityserviceurl because it is null, default='"+getSecurityLocator().getSecuritySoapAddress()+"' used");
		}
		else{
			getSecurityLocator().setSecuritySoapEndpointAddress(securityLoactorURL);
			log.debug("Setting securityserviceurl='"+securityLoactorURL+"'");
		}
	}
	
	
	/**
	 * Sets the endpoint address from inprameter and sets the SecuritySoap object 
	 * @param url
	 */
	public void startSecurityService(String url){
		//set the endpoint address from inparameter "url"
		this.getSecurityLocator().setSecuritySoapEndpointAddress(url);
		//Set SecuritySoap object
		try{
			this.setSecuritySoap(this.getSecurityLocator().getSecuritySoap());	
		}
		catch(ServiceException se){
            log.error("Could not set SecuritySoap object ",se);
           throw new InfrastructureException(se);
		}	
	}
	/**
	 * Sets the endpoint address from local string "securityLoactorURL" and sets the SecuritySoap object 
	 *
	 */
	public void startSecurityService(){
		//set the endpoint address stored in local string "securityLoactorURL";
		this.getSecurityLocator().setSecuritySoapEndpointAddress(this.getSecurityLoactorURL());
		//Set SecuritySoap object
		try{this.setSecuritySoap(this.getSecurityLocator().getSecuritySoap());	
		}
		catch(ServiceException se){
            log.error("Could not set SecuritySoap object",se);
             throw new InfrastructureException(se);
		}
	}
	
	/**
	 * fetchTicketByRef takes ticketRef and fetches the corresponding ticket.
	 * result is stored in hamtaTicketByRefReply object.
	 * @param ticketRef
	 */
	public void fetchTicketByRef(String ticketRef){
		try{
			this.setHamtaTicketByRefReply(this.securityLocator.getSecuritySoap().getTicketByRef(ticketRef));		
		}
		catch(Exception ee){
            log.error("Could not get ticket by ref",ee);
            throw new InfrastructureException("Could not get ticket by ref",ee);
		}	
		if(errorHandler.hasError(this.getHamtaTicketByRefReply().getService_result().getMajor())){
			log.error("Get ticket by Ref has service result <> 0");
            throw new InfrastructureException("Get ticket by Ref has service result <> 0");
		}
	}
	
	public String extractTicketFromSOAPMsg(){
		try{
			msgc = this.getSecurityLocator().getCall().getMessageContext();
		}
			catch( javax.xml.rpc.ServiceException re){
				log.error("extractTicketFromSOAPMsg: Could not get SOAPMessage");
               throw new InfrastructureException("Could not get SOAPMessage",re);
			}
			Element ele = null;
			//extract ticket
			try{
				nodList =  msgc.getCurrentMessage().getSOAPPart().getDocumentElement().getElementsByTagName("ticket");
				 ele = msgc.getCurrentMessage().getSOAPPart().getDocumentElement();
			}
			catch(Exception re){
                log.error("extractTicketFromSOAPMsg: Could not get ticket element");
              throw new InfrastructureException("Could not get ticket element",re);
			}
			try{
				if(nodList == null)
                    log.error("nodlist is null");
				if(nodList!=null){
					Node item1 = nodList.item(0);
					Node firstChild = item1.getFirstChild();
					ticket = firstChild.getNodeValue();
				}
			}
			catch(Exception ee){
                log.error("extractTicketFromSOAPMsg: Could not get first item in nodlist");
                throw new InfrastructureException("Could not get first item in nodlist",ee);
			}
			log.debug("Ticket:"+ticket);
			return ticket;
		}
	
	public String extractContextFromSOAPMsg(){

		try{
			msgc = this.getSecurityLocator().getCall().getMessageContext();
		}
			
			catch( javax.xml.rpc.ServiceException re){
				log.error("extractContextFromSOAPMsg: Could not get SOAPMessage");
	               throw new InfrastructureException("Could not get SOAPMessage",re);
			}
			//extract Context
			try{
				nodList =  msgc.getCurrentMessage().getSOAPPart().getDocumentElement().getElementsByTagName("context");
			}
			catch(Exception re){
                log.error("extractContextFromSOAPMsg: Could not get ticket element",re);
                throw new InfrastructureException(re);
			}
			//There are no context tag in this message.
			if(nodList.getLength() == 0)
				return context = "";
			try{
				context = nodList.item(0).getFirstChild().getNodeValue();
			}
			catch(Exception ee){
                log.error("extractContextFromSOAPMsg: Could not get first item in nodlist",ee);
                throw new InfrastructureException(ee);
			}
			log.debug("TicketContext:"+context);
			return context;
	}
	
}
