/*
 * Created on 2005-apr-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.rmi.RemoteException;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.HamtaPersonuppgifterReply;
import se.carelink.webservices.npo.Personuppgifter;
import se.carelink.webservices.npo.ServiceResult;
import se.idega.ehealth.npo.constants.LogDomains;
/**
 * @author Magnus Öström
 * PersonalInfoBean handles functions which are related to 
 * a patients personal information.
 * 
 */
public class PersonalInfoBean {	
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	private Personuppgifter personalInfo;
	private NPOServicesBean npoServicesBean;
	private HamtaPersonuppgifterReply hamtaPersonUppgifterReply;
	private NpoSessionBean npoSessionBean;
	private ServiceResult serviceResult;
	private NPOReplyBean npoReplyBean;
	private TicketBean ticketBean;
	//	Personal id from user
	private String inputPID;
	
	//Constructor
	public PersonalInfoBean(){
		getSessionBeans();
		if(!this.ticketBean.isSOAPHeaderSet()){
			log.debug("Vindex setting soap header:");
			this.ticketBean.setSoapHeader();
		}
		initPersonUppgifter();
		
	}
	//Getters and setters
	public java.lang.String getInputPID() {
        return inputPID;
    }
    public void setInputPID(java.lang.String inputPID) {
        this.inputPID = inputPID;
    }
	public HamtaPersonuppgifterReply getHamtaPersonuppgifterReply() {
        return hamtaPersonUppgifterReply;
    }
    public void setHamtaPersonUppgifterReply(HamtaPersonuppgifterReply hamtaPersonUppgifterReply) {
        this.hamtaPersonUppgifterReply = hamtaPersonUppgifterReply;
    }
    public Personuppgifter getPersonalInfo() {
        return personalInfo;
    }
	 public void setPersonalInfo(Personuppgifter personalInfo) {
    	this.personalInfo = personalInfo;
    }
	 public void setNpoReplyBean(NPOReplyBean npoReplyBean){
	 	this.npoReplyBean = npoReplyBean;
	 }
	 public NPOReplyBean getNpoReplyBean(){
	 	return npoReplyBean;
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
	 
	 /**
	  * invokes initPersonUppgifter with the PID from getInputPID();
	  *	Returns string "pid _ok" on ok or "pid_unauth" on error
	  */
	 public String intitPersonUppgifterCaller()
	 {
		if(initPersonUppgifter())
			return "pid_ok";
		return "pid_unauth";
	 }

	/**
	 * Gets a HamtaPersonUppgifterReply object and extract personal info and servcice result from it
	 */
	public boolean initPersonUppgifter()
    {	
			try{
				this.setHamtaPersonUppgifterReply(this.npoServicesBean.getNpoServiceSoap().hamtaPersonuppgifter(npoSessionBean.getTicketBean().getinputPID()));
				this.setPersonalInfo(this.getHamtaPersonuppgifterReply().getPersonuppgifter());
				this.getNpoReplyBean().setHamtaPersonuppgifterReply(this.getHamtaPersonuppgifterReply());
				//this.getNpoReplyBean().parseReplyCode(this.getHamtaPersonuppgifterReply().getService_result());
				return true;
			}
			catch(RemoteException re ){
				//handle exception
                log.error("Exception",re);
			}
			return true;
    }

}
