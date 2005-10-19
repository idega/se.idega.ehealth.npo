/*
 * Created on 2005-maj-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;
 
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.Samtycke;
import se.carelink.webservices.npo.SamtyckeOperationEnum;
import se.carelink.webservices.npo.SamtyckeStatusEnum;
import se.carelink.webservices.npo.SamtyckeTypEnum;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;



/**
 * @author Magnus Öström
 *
 * ConsentBean contains functions regarding consent one or consent two
 */
public class ConsentBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);

	/*
	 * Private Properties
	 */
	private boolean bConsentTwo;
	private boolean bConsentOne;
	private String typeOfConsent;
	private List tempList;
	private String sTyckeOneStatus;
	private String sTyckeTwoStatus;
	private NPOReplyBean npoReplyBean;
	private NPOServicesBean npoServicesBean;
	private TicketBean ticketBean;
	private SecurityBean securityBean;
	private NpoSessionBean npoSessionBean;
	private String urlParameters;
	private String sourceDomainId;
	
	
	
	///Constructor
	public ConsentBean()
	{
		getSessionBeans();
		this.ticketBean.setSoapHeader();//set header here
	
		
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
	
	/*
	 * Getters and setters
	 */
	/**
	 * @return Returns the sourceDomainId.
	 */
	public String getsourceDomainId() {
		showConsentStatus();
		return sourceDomainId;
	}
	/**
	 * @param sourceDomainId The sourceDomainId to set.
	 */
	public void setsourceDomainId(String sourceDomainId) {
		//XmlParser xmlParser = new XmlParser();
		//this.sourceDomainId; = xmlParser.xtractSourceDomainIDFromTicket(this.ticketBean.getDecodedTicket());
	}
	/**
	 * @return Returns the bConsentOne.
	 */
	public boolean isBConsentOne() {
		return bConsentOne;
	}
	/**
	 * @param consentOne The bConsentOne to set.
	 */
	public void setBConsentOne(boolean consentOne) {
		bConsentOne = consentOne;
	}
	/**
	 * @return Returns the bConsentTwo.
	 */
	public boolean isBConsentTwo() {
		return bConsentTwo;
	}
	/**
	 * @param consentTwo The bConsentTwo to set.
	 */
	public void setBConsentTwo(boolean consentTwo) {
		bConsentTwo = consentTwo;
	}
	/**
	 * @return Returns the testConsent.
	 */
	public String getTypeOfConsent() {
		return typeOfConsent;
	}
	/**
	 * @param testConsent The testConsent to set.
	 */
	public void setTypeOfConsent(String typeOfConsent) {
		this.typeOfConsent = typeOfConsent;
	}
	 public void setNpoReplyBean(NPOReplyBean npoReplyBean){
	 	this.npoReplyBean = npoReplyBean;
	 }
	 public NPOReplyBean getNpoReplyBean(){
	 	return npoReplyBean;
	 }
	 
	 /**
		 * @return Returns the sTyckeOneStatus.
		 */
		public String getsTyckeOneStatus() {
			return sTyckeOneStatus;
		}
		/**
		 * @param tyckeOneStatus The sTyckeOneStatus to set.
		 */
		public void setsTyckeOneStatus(String tyckeOneStatus) {
			sTyckeOneStatus = tyckeOneStatus;
		}
		/**
		 * @return Returns the sTyckeTwoStatus.
		 */
		public String getsTyckeTwoStatus() {
			return sTyckeTwoStatus;
		}
		/**
		 * @param tyckeTwoStatus The sTyckeTwoStatus to set.
		 */
		public void setsTyckeTwoStatus(String tyckeTwoStatus) {
			sTyckeTwoStatus = tyckeTwoStatus;
		}
	 
	 /**
	  * Public Methods
	  */	 
	
	/**
	 * get the consent2 from the provider and store it in this session
	 */
	public boolean fetchConsentTwo(String pID){
		try{
			this.getNpoReplyBean().setHamtaSamtyckeReply(npoServicesBean.getNpoServiceSoap().hamtaVardrelation(pID));
			}
		catch(RemoteException re){
			log.warn("RemoteException",re);
			return false;
		}
		return true;
	}
	public String handleSamtyckeEtt(){
		return "samett";
	}
	
	/**
	 * get the consent1 from the provider and store it in this session
	 */
	public boolean fetchConsentOne(String pID,String huvudman){
		
		try{
			this.getNpoReplyBean().setHamtaSamtyckeReply(npoServicesBean.getNpoServiceSoap().hamtaSamtycke1(pID,huvudman));
			}
		catch(RemoteException re){
            log.warn("RemoteException",re);
			return false;
		}
		return true; 
	}
	public String getSourceDomainFromTicket(){
		return "";
	}
	
	/**
	 * Register ConsentOK
	 * @param pID
	 * @param consentType
	 * @return s_ok on Ok or err on  error
	 */
	public String registerConsentOK(String pID,SamtyckeTypEnum consentType,String subType){
		try{
		this.getNpoReplyBean().setNPOMessageReply(npoServicesBean.getNpoServiceSoap().registreraSamtycke(pID,consentType,subType,SamtyckeOperationEnum.GeSamtycke));
		}
		catch(RemoteException re){
            log.error("RemoteException",re);
            throw new InfrastructureException("Samtycke_ok is not responding",re);
		}
		return "s_ok";
	}
	
	/**
	 * registers a consent
	 * @param pID
	 * @param consentType
	 * @param SamtyckeOperation
	 * @return err on error s_ok on ok.
	 */
	public String registerConsent(String pID,SamtyckeTypEnum consentType, SamtyckeOperationEnum SamtyckeOperation,String subType){
		try{
		this.getNpoReplyBean().setNPOMessageReply(npoServicesBean.getNpoServiceSoap().registreraSamtycke(pID,consentType,subType,SamtyckeOperation));
		}
		catch(RemoteException re){
            log.error("Samtycke is not responding",re);
            throw new InfrastructureException("Samtycke is not responding",re);
           
		}
		if(this.npoReplyBean.isHasErrorCode()){
			log.debug("major" + this.npoReplyBean.getmajor());
			log.debug("minor" + this.npoReplyBean.getminor());
			log.debug("logline" + this.npoReplyBean.getlogLineRef());
			 //throw new InfrastructureException("Samtycke returned error code");
		}
		return "s_ok";
	}
	
	/**
	 * 
	 * @param pID
	 * @param consentType
	 * @return err on error or no on ok.
	 */
	public String registerConsentNO(String pID,SamtyckeTypEnum consentType,String subType){
		try{
		this.getNpoReplyBean().setNPOMessageReply(npoServicesBean.getNpoServiceSoap().registreraSamtycke(pID,consentType,subType,SamtyckeOperationEnum.NekaSamtycke));
		}
		catch(RemoteException re){
            log.error("Samtycke is not responding",re);
            throw new InfrastructureException("Samtycke is not responding",re);
		}
		return "no";
	}
	
	/**
	 * handleConsent checks input from the user and acts accordingly
	 */
	public String handleGiveConsentTwo()
	{
		
		String Pnr =ticketBean.getinputPID();
		String retVal = null;
		
		//register consent ok
		retVal = registerConsent(Pnr,SamtyckeTypEnum.Vardrelation,SamtyckeOperationEnum.GeSamtycke,"samtycke2");
		return retVal ;
	}
	
	/**
	 * handleConsent checks input from the user and acts accordingly
	 */
	public String handleRefuseConsentTwo()
	{
		String Pnr =ticketBean.getinputPID();
		String retVal = null;
		
		//register consent ok
		retVal = registerConsentNO(Pnr,SamtyckeTypEnum.Vardrelation,"samtycke2");
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.invalidate();
		return retVal ;
	}
	
	/**
	 * Handles emergency consents, Currently not supported.
	 * @return s_ok on Ok or err on  error
	 */
	public String handleEmergencyConsent()
	{
		String Pnr = ticketBean.getinputPID();
		String retVal = null;
		//register consent ok
		retVal = registerConsent(Pnr,SamtyckeTypEnum.Vardrelation,SamtyckeOperationEnum.GeSamtycke,"nodlage");
		return retVal;	
	}
	
	/**
	 * handleConsent checks input from the user and acts accordingly
	 */
	public String handleGiveConsentOne()
	{
		String Pnr = ticketBean.getinputPID();
		String retVal = null;
		//register consent ok
		retVal = registerConsent(Pnr,SamtyckeTypEnum.Samtycke1,SamtyckeOperationEnum.GeSamtycke,"");
		return "consentTwo";	
	}
	/**
	 * handleConsent checks input from the user and acts accordingly
	 */
	public String handleRetakeConsentOne()
	{
		String Pnr = ticketBean.getinputPID();
		String retVal = null;
		//register consent ok
		retVal = registerConsent(Pnr,SamtyckeTypEnum.Samtycke1,SamtyckeOperationEnum.AngraSamtycke,"");
		return "consentTwo";	
	}
	/**
	 * handleConsent checks input from the user and acts accordingly
	 */
	public String handleGiveConsentTwoEgen()
	{
		String Pnr = ticketBean.getinputPID();
		String retVal = null;
		//register consent ok
		retVal = registerConsent(Pnr,SamtyckeTypEnum.Vardrelation,SamtyckeOperationEnum.GeSamtycke,"egen");
		return retVal;	
	}
	
	public String handleAbort(){
		return "no";
	}
	
	/** IS THIS USED??
	 * extract ticket parameters from URL and set them in the ticketBean 
	 *
	 */
	public String geturlParameters(){
		String urlParamTickeRef = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ticRef");
		String urlParamPID = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pID");
		this.ticketBean.setinputPID(urlParamPID);
		this.ticketBean.setTicketRef(urlParamTickeRef);
		return "ok";
	}
	
	/**
	 * @param urlParameters The urlParameters to set.
	 */
	public void seturlParameters(String urlParameters) {
		this.urlParameters = urlParameters;
	}
	
	/**
	 * NOT USED !!!!!
	 * 
	 * Gets the consent status from the clusters. Note that both types of consents needs to be checked
	 * since there are no guarantees that a Samtycke1 actually returns a samtycke 1. 
	 */
	public String showConsentStatus(){
		
		String msg = "";
		String samtycke = "";
		String Pnr = ticketBean.getinputPID();
		XmlParser xmlParser = new XmlParser();
		this.sourceDomainId = xmlParser.xtractSourceDomainIDFromTicket(this.ticketBean.getDecodedTicket());
		String Hvd = this.sourceDomainId;
		
		//get resource bundle
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = ResourceBundle.getBundle("se.idega.bundle.PatientlInfo",context.getViewRoot().getLocale());
		
		if(!fetchConsentOne(Pnr,Hvd))
			return null;
		Samtycke sTycke  = this.getNpoReplyBean().getHamtaSamtyckeReply().getSamtycke();
		 if ( sTycke == null )
		 	return null;
		 
		 if(sTycke.getSamtycke_typ() == SamtyckeTypEnum.Samtycke1){
				msg = bundle.getString("SamtyckeEtt");
				context.addMessage (null, new FacesMessage(msg));
				if(sTycke.getStatus() == SamtyckeStatusEnum.SamtyckeGes ){
					samtycke = "finns";
					msg = bundle.getString("SamtyckeMedgivit");
					this.setsTyckeOneStatus(samtycke);
					
				}
				else if(sTycke.getStatus() == SamtyckeStatusEnum.SamtyckeNekas ){
					samtycke = "finns inte";
					msg = bundle.getString("SamtyckeNekat");
					this.setsTyckeOneStatus(samtycke);
				}
		}
		return samtycke;	
	}
	
}
