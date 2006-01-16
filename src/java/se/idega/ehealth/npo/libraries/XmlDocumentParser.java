package se.idega.ehealth.npo.libraries;

import java.io.StringReader;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import org.xml.sax.InputSource;
import se.idega.ehealth.npo.constants.LogDomains;

public class XmlDocumentParser
{
    private Document document;
    private String namespacePrefix;
    private String namespace;
    private static Log log = LogFactory.getLog(LogDomains.LOG_DOMAIN_GUI);
    
    public XmlDocumentParser(String contents,String namespacePrefix,String namespace)
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();            
            this.document = builder.build(new InputSource (new StringReader(stripCDATA(contents) )));
            this.namespace = namespace;
            this.namespacePrefix = namespacePrefix;
        } catch (Exception ex)
        {
            log.error("Error creating XmlDocumentParser",ex);
            System.out.println(ex.getMessage());
        }
    }
    
    public String selectNodeText(String xpathExpression)
    {
        try
        {
            XPath xpath = XPath.newInstance(xpathExpression);
            xpath.addNamespace(namespacePrefix,namespace);
            Element elem = (Element) xpath.selectSingleNode(document);            
            
            return elem == null ? null : elem.getText();
        } catch (Exception ex)
        {
            return null;
        }
    }
    
    private String stripCDATA(String document)
    {
        int index1 = document.indexOf("<![CDATA[");
        int index2 = document.indexOf("]]>");
        if (index1 != -1 && index2!=-1)
        {
            String result = document.substring(index1+9,index2); 
            return result;
        } else
        {
            return document;
        }
    }
}
