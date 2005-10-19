/*
 * Created on 2005-jul-18
 *
 * 
 */
package se.idega.ehealth.npo;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.idega.ehealth.npo.constants.LogDomains;
/**
 * @author Magnus Öström
 * LogoutBean handles invalidation of th session and redirects the user to loggedOut.jsp
 * 
 */
public class LogoutBean {

	private String logout;
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	public LogoutBean(){
		doLogOut();
	}
	
	public void doLogOut(){
		log.debug("User is logging out");
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session.getAttribute("GetTicketBean")!= null){
			session.removeAttribute("GetTicketBean");
		}
		if(session.getAttribute("GetConsentBean")!= null){
			session.removeAttribute("GetConsentBean");
		}
		if(session.getAttribute("GetSecurityBean")!= null){
			session.removeAttribute("GetSecurityBean");
		}
		if(session.getAttribute("GetPersonalInfoBean")!= null){
			session.removeAttribute("GetPersonalInfoBean");
		}
		if(session.getAttribute("GetVardDokumentBean")!= null){
			session.removeAttribute("GetVardDokumentBean");
		}
		if(session.getAttribute("GetVardDokumentIndexBean")!= null){
			session.removeAttribute("GetVardDokumentIndexBean");
		}
		if(session.getAttribute("GetDrugsBean")!= null){
			session.removeAttribute("GetDrugsBean");
		}
		if(session.getAttribute("GetPrimaryCareBean")!= null){
			session.removeAttribute("GetPrimaryCareBean");
		}
		if(session.getAttribute("GetNpoServicesBean")!= null){
			session.removeAttribute("GetNpoServicesBean");
		}
		if(session.getAttribute("npoSessionBean")!= null){
			session.removeAttribute("npoSessionBean");
		}
		session.invalidate();
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	
		
	}
	/**
	 * @return Returns the logout.
	 */
	public String getlogout() {
		return logout;
	}
	/**
	 * @param logout The logout to set.
	 */
	public void setlogout(String logout) {
		this.logout = logout;
	}
}
