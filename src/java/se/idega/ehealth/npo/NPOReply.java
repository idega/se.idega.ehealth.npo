/*
 * Created on 2005-maj-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import se.carelink.webservices.npo.NPOMessageReply;
import se.carelink.webservices.npo.ServiceResult;

/**
 * @author Magnus Öström
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface NPOReply {
	public abstract void setNPOMessageReply(NPOMessageReply npoReplyMessage);
	public abstract NPOMessageReply getNPOMessageReply();
	public abstract String getOutputMessage();
	public abstract void setOutputMessage(String outputmessage);
	public abstract void parseReplyCode(ServiceResult service_Result,String caller);
}