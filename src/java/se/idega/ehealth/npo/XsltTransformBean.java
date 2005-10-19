/*
 * Created on 2005-jul-04
 *
 * 
 */
package se.idega.ehealth.npo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import se.idega.ehealth.npo.constants.LogDomains;
import se.idega.ehealth.npo.exceptions.InfrastructureException;
import se.idega.ehealth.npo.libraries.CommonLib;

/**
 * @author Magnus Öström
 *
 * Handles all transformation regarding xml and xslt.
 */
public class XsltTransformBean {
	private String htmlOut;
	
	static final String TYPE_DIAGNOSE="DIAGNSLUTENRPT";
	static final String TYPE_JOURNAL="TXTJOURNRPT";
	static final String TYPE_XRAY="RADREPORTRPT";
	static final String TYPE_CONTACT="PRIMVARDSKONTAKTRPT";
	static final String TYPE_INDEX="VINDEX";
	static final String TYPE_DRUGLIST="LAKLISTA";
	static final String TYPE_PRIMCAREINDEX = "PRIMCAREINDEX";
	static final String TYPE_PROVBUNDENDIAG = "PROVDIAGNRPT";
	static final String TYPE_LOGG = "LOGG";
	static final String TYPE_HTMLCHECK = "HTML_CHECK";
	
	private String xmlOut = "";
	private String applicationRootPath;
	private Map xsltFilePathsMap;
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	public XsltTransformBean(String applicationRootPath){
		setApplicationRootPath(applicationRootPath);
	}
	
	/**
	 * Parses the string xml with xslt  
	 * @param xml as String
	 * @param xslt as String
	 * @return String parsed as defined by xslt as a string.
	 */
	public String basicXSLTTransform(String xml,String xslt,XsltParameterList xslParams ){
		String xslString;
		ByteArrayOutputStream bArrOutStream;
		bArrOutStream = new ByteArrayOutputStream();
		Source xsltSource = new StreamSource(new StringReader(xslt));
		Source xmlSource = new StreamSource(new StringReader(xml));
		TransformerFactory transFact = TransformerFactory.newInstance();
		Templates cachedXSLT = null;
		Transformer trans = null;
		try{
		 cachedXSLT = transFact.newTemplates(xsltSource);
		}
		catch(TransformerConfigurationException tce){
            log.error("Exception",tce);
            throw new InfrastructureException(tce);	
		}
		try{
		trans = cachedXSLT.newTransformer();
		}
		catch(TransformerConfigurationException tce){
            log.error("Exception",tce);
            throw new InfrastructureException(tce);
		}
		//Get xsltParameters from XsltList ,if any, and sets them 
		if (xslParams != null){
			for(int i = 0;i< xslParams.size();i++){
				trans.setParameter(xslParams.getParam(i).getName(),xslParams.getParam(i).getValue());
			}
		}
		try{
		trans.transform(xmlSource,new StreamResult(bArrOutStream));
		}
		catch(TransformerException te){
			log.error("Exception",te);
            throw new InfrastructureException(te);
		}
		htmlOut=  bArrOutStream.toString();
		return htmlOut;
	}
	/**
	 * Parses the string xml with xslt  
	 * @param xml as String
	 * @param xslt as Soruce
	 * @return String parsed as defined by xslt.
	 */
	public String basicXSLTTransform(String xml,Source xslt,XsltParameterList xslParams ){
		String xslString;
		ByteArrayOutputStream bArrOutStream;
		bArrOutStream = new ByteArrayOutputStream();
		Source xmlSource = new StreamSource(new StringReader(xml));
		TransformerFactory transFact = TransformerFactory.newInstance();
		Templates cachedXSLT = null;
		Transformer trans = null;
		try{
		 cachedXSLT = transFact.newTemplates(xslt);
		}
		catch(TransformerConfigurationException tce){
            log.error("Exception",tce);
            throw new InfrastructureException(tce);	
		}
		try{
		trans = cachedXSLT.newTransformer();
		}
		catch(TransformerConfigurationException tce){
            log.error("Exception",tce);
            throw new InfrastructureException(tce);
		}
		//Get xsltParameters from XsltList ,if any, and sets them 
		if (xslParams != null){
			for(int i = 0;i< xslParams.size();i++){
				trans.setParameter(xslParams.getParam(i).getName(),xslParams.getParam(i).getValue());
			}
		}
		
		try{
		trans.transform(xmlSource,new StreamResult(bArrOutStream));
		}
		catch(TransformerException te){
            log.error("Exception",te);
            throw new InfrastructureException(te);
		}
		htmlOut=  bArrOutStream.toString();
		return htmlOut;
	}
	
	protected void initXsltSources(){

		Map propertyFileMap = CommonLib.loadNpoProperties(this.getClass().getClassLoader());
		//Construct it here because this method is called by getXsltFilePathsMap();
		xsltFilePathsMap= new HashMap();
		Map filePaths = xsltFilePathsMap;
		
		//copy the values from the property file to the internal map:
		filePaths.putAll(propertyFileMap);
		
		//Load the default values if they are not set:
		String path = (String) filePaths.get(TYPE_INDEX);
		if(path==null){
			filePaths.put(TYPE_INDEX,"style/xslt/vindex.xsl");
		}		
		path = (String) filePaths.get(TYPE_JOURNAL);
		if(path==null){
			filePaths.put(TYPE_JOURNAL,"style/xslt/Epikriser.xsl");
		}
		path = (String) filePaths.get(TYPE_DIAGNOSE);
		if(path==null){
			filePaths.put(TYPE_DIAGNOSE,"style/xslt/Diagnoser.xsl");
		}
		path = (String) filePaths.get(TYPE_XRAY);
		if(path==null){
			filePaths.put(TYPE_XRAY,"style/xslt/RemissSvarRontgen.xsl");
		}
		path = (String) filePaths.get(TYPE_CONTACT);
		if(path==null){
			filePaths.put(TYPE_CONTACT,"style/xslt/Primarvardskontakter.xsl");										   
		}
		path = (String) filePaths.get(TYPE_DRUGLIST);
		if(path==null){
			filePaths.put(TYPE_DRUGLIST,"style/xslt/laklista.xsl");
		}	
		path = (String) filePaths.get(TYPE_PRIMCAREINDEX);
		if(path==null){
			filePaths.put(TYPE_PRIMCAREINDEX,"style/xslt/vKontaktIndex.xsl");
		}
		path = (String) filePaths.get(TYPE_PROVBUNDENDIAG);
		if(path==null){
			filePaths.put(TYPE_PROVBUNDENDIAG,"style/xslt/ProvbundenDiagnostik.xsl");
		}
		path = (String) filePaths.get(TYPE_LOGG);
		if(path==null){
			filePaths.put(TYPE_LOGG,"style/xslt/logg.xsl");
		}
		path = (String) filePaths.get(TYPE_HTMLCHECK);
		if(path==null){
			filePaths.put(TYPE_HTMLCHECK,"style/xslt/labbHtmlCheck.xsl");
		}
	}

	/**
	 * 
	 * @param xsl 
	 * @param xpath
	 * @param value
	 * @return
	 */
	public String SetSortValue(File inFile,String xpath, String value){
		
		XMLOutputter xmlOutputter = new XMLOutputter(org.jdom.output.Format.getRawFormat().setOmitDeclaration(true));
		Document xsl;
		xsl = getXmlDocument(inFile);
		List node = null;
		/*try{
			XPath.setXPathClass(org.jaxen.jdom.JDOMXPath.class);
		}
		catch(Exception ee){
            log.error("Exception",ee);
            //throw new InfrastructureException(ee);
		}*/
		
		XPath xpt = null;
		try{
			xpt = XPath.newInstance(xpath);
		}
		catch(Exception ee){
            log.error("Exception",ee);
            throw new InfrastructureException(ee);
		}
		try{
		node =(List)xpt.selectNodes(xsl);
		}
		catch(Exception ee){
            log.error("Exception",ee);
            throw new InfrastructureException(ee);
		}
		//xsl.getRootElement().setAttribute(xpath,value);
		Attribute attrib = (Attribute)node.get(0);
		attrib.setValue(value);
		xmlOut = xmlOutputter.outputString(xsl);
		return xmlOut;
	}
	/**
	 * 
	 * @param inFile
	 * @return
	 */
	public Document getXmlDocument(File inFile){
		
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try{
			 doc =  builder.build(new FileReader(inFile));
			}
			catch(Exception ee){
                log.error("Exception",ee);
                throw new InfrastructureException(ee);
			}
		return doc;
	}
	
	
	public File getXsltVIndexBasicFile() {
		return getXsltFile(TYPE_INDEX);
	}
	public File getXsltLakListaFile() {
		return getXsltFile(TYPE_DRUGLIST);
	}
	public File getXsltVKontaktFile() {
		return getXsltFile(TYPE_PRIMCAREINDEX);
	}
	public File getXsltLoggFile() {
		return getXsltFile(TYPE_LOGG);
	}

	/**
	 * @return Returns the applicationRootPath.
	 */
	public String getApplicationRootPath() {
		return applicationRootPath;
	}
	/**
	 * @param applicationRootPath The applicationRootPath to set.
	 */
	public void setApplicationRootPath(String applicationRootPath) {
		this.applicationRootPath = applicationRootPath;
	}

	/**
	 * Returns the correct XSLT File for the type.
	 * Returns null if it is not found.
	 */
	public File getXsltFile(String npo_typ) {
		
		String filePath = (String) getXsltFilePathsMap().get(npo_typ);
		if(filePath!=null){
			File file = new File(getApplicationRootPath(),filePath);
			return file;
		}
		return null;
	}
	
	/**
	 * Returns the correct XSLT document for the type.
	 * Returns null if it is not found.
	 */
	public Source getXsltSource(String npo_typ) {
		
		File file = getXsltFile(npo_typ);
		if(file!=null){
			Source source = new StreamSource(file);
			return source;
		}
		return null;
	}

	/**
	 * A Map over file paths for xslt files.
	 * File paths are as stored as Strings and their keys is the npo_types
	 * @return
	 */
	public synchronized Map getXsltFilePathsMap() {
		if(xsltFilePathsMap==null){
			initXsltSources();
		}
		return xsltFilePathsMap;
	}

	public void setXsltFileMap(Map xsltFileMap) {
		this.xsltFilePathsMap = xsltFileMap;
	}
}
