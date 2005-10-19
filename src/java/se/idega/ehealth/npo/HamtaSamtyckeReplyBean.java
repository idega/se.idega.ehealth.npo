/*
 * Created on 2005-maj-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;
import se.carelink.webservices.npo.HamtaSamtyckeReply;
/**
 * @author Magnus Öström
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HamtaSamtyckeReplyBean extends NPOReplyBean {
		
	private HamtaSamtyckeReply hamtaSamtyckeReply;
	
	/**
	 * Constructor
	 */
	public HamtaSamtyckeReplyBean(){	
	}
	
	/**
	 * @return Returns the hamtaSamtyckeReply.
	 */
	public HamtaSamtyckeReply getHamtaSamtyckeReply() {
		return hamtaSamtyckeReply;
	}
	/**
	 * @param hamtaSamtyckeReply The hamtaSamtyckeReply to set.
	 */
	public void setHamtaSamtyckeReply(HamtaSamtyckeReply hamtaSamtyckeReply) {
		this.hamtaSamtyckeReply = hamtaSamtyckeReply;
	}
}
