/*
 * Created on 2005-jun-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import se.idega.ehealth.npo.TicketBean;

/**
 * @author Magnus Öström
 * SOAPHeaderBean builds and sets the SOAP header which is used in the entire session.
 * The Axis generated java proxy crap dosen't generate any code related to SOAP headers. 
 */
public class SOAPHeaderBean {

	private String signedTicket;
	/**
	 * Constructor
	 *
	 */
	public SOAPHeaderBean(){
		
	}
	
	/**
	 * Extract the ticket information stored in TicketBean and build the header.
	 * @param pID
	 * @param ticketBean
	 */
	public void buildHeader(String pID,TicketBean ticketBean){
		
	}
	//public void buildHeader(String )
	/**
	 * 
	 *Sets the precompiled SOAPheader.
	 */
	public void setSOAPHeader(){
		
	}
}

