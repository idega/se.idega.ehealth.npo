/*
 * Created on 2005-jun-28
 *
 * 
 */
package se.idega.ehealth.npo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.idega.ehealth.npo.constants.LogDomains;


/**
 * @author Magnus Öström
 * InitNPOListener initializes the NpoSessionBean when a new session is created.
 * 
 */
public class InitNPOListener implements HttpSessionListener {
	
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	private NpoSessionBean npoSessionBean;
	private HttpServletRequest httpTep;
	/**
	 * @return Returns the npoSessionBean.
	 */
	public NpoSessionBean getNpoSessionBean() {
		return npoSessionBean;
	}
	/**
	 * @param npoSessionBean The npoSessionBean to set.
	 */
	public void setNpoSessionBean(NpoSessionBean npoSessionBean) {
		this.npoSessionBean = npoSessionBean;
	}
	
	/* (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {
		/*String webappRootPath = arg0.getSession().getServletContext().getRealPath("/");
		String ticketRef = (String)arg0.getSession().getAttribute("ticRef");
		String inputPid = (String)arg0.getSession().getAttribute("pID");
		npoSessionBean = new NpoSessionBean(webappRootPath,inputPid,ticketRef);
		arg0.getSession().setAttribute("npoSessionBean",npoSessionBean);
		Enumeration enum =  arg0.getSession().getAttributeNames();*/
		
		/*MessageContext context = MessageContext.getCurrentContext();
		  HttpServletRequest req = (HttpServletRequest)
		 context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);*/
	}

	/* (non-Javadoc)<c:set>
		
	</c:set>
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		npoSessionBean = (NpoSessionBean)arg0.getSession().getAttribute("npoSessionBean");
		log.debug("Session destroyed event recived");
	}

	
	
}
