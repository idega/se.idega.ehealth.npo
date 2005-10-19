/*
 * Created on 2005-maj-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.HamtaPersonuppgifterReply;
import se.carelink.webservices.npo.HamtaSamtyckeReply;
import se.carelink.webservices.npo.HamtaVarddokumentIndexReply;
import se.carelink.webservices.npo.HamtaVarddokumentReply;
import se.carelink.webservices.npo.NPOMessageReply;
import se.carelink.webservices.npo.ServiceResult;
import se.carelink.webservices.npo.GetSecurityServiceConfigReply;
import se.carelink.webservices.npo.HamtaPatientLogReply;
import se.carelink.webservices.npo.security.GetTicketByRefReply;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;

;

/**
 * @author Magnus Öström
 * NPOReplyBean contains methods for handling and parsing NPOMessageReplies. 
 */
public class NPOReplyBean implements NPOReply {
	
	/*
	 * Private properties
	 */
	
	private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_WS);
	private NPOMessageReply npoMessageReply;
	private ErrorHandler errorHandler;
	private String outputMessage;
	private HamtaSamtyckeReply hamtaSamtyckeReply;
	private HamtaVarddokumentIndexReply hamtaVarddokumentIndexReply;
	private HamtaVarddokumentReply hamtaVarddokumentReply;
	private HamtaPersonuppgifterReply hamtaPersonuppgifterReply;
	private GetSecurityServiceConfigReply hamtaSecurityServiceConfigReply;
	private HamtaPatientLogReply hamtaPatientLogReply;
	private boolean hasErrorCode = false; 
	private String major;
	private String minor;
	private String minorText;
	private String majorText;
	private String logLineRef;
	
	
	
	public NPOReplyBean()
	{	
		errorHandler = new ErrorHandler();
	}
	/**
	 * @return Returns the hamtaPersonuppgifterReply.
	 */
	public HamtaPersonuppgifterReply getHamtaPersonuppgifterReply() {
		return hamtaPersonuppgifterReply;
	}
	/**
	 * @param hamtaPersonuppgifterReply The hamtaPersonuppgifterReply to set.
	 */
	public void setHamtaPersonuppgifterReply(HamtaPersonuppgifterReply hamtaPersonuppgifterReply) {
		/*if(this.hamtaPersonuppgifterReply == null)
			hamtaPersonuppgifterReply = new HamtaPersonuppgifterReply();*/
		parseReplyCode(hamtaPersonuppgifterReply.getService_result(),"");
		this.errorHandler.serviceResultLogger("hamtaPersonuppgifterReply");
		this.hamtaPersonuppgifterReply = hamtaPersonuppgifterReply;
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
		parseReplyCode(hamtaSamtyckeReply.getService_result(),"");
		this.errorHandler.serviceResultLogger("hamtaSamtyckeReply");
		this.hamtaSamtyckeReply = hamtaSamtyckeReply;
	}
	/**
	 * @return Returns the hamtaVarddokumentIndexReply.
	 */
	public HamtaVarddokumentIndexReply getHamtaVarddokumentIndexReply() {
		return hamtaVarddokumentIndexReply;
	}
	/**
	 * @param hamtaVarddokumentIndexReply The hamtaVarddokumentIndexReply to set.
	 */
	public void setHamtaVarddokumentIndexReply(
			HamtaVarddokumentIndexReply hamtaVarddokumentIndexReply) {
		parseReplyCode(hamtaVarddokumentIndexReply.getService_result(),"vindex");
		this.errorHandler.serviceResultLogger("hamtaVarddokumentIndexReply");
		this.hamtaVarddokumentIndexReply = hamtaVarddokumentIndexReply;
	}
	/**
	 * @return Returns the hamtaVarddokumentReply.
	 */
	public HamtaVarddokumentReply getHamtaVarddokumentReply() {
		return hamtaVarddokumentReply;
	}
	/**
	 * @param hamtaVarddokumentReply The hamtaVarddokumentReply to set.
	 */
	public void setHamtaVarddokumentReply(HamtaVarddokumentReply hamtaVarddokumentReply) {
		parseReplyCode(hamtaVarddokumentReply.getService_result(),"");
		this.errorHandler.serviceResultLogger("hamtaVarddokumentReply");
		this.hamtaVarddokumentReply = hamtaVarddokumentReply;
	}
	/**
	 * @return Returns the npoReplyMessage.
	 */
	public NPOMessageReply getNPOMessageReply() {
		return npoMessageReply;
	}
	/**
	 * @param npoReplyMessage The npoReplyMessage to set.
	 */
	public void setNPOMessageReply(NPOMessageReply npoMessageReply) {
		this.npoMessageReply = npoMessageReply;
	}
	/**
	 * @return Returns the outputMessage.
	 */
	public String getOutputMessage() {
		return outputMessage;
	}
	/**
	 * @param outputMessage The outputMessage to set.
	 */
	public void setOutputMessage(String outputMessage) {
		this.outputMessage = outputMessage;
	}
	/**
	 * @return Returns the hamtaSecurityServiceConfigReply.
	 */
	public GetSecurityServiceConfigReply getHamtaSecurityServiceConfigReply() {
		return hamtaSecurityServiceConfigReply;
	}
	/**
	 * @param hamtaSecurityServiceConfigReply The hamtaSecurityServiceConfigReply to set.
	 */
	public void setHamtaSecurityServiceConfigReply(
		GetSecurityServiceConfigReply hamtaSecurityServiceConfigReply) {
		parseReplyCode(hamtaSecurityServiceConfigReply.getService_result(),"");
		this.errorHandler.serviceResultLogger("hamtaSecurityServiceConfigReply");
		this.hamtaSecurityServiceConfigReply = hamtaSecurityServiceConfigReply;
	}
	
	/**
	 * @return Returns the hamtaPatientLogReply.
	 */
	public HamtaPatientLogReply gethamtaPatientLogReply() {
		return hamtaPatientLogReply;
	}
	/**
	 * @param hamtaPatientLogReply The hamtaPatientLogReply to set.
	 */
	public void sethamtaPatientLogReply( HamtaPatientLogReply hamtaPatientLogReply) {
		parseReplyCode(hamtaPatientLogReply.getService_result(),"log");
		this.errorHandler.serviceResultLogger("hamtaPatientLogReply");
		this.hamtaPatientLogReply = hamtaPatientLogReply;
	}
	/**
	 * @return Returns the hasErrorCode.
	 */
	public boolean isHasErrorCode() {
		return hasErrorCode;
	}
	/**
	 * @param hasErrorCode The hasErrorCode to set.
	 */
	public void setHasErrorCode(boolean hasErrorCode) {
		this.hasErrorCode = hasErrorCode;
	}
	public boolean hasFatalError(){
	if ( isHasErrorCode()&& this.major.equals("1") ){
			return true;
		}
	return false;
			
		
	}
	
	/**
	 * parseReplyCode parses the ReplyResult found in service_result and sets the 
	 * outputmessage accordingly. 
	 * @param service_Result
	 */
	public void parseReplyCode(ServiceResult service_Result, String caller){
    	int major = service_Result.getMajor();
    	int minor = service_Result.getMinor();
    	this.logLineRef  = service_Result.getLog_line_ref();
    	this.errorHandler.createOutputMsg(minor,major,logLineRef);
    	errorHandler.setClearTextError(major,minor,logLineRef,caller);
    	//Set the globals
    	this.major =Integer.toString(major);
    	this.minor =Integer.toString(minor);
    	
    	if(major != 0 ){
    		hasErrorCode = true;
    	}
    	else
    		hasErrorCode = false; 	
	}/**
	 * parseReplyCode parses the ReplyResult found in NPO Security service_result and sets the 
	 * outputmessage accordingly. 
	 * @param service_Result
	 */
	public void parseReplyCode(se.carelink.webservices.npo.security.ServiceResult service_Result){
    	int major = service_Result.getMajor();
    	int minor = service_Result.getMinor();
    	this.logLineRef  = service_Result.getLog_line_ref();
    	this.errorHandler.createOutputMsg(minor,major,logLineRef);
    	errorHandler.setClearTextError(major,minor,logLineRef,"security");
    	//Set the globals
    	this.major =Integer.toString(major);
    	this.minor =Integer.toString(minor); 	
    	
    	
    	if(major != 0 ){
    		hasErrorCode = true;
    	}
    	else
    		hasErrorCode = false; 
 
	}
	
	public void getClearTextError(int major, int minor){
		
		
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
	}
	
	public void serviceResultLogger(String webserviceCall){
		log.debug(webserviceCall+"<Major>"+majorText+"</Major>"+"<Minor>"+minorText+"</Minor>"+"<LogLineRef>"+this.logLineRef+"</LogLineRef>");
	}
	/**
	 * checks the result of the webservice call.
	 * @param npoReplyBean
	 * @param content
	 * @return
	 */
	public boolean checkResult(String content){
		if(this.hasErrorCode){
		//if there is an error but still has content, show what we have.
		if (content != null){
				return true;
		}
	}
	else{
		return false;
	}
		return true;	
 	}
	
	
	/**
	 * @return Returns the logLineRef.
	 */
	public String getlogLineRef() {
		return logLineRef;
	}
	/**
	 * @param logLineRef The logLineRef to set.
	 */
	public void setlogLineRef(String logLineRef) {
		this.logLineRef = logLineRef;
	}
	/**
	 * @return Returns the major.
	 */
	public String getmajor() {
		return major;
	}
	/**
	 * @param major The major to set.
	 */
	public void setmajor(String major) {
		this.major = major;
	}
	/**
	 * @return Returns the minor.
	 */
	public String getminor() {
		return minor;
	}
	/**
	 * @param minor The minor to set.
	 */
	public void setminor(String minor) {
		this.minor = minor;
	}
	
	/**
	 * @return Returns the majorText.
	 */
	public String getmajorText() {
		return majorText;
	}
	/**
	 * @param majorText The majorText to set.
	 */
	public void setmajorText(String majorText) {
		this.majorText = majorText;
	}
	/**
	 * @return Returns the minorText.
	 */
	public String getminorText() {
		return minorText;
	}
	/**
	 * @param minorText The minorText to set.
	 */
	public void setminorText(String minorText) {
		this.minorText = minorText;
	}
	
	/**
	 * @return Returns the errorhandler.
	 */
	public ErrorHandler geterrorhandler() {
		return errorHandler;
	}
	/**
	 * @param errorhandler The errorhandler to set.
	 */
	public void seterrorhandler(ErrorHandler errorhandler) {
		this.errorHandler = errorhandler;
	}
}
