/*
 * Created on 2005-jul-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.xml.transform.Transformer;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.message.SOAPHeaderElement;

import se.carelink.webservices.npo.ArrayOfVD_id;

/**
 * @author Magnus Öström
 *
 * Handles the primary care page. 
 */
public class PrimaryCareBean {
	
	private String vKontaktList;
	private String oldSortOrder;
	private String sortField = "";
	private NPOReplyBean npoReplyBean;
	private NPOServicesBean npoServicesBean;
	private VardDokumentIndexBean vardDokumentIndexBean;
	private NpoSessionBean npoSessionBean;
	private TicketBean ticketBean;
	private Message msg;
	private MessageContext msgc;
	private ArrayOfVD_id v_idARR;
	private String sSOAPMessage;
	private Transformer xlstTransformer;
	private SOAPHeaderElement soapHeaderElement;
	
	public PrimaryCareBean(){
		getSessionBeans();
		vardDokumentIndexBean = (VardDokumentIndexBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GetVardDokumentIndexBean");
		if(vardDokumentIndexBean == null){
		//this should never happen!	
			return;
		}
		initXsltTransform();
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
	 * @return Returns the vKontaktList.
	 */
	public String getvKontaktList() {
		return vKontaktList;
	}
	/**
	 * @param kontaktList The vKontaktList to set.
	 */
	public void setvKontaktList(String kontaktList) {
		vKontaktList = kontaktList;
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
	 * 
	 * @param xsltParameterList
	 */
	public void xslTransform(XsltParameterList xsltParameterList){	
		if(this.npoReplyBean.hasFatalError())
			return;
		
		//if the list is null the we have a filterchange event and we must add the sort order.
		if(xsltParameterList == null){
			xsltParameterList = new XsltParameterList();
			xsltParameterList.add(new XsltParameter("sortorder",oldSortOrder));	
		}
		String xslString = npoSessionBean.getXsltTransformBean().SetSortValue(npoSessionBean.getXsltTransformBean().getXsltVKontaktFile(),"//stylesheet/xsl:template//xsl:apply-templates/xsl:sort/@select",sortField);
		vKontaktList = npoSessionBean.getXsltTransformBean().basicXSLTTransform(vardDokumentIndexBean.getSSOAPMessage(),xslString, xsltParameterList);
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
		sortField  = "cl:tizdsstampel";
		xslTransform(xsltParameterList);
	}
}
