/*
 * Created on 2005-jun-09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.rmi.RemoteException;
import javax.faces.context.FacesContext;
import org.apache.axis.MessageContext;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.ArrayOfVD_id;
import se.carelink.webservices.npo.VD_id;
import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;
/**
 * @author Magnus Öström
 *
 * VardDokumentBean handles initial parsing and logic when requesting Vårddokument.
 */
public class VardDokumentBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	/**
	 * Private properties
	 *
	 */
	private NPOReplyBean npoReplyBean;
	private NPOServicesBean npoServicesBean;
	private TicketBean ticketBean;
	private MessageContext msgc;
	private ArrayOfVD_id v_idARR;
	private SOAPHeaderElement soapHeaderElement;
	private NpoSessionBean npoSessionBean;
	private String vardDocumentAsXml;
	private String npoType;
	
	/**
	 * Constructor
	 */
	public VardDokumentBean(){
		getSessionBeans();
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
	 * Getters and setters
	 *
	 */
	
	/**
	 * @return Returns the vardDocumentAsXml.
	 */
	public String getvardDocumentAsXml() {
		fetchVarddokument();
		return vardDocumentAsXml;
	}
	/**
	 * @param vardDocumentAsXml The vardDocumentAsXml to set.
	 */
	public void setvardDocumentAsXml(String vardDocumentAsXml) {
		this.vardDocumentAsXml = vardDocumentAsXml;
	}
	
	/////////////////////Public methods////////////////////////

	/**
	 * Gets the vardokument, get the id from the url
	 * Checks the type o
	 */
	public void fetchVarddokument(){}
    
    /*
		String pnr = ticketBean.getinputPID();
		VD_id[] v_arrid;
		String npo_typ;
		v_arrid = new VD_id[1];
		VD_id v_id = new VD_id();
		String urlParam = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		//root and extension are separated by '-' in the id parameter
		String root = urlParam.substring(0,urlParam.indexOf("sSs"));
		String extension = urlParam.substring(urlParam.indexOf("sSs")+3,urlParam.length());
		v_id.setRoot(root);
		log.debug("Setting root: ");
		log.debug(root);
		v_id.setExtension(extension);
		log.debug("Setting extension: ");
		log.debug(extension);
		
		
		//v_id.setRoot(urlParam.substring(0,4));
		//v_id.setExtension(urlParam.substring(4));
		v_arrid[0]=v_id;
		ArrayOfVD_id vArr = new ArrayOfVD_id();
		vArr.setVD_id(v_arrid);
		
		try{
			this.npoReplyBean.setHamtaVarddokumentReply(this.npoServicesBean.getNpoServiceSoap().hamtaVarddokument(pnr,vArr));
		}
		catch (RemoteException re){
            log.error("HamtaVarddokumentReply dosent answer",re);
            throw new InfrastructureException("Exception",re);
		}
		if(this.npoReplyBean.isHasErrorCode()){
			 log.debug("HamtaVarddokumentReply returned error, showing error page");
			vardDocumentAsXml = this.npoReplyBean.geterrorhandler().getoutputMsg();
			setnpoType("");
			return ;
		}
			
		try{
		//get the type of the document we got.
		npo_typ = this.npoReplyBean.getHamtaVarddokumentReply().getKuvert().getKuvert(0).getNpo_typ();
		setnpoType(npo_typ);
		}
		catch(Exception ee){
			 log.error("Could not get the npo_type ",ee);
	            throw new InfrastructureException("Exception",ee);
		}
		
		String xmlContent = this.npoReplyBean.getHamtaVarddokumentReply().getKuvert().getKuvert(0).getContent();
		
		//this is a workaround when the document is enclosed by a <!CDATA:
		xmlContent = XmlParser.stripCdata(xmlContent);
		
		//Check for HTML in dokument, strip <HTML> tag if found.
		if(npo_typ.equalsIgnoreCase("PROVDIAGNRPT")){
			vardDocumentAsXml = this.npoSessionBean.getXsltTransformBean().basicXSLTTransform(xmlContent,this.npoSessionBean.getXsltTransformBean().getXsltSource("HTML_CHECK"),null);
			if(vardDocumentAsXml.length()> 10){
				vardDocumentAsXml = XmlParser.stripHtmlTagdata(vardDocumentAsXml);
				System.out.print(vardDocumentAsXml);
				return;
			}
				
		}
		vardDocumentAsXml = this.npoSessionBean.getXsltTransformBean().basicXSLTTransform(xmlContent,this.npoSessionBean.getXsltTransformBean().getXsltSource(npo_typ),null);

	}*/


	/**
	 * @return Returns the npoType.
	 */
	public String getnpoType() {
		return npoType;
	}
	/**
	 * @param npoType The npoType to set.
	 */
	public void setnpoType(String npoType) {
		this.npoType = npoType;
	}
}
