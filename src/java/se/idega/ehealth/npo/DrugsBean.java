/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.carelink.webservices.npo.ArrayOfVD_id;
import se.carelink.webservices.npo.HamtaVarddokumentReply;
import se.carelink.webservices.npo.Kuvert;
import se.carelink.webservices.npo.VD_id;
import se.idega.ehealth.npo.ErrorHandler;
import se.idega.ehealth.npo.constants.LogDomains;

/**
 * @author Magnus Öström
 *
 * DrugsBean is the backing bean for LakLista.jsp
 */
public class DrugsBean {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);

	private NPOReplyBean npoReplyBean;
	private NPOServicesBean npoServicesBean;
	private TicketBean ticketBean;
	private ErrorHandler errorHandler;
	private NpoSessionBean npoSessionBean;
	private String oldSortOrder ="";
	private VardDokumentIndexBean vardDokumentIndexBean;
	private String lakList ="";
	private String sortField= "";
	private String xmlToParse="";
	
	
	public DrugsBean(){
		errorHandler = new ErrorHandler();
		getSessionBeans();
		initDrugsList();
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
	 *
	 *
	 */
	public void initDrugsList(){
		String xmlList = "";
		XmlParser xmlParse = new XmlParser();
		vardDokumentIndexBean = (VardDokumentIndexBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("GetVardDokumentIndexBean");
		if(vardDokumentIndexBean == null){
		//this should never happen!	
		}
		if(getDrugIds())
		{
		xmlToParse = xmlParse.addRoottElement("LakLista",xmlToParse);
		//initialize sort parameters
		XsltParameterList xsltParameterList = new XsltParameterList();
		xsltParameterList.add(new XsltParameter("sortorder","ascending"));
		oldSortOrder = "ascending";
		sortField  = "//NPOForskrivetLakemedel/effectiveTime/Low/@value";
		xslTransform(xsltParameterList);
		}
		else{
			lakList = "";
		}
	}
	/**
	 * 
	 * @param vID the concatenated varddokumnet id as a string
	 * @return the content of the vårddokument
	 */
	public String fetchLakLista(ArrayOfVD_id vArr)
    
    {
        return null;
    }
    
    /*
		XmlParser xmlParse = new XmlParser();
		se.carelink.webservices.npo.ArrayOfKuvert arrKuvert; 
		String pnr = ticketBean.getinputPID();
		try{
			HamtaVarddokumentReply reply = npoServicesBean.getNpoServiceSoap().hamtaVarddokument(pnr,vArr);
			this.npoReplyBean.setHamtaVarddokumentReply(reply);
		}
		catch (RemoteException re){
            log.warn("RemoteException",re);
		}
		//handle error
		if(this.npoReplyBean.isHasErrorCode()){
			lakList = this.npoReplyBean.geterrorhandler().getoutputMsg();
			return "";
		}
		arrKuvert = this.npoReplyBean.getHamtaVarddokumentReply().getKuvert();
		for(int i= 0;i < arrKuvert.getKuvert().length;i++){
			String contentWithCdata = arrKuvert.getKuvert(i).getContent();
			String content = XmlParser.stripCdata(contentWithCdata);
			xmlParse.mergeNameInXML(content);
		}			
		xmlToParse = xmlParse.getLakemedelsLista();
		return xmlParse.getLakemedelsLista();
	}*/
	/**
	 * @return Returns the lakList.
	 */
	public String getLakList() {
		return lakList;
	}
	/**
	 * @param lakList The lakList to set.
	 */
	public void setLakList(String lakList) {
		this.lakList = lakList;
	}
	/**
	 *  get the läkemedelslistvårddokuments id from the vårdindexlist and store them in the drugDocIds
	 */
	public boolean getDrugIds()
    {
        return false;
    }
		/*
		//check for fatal error 
		 if(this.npoReplyBean.hasFatalError())
		 	return false;
		
		ArrayList arrList = new ArrayList();
		se.carelink.webservices.npo.ArrayOfKuvert arrKuvert; 
		arrKuvert = vardDokumentIndexBean.getNpoReplyBean().getHamtaVarddokumentIndexReply().getKuvert();
		for(int i = 0; i<arrKuvert.getKuvert().length;i++){
			Kuvert kuvert = arrKuvert.getKuvert(i);
			String npo_typ = kuvert.getNpo_typ();
			
			if(npo_typ!=null && npo_typ.equals("FORSKRLAKRPT")){
				arrList.add(arrKuvert.getKuvert(i).getVd_id());
			}				
		}
		//check so that there are some list to parse.
		if(arrList.size()<1){
			return false;
		}
		xmlToParse = getListor(arrList);
		return true;
	}*/
	
	/**
	 * Loops through the vid array and gets the documents
	 * @param vIds
	 */
	/*public String getListor(ArrayList vIds){	
		VD_id[] v_arrid;	
		v_arrid = new VD_id[1];
		String xmlParse = "";
		
		for(int i = 0; i< vIds.size(); i++){
			v_arrid[0]= (VD_id)vIds.get(i);
			ArrayOfVD_id vArr = new ArrayOfVD_id();
			vArr.setVD_id(v_arrid);
			xmlParse += fetchLakLista(vArr);
		}
		return xmlParse;
	}*/

	
	/**
	 * Loops through the vid array and gets the documents
	 * @param vIds
	 * MO 29.09.2005  removed for loop, now we are fetching all Varddokuments at once.
	 * MÖ 30.09.2005 rewrote since class-cast did not work
	 */
	public String getListor(ArrayList vIds)
	{
		VD_id[] idsArr = new VD_id[ vIds.size() ];
		int i=0;		// position counter
		for (Iterator iter = vIds.iterator() ; iter.hasNext() ; )
		{
			idsArr[i++] = (VD_id) iter.next();
		}
		ArrayOfVD_id vArr =  new ArrayOfVD_id( idsArr );

		return fetchLakLista(vArr);
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
		//check if list is null
		if(xsltParameterList == null){
			xsltParameterList = new XsltParameterList();
			xsltParameterList.add(new XsltParameter("sortorder",oldSortOrder));	
		}
		String xslString = npoSessionBean.getXsltTransformBean().SetSortValue(npoSessionBean.getXsltTransformBean().getXsltLakListaFile(),"//stylesheet/xsl:template//xsl:for-each/xsl:sort/@select",sortField);
		lakList = npoSessionBean.getXsltTransformBean().basicXSLTTransform(xmlToParse,xslString, xsltParameterList);
	}
	
}
