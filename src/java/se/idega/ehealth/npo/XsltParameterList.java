/*
 * Created on 2005-jul-07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import java.util.LinkedList;
/**
 * @author Magnus Öström
 *
 * XsltParameterList is a list of XsltParameters;
 */
public class XsltParameterList extends LinkedList {
	
	public XsltParameterList(){	
	}
	
	public void addParameter(XsltParameter xsltParameter){
		this.add(xsltParameter);
	}
	public void addParameter(String name, Object value){
		this.add(new XsltParameter(name,value));
	}
	public XsltParameter getParam(int index){
		return (XsltParameter)this.get(index);
	}
}
