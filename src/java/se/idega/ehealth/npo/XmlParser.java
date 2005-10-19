/*
 * Created on 2005-jul-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Attribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import org.jdom.Content;
import org.jdom.filter.*;
import org.jdom.xpath.*;

import se.idega.ehealth.npo.constants.LogDomains;

/**
 * @author Administratör
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlParser {
    
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
	
	private String lakemedelsLista ;
	private Document internLista;
	private String ticketInformation;
	/**
	 * @return Returns the lakemedelsLista.
	 */
	public String getLakemedelsLista() {
		return lakemedelsLista;
	}
	/**
	 * @param lakemedelsLista The lakemedelsLista to set.
	 */
	public void setLakemedelsLista(String lakemedelsLista) {
		this.lakemedelsLista = lakemedelsLista;
	}
	public XmlParser(){
		
		lakemedelsLista = "" ;
	}
	
	public void mergeNameInXML(String xml){
		String output = "";
		XMLOutputter xmlOutputter = new XMLOutputter();
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		List nodeList = null;
		try{
		 doc =  builder.build(new StringReader(xml));
		}
		catch(Exception ee){
		    log.warn("Exception",ee);
		    return ;
		}	
		Element root = doc.getRootElement();
		List namedChildren = root.getContent(new ElementFilter("NPORapportDel"));
		Iterator itr = namedChildren.listIterator();
		while(itr.hasNext()){
			Element el = (Element)itr.next();
			lakemedelsLista += xmlOutputter.outputString(el);
		}
	}
	
	public String addRoottElement(String element,String xmlAsString){
		xmlAsString = "<LakLista>"+xmlAsString+"</LakLista>";
		return xmlAsString;
	}
	
	/**
	 * Method to strip out an enclosing CDATA block
	 * @param xmlContent XML Content with (possible) surrounding CDATA
	 * @return a XML document without the CDATA
	 */
	public static String stripCdata(String xmlContent) {
		if(xmlContent!=null){
			String CDATABEGIN="<![CDATA[";
			String CDATAEND="]]>";
			if(xmlContent.startsWith(CDATABEGIN)){
				String content1 = xmlContent.substring(CDATABEGIN.length());
				String returnXML = content1.substring(0,content1.lastIndexOf(CDATAEND));
				return returnXML;
			}
			
		}
		return xmlContent;
	}
	/**
	 * Method to strip out an enclosing HTML block
	 * @param xmlContent XML Content with (possible) surrounding HTML tags
	 * @return a XML document without the HTML tags
	 */
	public static String stripHtmlTagdata(String xmlContent) {
		if(xmlContent!=null){
			String CDATABEGIN="<HTML>";
			String CDATAEND="</HTML>";
			int start = xmlContent.indexOf(CDATABEGIN)+CDATABEGIN.length();
			int end = xmlContent.indexOf(CDATAEND)-CDATABEGIN.length();
			String returnHTML = xmlContent.substring(start,end);
			if(xmlContent.startsWith(CDATABEGIN)){
				String content1 = xmlContent.substring(CDATABEGIN.length());
				String returnXML = content1.substring(0,content1.lastIndexOf(CDATAEND));
				return returnXML;
			}
			
			return returnHTML;
			
		}
		return "";
	}
	
	/***
	 * Method to extract information about user type from the ticket.
	 * @param ticket
	 * @return the NPO_ROLE as string
	 */
	public String xtractNPO_TYPEFromTicket(String ticket){
		XMLOutputter xmlOutputter = new XMLOutputter();
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		List nodeList = null;
		try{
		 doc =  builder.build(new StringReader(ticket));
		}
		catch(Exception ee){
		    log.warn("Exception",ee);
		    return "";
		}	
		Element root = doc.getRootElement();
		List namedChildren =  root.getContent();
		listElements(namedChildren,"","AttributeValue");
		return ticketInformation;
	}
	/***
	 * Method to extract information about user type from the ticket.
	 * @param ticket
	 * @return the NPO_ROLE as string
	 */
	public String xtractSourceDomainIDFromTicket(String ticket){
		
		log.debug("decoded ticket");
		log.debug(ticket);
		
		XMLOutputter xmlOutputter = new XMLOutputter();
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		List nodeList = null;
		try{
		 doc =  builder.build(new StringReader(ticket));
		}
		catch(Exception ee){
		    log.warn("Exception",ee);
		    return "";
		}	
		Element root = doc.getRootElement();
		List namedChildren =  root.getContent();
		listElements(namedChildren,"","NameID");
		String toParse;
		
		log.debug("ticket information: ");
		log.debug(ticketInformation);
		
		int atIndex = ticketInformation.indexOf("@");
		toParse= ticketInformation.substring(atIndex+1,ticketInformation.length());
		log.debug("toparse: ");
		log.debug(toParse);
		return toParse;
	}
	
	/***
	 * Recursive function that traverses the entire XML tree and checks for the NPO_ROLE
	 * @param es
	 * @param indent
	 */
	private void listElements(List es, String indent,String expr) {
		  for (Iterator i = es.iterator(); i.hasNext();) {
		    Element e = (Element) i.next();
		    listElement(e, indent,expr);
		  }
		}
	
		private  void listElement(Element e, String indent,String expr) {
			log.debug(e.getName());
		  if(e.getName().equals(expr)){
		  	ticketInformation = e.getText().trim();
		  }
		  //List all children
		  List c = e.getChildren();
		  listElements(c, indent + " ",expr);
		}
}
