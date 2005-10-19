/*
 * Created on 2005-jun-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.Call;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NodeList;

import se.carelink.webservices.npo.NPOServiceLocator;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;
import se.idega.ehealth.npo.libraries.CommonLib;

/**
 * @author Magnus Öström	
 * 
 * NpoSessionBean starts up and stores session wide objects.
 * It also initializes the 
 *
 */
public class NpoSessionBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	private NPOServicesBean npoServicesBean;
	private TicketBean ticketBean;
	private SOAPHeaderBean soapHeaderBean;
	private SecurityBean securityBean;
	private NPOReplyBean npoReplyBean;
	private ConsentBean consentBean;
	private ErrorHandler errorHandler;
	private Message msg;
	private MessageContext msgc;
	private String sSOAPMessage;
	private Call[] calls;
	private Call call;
	private NodeList nodList;
	private String hiddenString;
	private XsltTransformBean xsltTransformBean;
	private boolean errorOnStartup = false;
	private String applicationRootPath;
	private String inputPid;
	private String ticketRef;
	private boolean isPatient = false;

	private boolean isSessionAlive = true;
	
	public NpoSessionBean(String applicationRootPath, String ticketRef){
		
		File fp = new File(CommonLib.loadNpoProperties(getClass().getClassLoader()).getProperty("truststore_filepath"));
		long tmp = fp.length();
		log.debug("TrustStore FileLenght:"+tmp);
		File fpp = new File(CommonLib.loadNpoProperties(getClass().getClassLoader()).getProperty("keystore_filepath"));
		long tmpp = fp.length();
        log.debug("KeyStore FileLenght:"+tmpp);
        
        //System properties for ticket handling
		System.setProperty("javax.net.ssl.trustStoreType","JKS");
	    System.setProperty("javax.net.ssl.trustStore",CommonLib.loadNpoProperties(getClass().getClassLoader()).getProperty("truststore_filepath"));
	    System.setProperty("javax.net.ssl.trustStorePassword",CommonLib.loadNpoProperties(getClass().getClassLoader()).getProperty("truststore_password"));
	    System.setProperty("javax.net.ssl.keyStoreType","PKCS12");
	    System.setProperty("javax.net.ssl.keyStore",CommonLib.loadNpoProperties(getClass().getClassLoader()).getProperty("keystore_filepath"));
	    System.setProperty("javax.net.ssl.keyStorePassword",CommonLib.loadNpoProperties(getClass().getClassLoader()).getProperty("keystore_password"));
		
	    setApplicationRootPath(applicationRootPath);
		npoServicesBean = new NPOServicesBean();
		NPOServiceLocator serviceLocator = new NPOServiceLocator();
		String serviceUrl = getServiceUrl();
		if(serviceUrl!=null){
			serviceLocator.setNPOServiceSoapEndpointAddress(serviceUrl);
		}
		npoServicesBean.setNpoLocator(serviceLocator);
		
		npoReplyBean= new NPOReplyBean();
		errorHandler = new ErrorHandler();
		securityBean = new SecurityBean();
		ticketBean = new TicketBean(ticketRef);
		xsltTransformBean = new XsltTransformBean(getApplicationRootPath());
	}
	
    /**
     * Reads the endpoint service url from a properties file (under nposervice.properties in the current java directory) 
     * and returns it if it is set. If nothing is set it returns null.
     *
     */
    private String getServiceUrl() {
    	String url = CommonLib.loadNpoProperties(this.getClass().getClassLoader()).getProperty("nposervice_url");
    	if (url==null)
    	{
    		log.error("property nposervice_url is not set");
    		throw new InfrastructureException("property nposervice_url is not set");
    	}
		return url;
	}
	
	/**
	 * Starts a NPOReplyBean(if not already created) and calls the securityconfigreply webservice. 
	 *
	 */
	public void initializeNPO(){
		try{
		this.npoReplyBean.setHamtaSecurityServiceConfigReply(this.npoServicesBean.getNpoServiceSoap().getSecurityServiceConfig());
		}
		catch(RemoteException re){
            log.error("GetSecurityServiceConfig failed",re);
            throw new InfrastructureException("Exception",re);
		}
		if(this.npoReplyBean.isHasErrorCode()){
			log.debug("NpoSessionBean has error on startup");
			errorOnStartup = true;
			return;
		}
		//set the url to the security webservice
		this.ticketBean.setSecurityServiceID(this.ticketBean.getDecodedTickeRef());
		this.securityBean.setSecurityLoactorURL(this.npoReplyBean.getHamtaSecurityServiceConfigReply().getSecurity_config(),this.ticketBean.getSecurityServiceID());
		this.securityBean.fetchTicketByRef(this.ticketBean.getTicketRef());
		this.ticketBean.setHamtaGetTicketByRefReply(this.securityBean.getHamtaTicketByRefReply());
		this.ticketBean.setTicket(this.securityBean.extractTicketFromSOAPMsg());
		this.ticketBean.setDecodedTicket(this.ticketBean.getTicket());
		this.ticketBean.setContext(this.securityBean.extractContextFromSOAPMsg());

	}
	
	/**
	 * @return Returns the npoReplyBean.
	 */
	public NPOReplyBean getNpoReplyBean() {
		return npoReplyBean;
	}
	/**
	 * @param npoReplyBean The npoReplyBean to set.
	 */
	public void setNpoReplyBean(NPOReplyBean npoReplyBean) {
		this.npoReplyBean = npoReplyBean;
	}
	/**
	 * @return Returns the hiddenString.
	 */
	public String getHiddenString() {
		return hiddenString;
	}
	/**
	 * @param hiddenString The hiddenString to set.
	 */
	public void setHiddenString(String hiddenString) {
		this.hiddenString = hiddenString;
	}
	
	/**
	 * @return Returns the npoServicesBean.
	 */
	public NPOServicesBean getNpoServicesBean() {
		return npoServicesBean;
	}
	/**
	 * @param npoServicesBean The npoServicesBean to set.
	 */
	public void setNpoServicesBean(NPOServicesBean npoServicesBean) {
		this.npoServicesBean = npoServicesBean;
	}
	/**
	 * @return Returns the securityBean.
	 */
	public SecurityBean getSecurityBean() {
		return securityBean;
	}
	/**
	 * @param securityBean The securityBean to set.
	 */
	public void setSecurityBean(SecurityBean securityBean) {
		this.securityBean = securityBean;
	}
	/**
	 * @return Returns the soapHeaderBean.
	 */
	public SOAPHeaderBean getSoapHeaderBean() {
		return soapHeaderBean;
	}
	/**
	 * @param soapHeaderBean The soapHeaderBean to set.
	 */
	public void setSoapHeaderBean(SOAPHeaderBean soapHeaderBean) {
		this.soapHeaderBean = soapHeaderBean;
	}
	/**
	 * @return Returns the ticketBean.
	 */
	public TicketBean getTicketBean() {
		 log.error("getting ticketbean");
		return ticketBean;
	}
	/**
	 * @param ticketBean The ticketBean to set.
	 */
	public void setTicketBean(TicketBean ticketBean) {
		this.ticketBean = ticketBean;
	}
	/**
	 * @return Returns the xsltTransformBean.
	 */
	public XsltTransformBean getXsltTransformBean() {
		return xsltTransformBean;
	}
	/**
	 * @param xsltTransformBean The xsltTransformBean to set.
	 */
	public void setXsltTransformBean(XsltTransformBean xsltTransformBean) {
		this.xsltTransformBean = xsltTransformBean;
	}
	
	/**
	 * Is this really used??
	 * Gets the SOAPMessage from the GetTicketByRef call and extracts the Ticket as a string.
	 * @return
	 */
	public String getMsgc() {
		
		//Cant get this to work, so im using the decaprated .getcall instead.
		/*try{
			calls = this.securityBean.getSecurityLocator().getCalls(new javax.xml.namespace.QName("http://carelink.se/webservices/npo/security", "GetTicketByRef"));		
		}
			catch( javax.xml.rpc.ServiceException re){	
			}
			try{
				sSOAPMessage =  msgc.getCurrentMessage().getSOAPPartAsString();	
			}
			catch(RemoteException re){
				re.printStackTrace();
			}*/
			try{
				msgc = this.securityBean.getSecurityLocator().getCall().getMessageContext();
			}
				catch( javax.xml.rpc.ServiceException re){
                    log.error("Could not get securitylocator call",re);
                    throw new InfrastructureException("Exception",re);
				}
				try{
					nodList =  msgc.getCurrentMessage().getSOAPPart().getDocumentElement().getElementsByTagName("ticket");
				}
				catch(Exception re){
                    log.error("Could not get securitylocator SOAP part of call",re);
                    throw new InfrastructureException("Exception",re);
				}
				for(int i = 0; i< nodList.getLength(); i++){
					
				}
				try{
					sSOAPMessage = nodList.item(0).getFirstChild().getNodeValue();
				}
				catch(Exception ee){
                    log.error("Securitylocator error in getMsgc ",ee);
                    throw new InfrastructureException("Exception",ee);
				}
		return sSOAPMessage;
	}
	
	/**
	 * invalidate session and logout.
	 *
	 */
	public void logout(){
		
	}
	
	/**
	 * @return Returns the errorOnStartup.
	 */
	public boolean isErrorOnStartup() {
		return errorOnStartup;
	}
	/**
	 * @param errorOnStartup The errorOnStartup to set.
	 */
	public void setErrorOnStartup(boolean errorOnStartup) {
		this.errorOnStartup = errorOnStartup;
	}
	/**
	 * @return Returns the applicationRootPath.
	 */
	public String getApplicationRootPath() {
		return applicationRootPath;
	}
	/**
	 * @param applicationRootPath The applicationRootPath to set.
	 */
	public void setApplicationRootPath(String applicationRootPath) {
		this.applicationRootPath = applicationRootPath;
	}
	/**
	 * @return Returns the inputPid.
	 */
	public String getinputPid() {
		return inputPid;
	}
	/**
	 * @param inputPid The inputPid to set.
	 */
	public void setinputPid(String inputPid) {
		this.inputPid = inputPid;
	}
	/**
	 * @return Returns the isSessionAlive.
	 */
	public boolean isSessionAlive() {
		return isSessionAlive;
	}
	/**
	 * @param isSessionAlive The isSessionAlive to set.
	 */
	public void setSessionAlive(boolean isSessionAlive) {
		this.isSessionAlive = isSessionAlive;
	}
	/**
	 * @return Returns the consentBean.
	 */
	public ConsentBean getconsentBean() {
		return consentBean;
	}
	/**
	 * @param consentBean The consentBean to set.
	 */
	public void setconsentBean(ConsentBean consentBean) {
		this.consentBean = consentBean;
	}
	/**
	 *  Returns true if an error is reported from the PÖS-service
	 */
	public boolean getisPatient(){
		return isPatient;
	}
	public void setisPatient(boolean isPatient){
		this.isPatient = isPatient;
	}
}
