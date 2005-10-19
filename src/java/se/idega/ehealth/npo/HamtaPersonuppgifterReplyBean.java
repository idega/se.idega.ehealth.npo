/*
 * Created on 2005-maj-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import se.carelink.webservices.npo.HamtaPersonuppgifterReply;

/**
 * @author Magnus Öström
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HamtaPersonuppgifterReplyBean extends NPOReplyBean{

	/**
	 * Private Properties
	 */
	private HamtaPersonuppgifterReply hamtaPersonUppgifterReply;
	
	public void HamtaPersonuppgifterReply(){
	}
	/**
	 * @return Returns the hamtaPersonUppgifterReply.
	 */
	public HamtaPersonuppgifterReply getHamtaPersonUppgifterReply() {
		return hamtaPersonUppgifterReply;
	}
	/**
	 * @param hamtaPersonUppgifterReply The hamtaPersonUppgifterReply to set.
	 */
	public void setHamtaPersonUppgifterReply(
			HamtaPersonuppgifterReply hamtaPersonUppgifterReply) {
		this.hamtaPersonUppgifterReply = hamtaPersonUppgifterReply;
	}
	
}
