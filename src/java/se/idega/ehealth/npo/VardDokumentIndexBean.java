/*
 * Created on 2005-jul-06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIInput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.Kuvert;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;

import java.util.Map;


/**
 * @author Magnus Öström
 *
 * VardokumentIndexBean handles calls and variables related to the vardokument index.
 */
public class VardDokumentIndexBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);

	/**
	 * Private properties
	 *
	 */
	private List vardIndexList;
	private NPOReplyBean npoReplyBean;
	private NPOServicesBean npoServicesBean;
	private TicketBean ticketBean;
	private ErrorHandler errorHandler;
	private Message msg;
	private MessageContext msgc;
	private String sSOAPMessage;
	private NpoSessionBean npoSessionBean;
	private String vIndexList;
	private String xmlString;
	private String oldSortOrder;
	private List tidsFilterLista;
	private List npotypFilterLista;
	private List landstingFilterLista;
	private String currNpoTypFilter= "";
	private String currLandstingFilter= "";
	private String currTidFilter = "00000000#99999999";
	private String sortField = "";
	private boolean showError = false;
  
	/**
	 * Constructor
	 */
	public VardDokumentIndexBean(){
		errorHandler = new ErrorHandler();
		getSessionBeans();
		vardIndexList = new ArrayList();
		if(!checkStartup())
			return;
		
		if(initVardDokumentIndex())
		{
			initFilterLists();
		}
	}
	
	/**
	 * Checks if something has gone wrong during startup.
	 * @return
	 */
	public boolean checkStartup(){
		if(npoSessionBean.isErrorOnStartup()){
			vIndexList = this.npoReplyBean.geterrorhandler().getoutputMsg();
			log.debug("npoSessionBean reported error on startup:"+vIndexList);
			return false;
		}
		else
			return true;
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
	 * @return Returns the vardIndexList.
	 */
	public List getVardIndexList() {
			return vardIndexList;
	}
	/**
	 * @param vardIndexList The vardIndexList to set.
	 */
	public void setVardIndexList(List VardIndexList) {
		vardIndexList = VardIndexList;
	}
	/**
	 * @return Returns the msg.
	 */
	public Message getMsg() {
		msg = msgc.getCurrentMessage();
		return msg;
	}
	/**
	 * @param msg The msg to set.
	 */
	public void setMsg(Message msg) {
		this.msg = msg;
	}
	/**
	 * @return Returns the vIndexList.
	 */
	public String getvIndexList() {
		return vIndexList;
	}
	/**
	 * @param indexList The vIndexList to set.
	 */
	public void setvIndexList(String indexList) {
		vIndexList = indexList;
	}
	/**
	 * @return Returns the xmlString.
	 */
	public String getxmlString() {
		return xmlString;
	}
	/**
	 * @param xmlString The xmlString to set.
	 */
	public void setxmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	/**
	 * @return Returns the landstingFilterLista.
	 */
	public List getlandstingFilterLista() {
		return landstingFilterLista;
	}
	/**
	 * @param landstingFilterLista The landstingFilterLista to set.
	 */
	public void setlandstingFilterLista(List landstingFilterLista) {
		this.landstingFilterLista = landstingFilterLista;
	}
	/**
	 * @return Returns the npotypFilterLista.
	 */
	public List getnpotypFilterLista() {
		
		return npotypFilterLista;
	}
	/**
	 * @param npotypFilterLista The npotypFilterLista to set.
	 */
	public void setnpotypFilterLista(List npotypFilterLista) {
		this.npotypFilterLista = npotypFilterLista;
	}
	/**
	 * @return Returns the tidsFilterLista.
	 */
	public List gettidsFilterLista() {
		return tidsFilterLista;
	}
	/**
	 * @param tidsFilterLista The tidsFilterLista to set.
	 */
	public void settidsFilterLista(List tidsFilterLista) {
		this.tidsFilterLista = tidsFilterLista;
	}
	/**
	 * @return Returns the currLandstingFilter.
	 */
	public String getcurrLandstingFilter() {
		return currLandstingFilter;
	}
	/**
	 * @param currLandstingFilter The currLandstingFilter to set.
	 */
	public void setcurrLandstingFilter(String currLandstingFilter) {
		this.currLandstingFilter = currLandstingFilter;
	}
	/**
	 * @return Returns the currNpoTypFilter.
	 */
	public String getcurrNpoTypFilter() {
		return currNpoTypFilter;
	}
	/**
	 * @param currNpoTypFilter The currNpoTypFilter to set.
	 */
	public void setcurrNpoTypFilter(String currNpoTypFilter) {
		this.currNpoTypFilter = currNpoTypFilter;
	}
	/**
	 * @return Returns the currTidFilter.
	 */
	public String getcurrTidFilter() {
		return currTidFilter;
	}
	/**
	 * @param currTidFilter The currTidFilter to set.
	 */
	public void setcurrTidFilter(String currTidFilter) {
		this.currTidFilter = currTidFilter;
	}
	/**
	 * @return Returns the sSOAPMessage.
	 */
	public String getSSOAPMessage() {
		return sSOAPMessage;
	}
	/**
	 * @param message The sSOAPMessage to set.
	 */
	public void setSSOAPMessage(String message) {
		sSOAPMessage = message;
	}
	
	/**
	 * @return Returns the msgcontext and 
	 * Gets the SOAP message with the hamtavardokumentIndexResult and sets 
	 * sSOAPMessage. This is done because we want the intact XML structure from the SOAP Message to 
	 * be able to transform the XML with XSLT
	 * */
	public MessageContext getMsgc() {
		
		try{
			msgc = this.getNpoServicesBean().getNpoLocator().getCall().getMessageContext();		
		}
			catch( javax.xml.rpc.ServiceException re){	
				log.error("Could not get MSGContext from vindex call");
				throw new InfrastructureException("Could not get MSGContext from vindex call",re);
			}
			try{
				sSOAPMessage =  msgc.getCurrentMessage().getSOAPPartAsString();	
				log.debug("HämtaVårddokumentIndex answers: " +sSOAPMessage);
			}
			catch(RemoteException re){
                log.error("Could not get SOAP part from vindex call",re);
                throw new InfrastructureException("could not set soapMessage",re);
			}
		return msgc;
	}
	/**
	 * @param msgc The msgc to set.
	 */
	public void setMsgc(MessageContext msgc) {
		this.msgc = msgc;
	}
	
	
	/////////////////////Public methods////////////////////////

	
	/**
	 * Initializes the VardDokumentIndexReply
	 * Only run once i each session, otherwise xsltTransform will probably fail because of decaprated method in 
	 * getMsgc();
	 */
	public boolean initVardDokumentIndex(){
		String pnr = ticketBean.getinputPID();
		log.debug("PID = ");
		log.error(ticketBean.getinputPID());
		try{
			this.getNpoReplyBean().setHamtaVarddokumentIndexReply(this.npoServicesBean.getNpoServiceSoap().hamtaVarddokumentIndex(pnr));
		}
		catch (RemoteException re){
            log.error("Could not get vindex",re);
            throw new InfrastructureException("could not get vindex",re);
		}
		//Check for errors
		if(this.npoReplyBean.isHasErrorCode()){
			log.debug("initVardDokumentIndex has error :"+vIndexList);
			setShowError(true);
		}
		else{
			setShowError(false);
		}
		
		this.getMsgc();
		initXsltTransform();
		return true;
	}
	
	/**
	 * Gets handles the events given when sorting the list.
	 * @param event
	 * @throws AbortProcessingException
	 */
	public synchronized void processSortChange(ValueChangeEvent event)throws AbortProcessingException {
		XsltParameterList xsltParameterList = new XsltParameterList();
		if (null != event.getNewValue()) {
			if( event.getComponent().getId().equals("hidSortOrder")){
			xsltParameterList.add(new XsltParameter("sortorder",event.getNewValue().toString()));
			oldSortOrder = event.getNewValue().toString();
			}
			else if(event.getComponent().getId().equals("hidSortColumn")){
			sortField = event.getNewValue().toString();
			xsltParameterList.add(new XsltParameter("sortorder",oldSortOrder));	
			}
	  }
		xslTransform(xsltParameterList);
	}   
	
	/**
	 * handler when "filter" button on page is pressed
	 *
	 */
	public void filterChange(){
		xslTransform(null);
		
	}
	/**
	 * 
	 * @param xsltParameterList
	 */
	public void xslTransform(XsltParameterList xsltParameterList){
		//Return it there are no dokuments to show
		if(this.npoReplyBean.hasFatalError())
			return;
		
		//if the list is null the we have a filterchange event and we must add the sort order.
		if(xsltParameterList == null){
			xsltParameterList = new XsltParameterList();
			xsltParameterList.add(new XsltParameter("sortorder",oldSortOrder));	
		}
		xsltParameterList.add(new XsltParameter("timeFilter",currTidFilter));
		xsltParameterList.add(new XsltParameter("typFilter",currNpoTypFilter));
		xsltParameterList.add(new XsltParameter("LandstingFilter",currLandstingFilter));
		
		String xslString = npoSessionBean.getXsltTransformBean().SetSortValue(npoSessionBean.getXsltTransformBean().getXsltVIndexBasicFile(),"//stylesheet/xsl:template//xsl:apply-templates/xsl:sort/@select",sortField);
		vIndexList = npoSessionBean.getXsltTransformBean().basicXSLTTransform(sSOAPMessage,xslString, xsltParameterList);
	}
	
	/**
	 * initXsltTransform sets the default values for the transform, and calls xslTransform(XsltParameterList xsltParameterList);
	 * initXsltTransform is only called once when page is first shown.
	 *
	 */
	public void initXsltTransform(){
		XsltParameterList xsltParameterList = new XsltParameterList();
		xsltParameterList.add(new XsltParameter("sortorder","ascending"));
		oldSortOrder = "ascending";
		sortField  = "cl:tidsstampel";
		xslTransform(xsltParameterList);
	}
	
	/**
	 * initializes the values in the filter dropdown boxes.
	 *
	 */
	public void initFilterLists(){
		String first;
		String last;
		String DATE_FORMAT = "yyyyMMdd";
	    java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat(DATE_FORMAT);
		tidsFilterLista = new ArrayList();
		npotypFilterLista = new ArrayList();
		landstingFilterLista = new ArrayList();
		//Return it there are no dokuments to show
		if(this.npoReplyBean.hasFatalError()){
			log.debug("Fatalerror in initfilterlist");
		}
		else{
			//get npo_typ values from the kuverts 
			npotypFilterLista.add(new SelectItem("","Alla typer"));
			if(!this.npoReplyBean.hasFatalError()){
				List isList = new ArrayList();
					for(int i = 0;i< this.npoReplyBean.getHamtaVarddokumentIndexReply().getKuvert().getKuvert().length;i++){
						String  npotyp =  this.npoReplyBean.getHamtaVarddokumentIndexReply().getKuvert().getKuvert(i).getNpo_typ();
						if(npotyp!=null){
						if(!isList.contains(npotyp)){
							isList.add(npotyp);
						if(!npotyp.equals("FORSKRLAKRPT") && !npotyp.equals("PRIMVARDSKONTAKTRPT") && !npotyp.equals("FORSKRLAK")){	
							if(npotyp.equals("DIAGNSLUTENRPT"))
								npotypFilterLista.add(new SelectItem("DIAGNSLUTENRPT","Diagnos"));
							if(npotyp.equals("RADREPORTRPT"))
								npotypFilterLista.add(new SelectItem("RADREPORTRPT","Röntgen"));
							if(npotyp.equals("TXTJOURNRPT"))
								npotypFilterLista.add(new SelectItem("TXTJOURNRPT","Epikris"));
							if(npotyp.equals("PROVDIAGNRPT"))
								npotypFilterLista.add(new SelectItem("PROVDIAGNRPT","Labsvar"));
							//else
								//npotypFilterLista.add(new SelectItem(npotyp.toString(),npotyp.toString()));
							}
						}
						log.debug("Recived vindex envelope no:"+i);
					}
		
				}	
			}
		}
	
		//time filter values
		tidsFilterLista.add(new SelectItem("00000000#99999999","Alla tider"));
		java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT-1"));
		sdf.setTimeZone(java.util.TimeZone.getDefault()); 
		last = sdf.format(cal.getTime());
		cal.add(java.util.Calendar.WEEK_OF_MONTH,-1);
		cal.getTime();
		first = sdf.format(cal.getTime());
		tidsFilterLista.add(new SelectItem(first+"#"+last,"Senaste veckan"));
		cal.add(java.util.Calendar.MONTH,-1);
		cal.getTime();
		first = sdf.format(cal.getTime());
		tidsFilterLista.add(new SelectItem(first+"#"+last,"Senaste månaden"));
		cal.add(java.util.Calendar.YEAR,-1);
		cal.getTime();
		first = sdf.format(cal.getTime());
		tidsFilterLista.add(new SelectItem(first+"#"+last,"Senaste året"));
		
		//landstingsfilter values
		landstingFilterLista.add(new SelectItem("","Alla huvudmän"));
		landstingFilterLista.add(new SelectItem("LJL","LJL"));
		landstingFilterLista.add(new SelectItem("LiO","LiÖ"));
		landstingFilterLista.add(new SelectItem("LUL","LUL"));
		landstingFilterLista.add(new SelectItem("NLL","NLL"));	
	}
	public String gotoLak(){
		return "gotoLak";
	}
	
	/**
	 *  Returns true if an error is reported from the PÖS-service
	 */
	public boolean getShowError(){
		return showError;
	}
	public void setShowError(boolean hasError){
		showError = hasError;
	}
}
