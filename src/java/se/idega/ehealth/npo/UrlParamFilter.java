/*
 * Created on 2005-jul-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.NPOMessageHeader;
import se.carelink.webservices.npo.SamtyckeOperationEnum;
import se.carelink.webservices.npo.SamtyckeTypEnum;
import se.idega.ehealth.npo.constants.Constants;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;
import se.idega.ehealth.npo.libraries.CommonLib;
import se.carelink.webservices.npo.NPOMessageReply;

/**
 * @author Administratör
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UrlParamFilter implements Filter {
	
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	  public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
	      throws ServletException, IOException {
		NpoSessionBean npoSessionBean;
		InputPidBean inputPidBean;
		HttpServletRequest hreq = (HttpServletRequest)request;
		HttpServletResponse hres = (HttpServletResponse)response;
		Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_WS);

		
		// MO 21.09.2005    set no-cache headers
		hres.setContentType("text/html;charset=ISO-8859-1");
		hres.setHeader("Pragma", "no-cache");
		hres.setHeader("Cache-Control", "no-cache");
		hres.setHeader("Expires", "0"); 
		String temp = hreq.getRequestURI();
		System.out.println("requested URI:" +hreq.getRequestURI());
		
		
	  	
		if(!isInitialized(hreq)){
			
			//2005-09-30 MÖ- removed, throws a nullpinter exception.
			//int timeout = Integer.parseInt(CommonLib.loadNpoProperties(this.getClass().getClassLoader()).getProperty(Constants.SESSION_TIME_OUT_PROPERTY));
			
			hreq.getSession().setMaxInactiveInterval(6000);
	  		String ticketRef = hreq.getParameter("ticRef");
		  	String inputPid  = hreq.getParameter("pID");
		    String webappRootPath = hreq.getSession().getServletContext().getRealPath("/");
		    
		    //MO 09.08.2005  send error message, if ticketRef is missing.
		  	// MÖ This also prevents restart of application when session has been invalidated
		  	// since the parameters only are available on first startup 
		    if (ticketRef==null )
            {
		    	log.warn("ticRef or pID missing");
                hres.sendError(HttpServletResponse.SC_UNAUTHORIZED,"You need to login to use NPÖ");
                return;
            }   
		    //start the npoSessionBean and get ticket.
		    npoSessionBean = new NpoSessionBean(webappRootPath,ticketRef);
		    initialize(hreq,npoSessionBean);
		    npoSessionBean.initializeNPO();
		    XmlParser xmlParser = new XmlParser();
			String npo_ROLE = xmlParser.xtractNPO_TYPEFromTicket(npoSessionBean.getTicketBean().getDecodedTicket());
			//check if ticket type is patient
			if(npo_ROLE.equals("NPO_pat")){
				npoSessionBean.setisPatient(true);
				log.debug("npo_ROLE: NPO_pat");
		  		if(inputPid == null || inputPid.length() < 10)
		  		{
		  			log.error("Error: Input pid is null or invalid");
		  			hres.sendError(HttpServletResponse.SC_FORBIDDEN);
		  		}
		  			
		  		//need to send a consent of type egen here.	
		  		npoSessionBean.getTicketBean().setinputPID(inputPid);
		  		npoSessionBean.setinputPid(inputPid);
		  		registerOwnConsent(npoSessionBean,inputPid,hres);	
		  		//Redirect
		  		hres.sendRedirect("pages/patientMain.jsf");	  		
		  	}
			else if(npo_ROLE.equals("NPO_vg")){
				
				npoSessionBean.setisPatient(false);
				
				//check if the pid is empty so that we should redirect to input page
			  	if (inputPid == null || inputPid.length() < 10 )
			  	{
			  		inputPidBean = new InputPidBean();
			  		hres.sendRedirect("pages/enterPid.jsf");
			  	}
			  	else
			  	{		  	
			  		log.debug("npo_ROLE: NPO_vg");
			  		npoSessionBean.getTicketBean().setinputPID(inputPid);
			  		npoSessionBean.setinputPid(inputPid);
			  		String newUrl = "pages/main.jsf";
			  		hres.sendRedirect(newUrl);
			  	}
			}
			else{
				//npo_ROLE in ticket has wrong type
				log.error("Error: npo_ROLE has invalid type");
				hres.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		  	
	  	}
	  	else{
	  		//just let it pass through to the underlying page who handles the url:
	  		if(hreq.getSession(false)== null){
	  			log.error("Error: Session is invalid and user tried to view a page");
				hres.sendError(HttpServletResponse.SC_UNAUTHORIZED,"You are not authorized to use NPÖ");
			}
	  		chain.doFilter(request,response);
	  	}
	  }

	  public void init(FilterConfig config)
	      throws ServletException {
	  }
	  
	  public void destroy() {}
	  
	  public boolean isInitialized(HttpServletRequest request){
	  	if(request.getSession().getAttribute("npoSessionBean") != null ){
	  		return true;
	  	}
	  	else
	  		return false;
	  }
	  
	  protected void initialize(HttpServletRequest request,NpoSessionBean npoSessionBean){
		  request.getSession().setAttribute("npoSessionBean",npoSessionBean);
		  request.getSession().setAttribute("GetNPOSessionBean",npoSessionBean);
	  }
	  
	  //Gives a consent2 with subtype "egen" when a patient enters NPÖ
	  	public void registerOwnConsent(NpoSessionBean npoSessionBean, String inputPid,HttpServletResponse hres){
	  		Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_WS);
	  		NPOMessageReply npoMsgReply;
	  		try{
	  			NPOMessageHeader npoHeader;
	  			npoHeader = new NPOMessageHeader();
	  			log.debug("header created");
	  			npoHeader.setContext(npoSessionBean.getTicketBean().getContext());
	  			log.debug("context:");
	  			log.debug(npoSessionBean.getTicketBean().getContext());
	  			npoHeader.setTicket(npoSessionBean.getTicketBean().getTicket());
	  			log.debug("ticket:");
	  			log.debug(npoSessionBean.getTicketBean().getTicket());
	  			try{
	  			org.apache.axis.client.Stub s = (org.apache.axis.client.Stub)npoSessionBean.getNpoServicesBean().getNpoServiceSoap();
	  			s.setHeader("http://carelink.se/webservices/npo","NPOMessageHeader",npoHeader);
	  			}
	  			catch(Exception ee){
	  	            log.error("Could not set Soap header",ee);
	  	            throw new InfrastructureException("Exception",ee);
	  			}
	  		
	  		}
	  		catch(Exception ee){
	  			System.out.println("URLparameter error");
	  			System.out.println(ee);
				
	  		}
	  		try{
	  		npoMsgReply = npoSessionBean.getNpoServicesBean().getNpoServiceSoap().registreraSamtycke(inputPid,SamtyckeTypEnum.Vardrelation,"egen",SamtyckeOperationEnum.GeSamtycke);
	  		}
	  		catch(Exception ee){
	  			log.error("Register Samtycke2 subtype egen is not responding");
	            throw new InfrastructureException("Samtycke is not responding",ee);
	  		}
	  		if(npoMsgReply.getService_result().getMajor() == 1){
	  			log.debug("Error when trying to register samtycke2 sub type egen"+"<Major>"
	  					+"</Major>"+npoMsgReply.getService_result().getMajor()+"<Minor>"
	  					+"</Minor>"+npoMsgReply.getService_result().getMinor());
	  			try{
	  				hres.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	  			}
	  			catch(Exception ee){
	  			}
	  	
	  			
	  		}
	  }
}

