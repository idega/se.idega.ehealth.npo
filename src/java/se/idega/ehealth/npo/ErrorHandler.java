/*
 * Created on 2005-jul-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.idega.ehealth.npo.constants.LogDomains;


/**
 * @author Magnus Öström
 *
 * 	
 *
 * Errorhandler should be used to handle all error messages.
 */
public class ErrorHandler {
	
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_WS);
	private NPOReplyBean npoReplyBean;
	private String security_minorText;
	private String security_majorText;
	private String security_logRef;
	private String log_minorText;
	private String log_majorText;
	private String log_logRef;
	private String vindex_minorText;
	private String vindex_majorText;
	private String vindex_logRef;
	private String laklist_minorText;
	private String laklist_majorText;
	private String laklist_logRef;
	private String vkontakt_minorText;
	private String vkontakt_majorText;
	private String vkontakt_logRef;
	private String majorText;
	private String minorText;
	private String logLineRef;
	private String outputMsg;
	
	public ErrorHandler(){
		
	}
	
	/**
	 * @return Returns the outputMsg.
	 */
	public String getoutputMsg() {
		return outputMsg;
	}
	/**
	 * @param outputMsg The outputMsg to set.
	 */
	public void setoutputMsg(String outputMsg) {
		this.outputMsg = outputMsg;
	}
	
	public boolean hasError(int major){
		if(major != 0)
			return true;
		return false;
	}
	public void setClearTextError(int major, int minor,String log_line_Ref, String caller){		
		
		this.logLineRef = log_line_Ref;
		
		log.debug("SCE Major:"+major+"Minor"+minor+"Caller"+caller);
		
		switch(major){
		case 0: majorText = "Inga fel (0)"; break;
		case 1: majorText = "Fel (1)"; break;
		case 2: majorText = "Delvis ok (2)"; break;
		}
		
		switch(minor)
		{
		 case 100:  minorText = "Ingen information hittades (100)"; break;
		 case 101:  minorText = "Samtycke finns inte, eller nekades (101)"; break;
		 case 102:  minorText = "Ogiltig biljett (102)"; break;
		 case 103:  minorText = "Konfigurationsfel (103)"; break;
		 case 104:  minorText = "Ogiltigt svarsmeddelande (104)"; break;
		 case 105:  minorText = "Parametrar ogiltiga eller saknas (105)"; break;
		 case 150:  minorText = "SMT-tjänst saknas för huvudman (150)"; break;
		 case 200:  minorText = "Fel i PÖS-tjänsten (200)"; break;
		 case 201:  minorText = "Fel i kommunikation med PDT (201)"; break;
		 case 202:  minorText = "Fel från alla PDT (202)"; break;
		 case 500:  minorText = "Fel i kommunikation med bakomliggande system (500)"; break;
		 case 501:  minorText = "Fel i bakomliggande system (501)"; break;
		 case 502:  minorText = "Fel i PDT-tjänst (502)"; break;
		 case 900:  minorText = "Ospecificerat fel (900)"; break;
		 default: minorText= "inga fel";
		}
		
		if(caller.equals("log")){
			log_minorText = minorText;
			log_majorText = majorText;
			log_logRef = log_line_Ref;
		}
		else if (caller.equals("security")){
			security_minorText = minorText;
			security_majorText = majorText;
			security_logRef = log_line_Ref;
		}
		else if (caller.equals("vindex")){
			vindex_minorText = minorText;
			vindex_majorText = majorText;
			vindex_logRef = log_line_Ref;
		}
		else if (caller.equals("vkontakt")){
			vkontakt_minorText = minorText;
			vkontakt_majorText = majorText;
			vkontakt_logRef = log_line_Ref;
		}
		else if (caller.equals("laklist")){
			laklist_minorText = minorText;
			laklist_majorText = majorText;
			laklist_logRef = log_line_Ref;
		}
	}
	public void logSecurityError(){
		log.debug("GetTicketByRef"+"<Major>"+security_majorText+"</Major>"+"<Minor>"+security_minorText+"</Minor>");
	}
	public void serviceResultLogger(String webserviceCall){
		log.debug(webserviceCall+"<Major>"+majorText+"</Major>"+"<Minor>"+minorText+"</Minor>"+"<LogLineRef>"+this.logLineRef+"</LogLineRef>");
	}
	
	public void createOutputMsg(int minor, int major, String log_line_ref){
		String out_msg;      
    	//create html output
        out_msg = "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='sv'>" +
                "<head><meta http-equiv='content-type' content='text/html; " +
                "charset=ISO-8859-1'/><title>Personnummer + Namn</title>" +
                "<link rel='stylesheet' type='text/css' href='../style/popup_style.css'/></head>" +
                "<body>" +
                "<div id='header'>" +
                "<img src='../style/images/logo_carelink_small.jpg' id='logo_carelink_small' alt='Carelink'/>" +
                "<div class='header-right'>" +
                "<h2>Ett fel har inträffat</h2>" +
                "</div>" +
                "</div>" +           
                "<div id='content-container'>" +
                "<h3>Det valda dokumentet kan inte visas</h3>" +
				"<hr/>"+
                "<table cellspacing='0' cellpadding='0'>" +
                "<tr>" +
                "<td><strong>Felkod major</strong></td>" +
                "<td class='last-cell'>" + major + "</td>" +
                "</tr>" +
                "<tr class='odd'>" +
                "<td><strong>Felkod minor</strong></td>" +
                "<td class='last-cell'>" + minor + "</td>" +
                "</tr>" +
                "<tr class='odd'>" +
                "<td><strong>Feltext</strong></td>" +
                "<td class='last-cell'>" + log_line_ref + "</td>" +
                "</tr>" +
                "</table>" +
                "</div>" +
                "<div id='footer'>" +
                "<p>Dokument id: 189173.2103.201 &nbsp;&nbsp;&nbsp; 4771</p>" +
                "</div></body></html>"; 
        this.setoutputMsg(out_msg);
	}
	
	/**
	 * @return Returns the vindex_logRef.
	 */
	public String getvindex_logRef() {
		return vindex_logRef;
	}
	/**
	 * @param vindex_logRef The vindex_logRef to set.
	 */
	public void setvindex_logRef(String vindex_logRef) {
		this.vindex_logRef = vindex_logRef;
	}
	/**
	 * @return Returns the vindex_majorText.
	 */
	public String getvindex_majorText() {
		return vindex_majorText;
	}
	/**
	 * @param vindex_majorText The vindex_majorText to set.
	 */
	public void setvindex_majorText(String vindex_majorText) {
		this.vindex_majorText = vindex_majorText;
	}
	/**
	 * @return Returns the vindex_minorText.
	 */
	public String getvindex_minorText() {
		return vindex_minorText;
	}
	/**
	 * @param vindex_minorText The vindex_minorText to set.
	 */
	public void setvindex_minorText(String vindex_minorText) {
		this.vindex_minorText = vindex_minorText;
	}
}
