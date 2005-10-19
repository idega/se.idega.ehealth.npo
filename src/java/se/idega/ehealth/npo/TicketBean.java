/*
 * Created on 2005-apr-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;


import java.io.StringReader;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import se.carelink.webservices.npo.NPOMessageHeader;
import se.carelink.webservices.npo.security.GetTicketByRefReply;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;

/**
 * @author Magnus Öström	
 * Class handles various functions related to ticket informations and user input.
 * 
 */
public class TicketBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	///////// Private properties ///////////// 
	private String inputPID = "";
	private String ticketRef = "";
	private Base64 b64;
	private GetTicketByRefReply hamtaGetTicketByRefReply;
	private String decodedTickeRef;
	private String securityServiceID;;
	private Document XMLDoc;
	private String context;
	private String ticket;
	private NpoSessionBean npoSessionBean;
	private NPOServicesBean npoServicesBean;
	private String decodedTicket;
	private String userType; //NPO_TYPE from ticket
	private String SourceDomainId;
	private boolean isSOAPHeaderSet = false;
	
	
	
	////////Constructor////////////////
	public TicketBean(String ticRef){
		this.ticketRef = ticRef;
		b64 = new Base64();
		decodedTickeRef = new String(b64.decode(ticketRef.getBytes()));
		//sete = ticketRef.getBytes();
	}
	
	//// Getters and setters//////////
	/**
	 * Gets the Personal Id
	 */
	public java.lang.String getinputPID() {
        return inputPID;
    }

	/**
	 * Sets the personal ID
	 * @param inputPID
	 */
    public void setinputPID(java.lang.String inputPID) {
        this.inputPID = inputPID;
    }
    
    ////////Public methods/////////////
    /**
     * Checks if the entered personal is correct.
     */
    public String isPnrCorrect()
    {
    	return "pid_ok";
    	/*
      //Check PID should come from the ticket later on
		String tmpString;
		tmpString = "191212121212";
		if (this.getInputPID().equals(tmpString)){
			return "pid_ok";
		}	
		else{
			return "pid_unauth";	
		}*/
    	
    }
    public void setSecurityServiceID(String decodedTickeRef){
    	NodeList nodelist;
    	SAXBuilder saxBuilder;
    	List allChildren;
    	log.debug("decodedTickeRef "+decodedTickeRef);
    	
    	saxBuilder = new SAXBuilder();
    	try{
    	XMLDoc =  saxBuilder.build(new InputSource(new StringReader(decodedTickeRef)));
    	}
    	catch(Exception ee){
            log.error("Could not create xmldocument from decoded ticket ref",ee);
            throw new InfrastructureException(ee);
    	}
    	try{
	    	Element root = XMLDoc.getRootElement();
	    	Element child = root.getChild("SecServId");
	    	securityServiceID= child.getContent(0).getValue(); 
    	}
    	catch(Exception ee){
    		log.error("Could not get SecServId from ticketRef",ee);
            throw new InfrastructureException(ee);
    	}
   
    }
	
	/**
	 * @return Returns the hamtaGetTicketByRefReply.
	 */
	public GetTicketByRefReply getHamtaGetTicketByRefReply() {
		return hamtaGetTicketByRefReply;
	}
	/**
	 * @param hamtaGetTicketByRefReply The hamtaGetTicketByRefReply to set.
	 */
	public void setHamtaGetTicketByRefReply(
			GetTicketByRefReply hamtaGetTicketByRefReply) {
		this.hamtaGetTicketByRefReply = hamtaGetTicketByRefReply;
	}
	/**
	 * @return Returns the ticketRef.
	 */
	public String getTicketRef() {
		return ticketRef;
	}
	/**
	 * @param ticketRef The ticketRef to set.
	 */
	public void setTicketRef(String ticketRef) {
		this.ticketRef = ticketRef;
	}
	/**
	 * @return Returns the decodedTickeRef.
	 */
	public String getDecodedTickeRef() {
		return decodedTickeRef;
	}
	/**
	 * @param decodedTickeRef The decodedTickeRef to set.
	 */
	public void setDecodedTickeRef(String decodedTickeRef) {
		this.decodedTickeRef = decodedTickeRef;
	}
	/**
	 * @return Returns the securityServiceID.
	 */
	public String getSecurityServiceID() {
		return securityServiceID;
	}
	
	/**
	 * @return Returns the ticket.
	 */
	public String getTicket() {
		return ticket;
	}
	/**
	 * @param ticket The ticket to set.
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	/**
	 * @return Returns the context.
	 */
	public String getContext() {
		return context;
	}
	/**
	 * @param context The context to set.
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	/**
	 * Sets the SoapHeader with the valid and signed ticket.
	 * @param decodedSignedTicket
	 */
	public void setSoapHeader(){
		//access to NpoServiceSoap is needed. Set the session beans.
		isSOAPHeaderSet = true;
		log.debug("Setting soap header");
		setSessionBeans();
		NPOMessageHeader npoHeader;
		npoHeader = new NPOMessageHeader();
		npoHeader.setContext(this.getContext());
		npoHeader.setTicket(this.getTicket());
		log.debug("ticketbean ticket:");
		log.debug(this.getTicket());
		try{
		org.apache.axis.client.Stub s = (org.apache.axis.client.Stub)npoServicesBean.getNpoServiceSoap();
		s.setHeader("http://carelink.se/webservices/npo","NPOMessageHeader",npoHeader);
		}
		catch(Exception ee){
            log.error("Could not set Soap header",ee);
            throw new InfrastructureException("Exception",ee);
		}
	}
	/**
	 * get the session-wide beans 
	 *
	 */
	public void setSessionBeans(){
		if(npoSessionBean == null)
		npoSessionBean = (NpoSessionBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("npoSessionBean");
		if(npoServicesBean == null)
		npoServicesBean = npoSessionBean.getNpoServicesBean();
	}
	
	
	public void getUserTypeFromTicket(String ticket){
	String ticketFromFile; 
	}
	/**
	 * @return Returns the decodedTicket.
	 */
	public String getDecodedTicket() {
		return decodedTicket;
	}
	/**
	 * @param decodedTicket The decodedTicket to set.
	 */
	public void setDecodedTicket(String decodedTicket) {
		Base64 b_64 = new Base64();
		decodedTicket = new String(b64.decode(decodedTicket.getBytes()));
		this.decodedTicket = decodedTicket;
	}
	
	public void setSourceDomainId(String SourceDomainId){
		
	}
	public String getSourceDomainId(){
		return this.SourceDomainId;
		
	}
	/**
	 * @return Returns the isSOAPHeaderSet.
	 */
	public boolean isSOAPHeaderSet() {
		return isSOAPHeaderSet;
	}
	/**
	 * @param isSOAPHeaderSet The isSOAPHeaderSet to set.
	 */
	public void setSOAPHeaderSet(boolean isSOAPHeaderSet) {
		this.isSOAPHeaderSet = isSOAPHeaderSet;
	}
}
