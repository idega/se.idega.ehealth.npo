/*
 * Created on 2005-sep-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.rmi.RemoteException;

import javax.faces.context.FacesContext;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.io.StringReader;
import java.io.FileReader;

import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;

/**
 * @author Magnus Öström
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoggMessageBean {
	 
	
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	private NPOReplyBean npoReplyBean;
	private NPOServicesBean npoServicesBean;
	private TicketBean ticketBean;
	private Message msg;
	private MessageContext msgc;
	private String sSOAPMessage;
	private NpoSessionBean npoSessionBean;
	private String loggList;
	
	public LoggMessageBean(){
		;
		getSessionBeans();
		if(isPatient())
		{
			getLogg();
		}
	}
	
	/**
	 * Gets all previously intanitated session-wide beans.
	 * 
	 *
	 */
	public void getSessionBeans(){
		if(npoSessionBean == null)
			npoSessionBean = (NpoSessionBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("npoSessionBean");
		if(npoServicesBean == null)
			npoServicesBean = npoSessionBean.getNpoServicesBean();
		if(ticketBean == null)
			ticketBean = npoSessionBean.getTicketBean();
		if(npoReplyBean == null)
			npoReplyBean = npoSessionBean.getNpoReplyBean();
	}
	
	public void getLogg(){
		try{
			this.npoReplyBean.sethamtaPatientLogReply(this.npoSessionBean.getNpoServicesBean().getNpoServiceSoap().hamtaPatientLog(this.npoSessionBean.getinputPid()));
		}
		catch(RemoteException re){
			log.error("hamtaPatientLog does not answer",re);
			throw new InfrastructureException("Exception",re);
		}
		//Get the soapMessage to parse
		getMsgc();
		parseLoggMessage();
	}
	
	/**
	 * @return Returns the msgcontext and 
	 * Gets the SOAP message with the hamtaLogMessageResult and sets 
	 * sSOAPMessage. This is done because we want the intact XML structure from the SOAP Message to 
	 * be able to transform the XML with XSLT
	 * */
	public MessageContext getMsgc() {
		try{
			msgc = this.npoSessionBean.getNpoServicesBean().getNpoLocator().getCall().getMessageContext();		
		}
			catch( javax.xml.rpc.ServiceException re){	
			}
			try{
				sSOAPMessage =  msgc.getCurrentMessage().getSOAPPartAsString();	
			}
			catch(RemoteException re){
				 log.error("Could not get SOAP part from log call",re);
	                throw new InfrastructureException("could not set soapMessage",re);
			}
			log.debug("hamtaPatientLog returned: " + sSOAPMessage);
		return msgc;
	}
	public void parseLoggMessage(){
		
		File fp = npoSessionBean.getXsltTransformBean().getXsltLoggFile();
		FileReader fileReader = null;
		
		try{
			fileReader = new FileReader(fp);
		}
		catch(Exception ee){
			log.error("Could not read logg xsl from file");
             throw new InfrastructureException("Could not read logg xsl from file",ee);
			
		}
		Source xsltSource = new StreamSource(fileReader);
		loggList = npoSessionBean.getXsltTransformBean().basicXSLTTransform(sSOAPMessage,xsltSource, null);
	}
	
	public boolean isPatient(){
		XmlParser xmlParser = new XmlParser();
		String npo_ROLE = xmlParser.xtractNPO_TYPEFromTicket(npoSessionBean.getTicketBean().getDecodedTicket());
		if(npo_ROLE.equals("NPO_pat")){
			return true ;
		}
		else{
			return false;
		}
	}
	
	/**
	 * @return Returns the loggList.
	 */
	public String getloggList() {
		return loggList;
	}
	/**
	 * @param loggList The loggList to set.
	 */
	public void setloggList(String loggList) {
		this.loggList = loggList;
	}
}
