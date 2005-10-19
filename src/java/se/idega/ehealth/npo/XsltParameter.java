/*
 * Created on 2005-jul-07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

/**
 * @author Magnus Öström
 *
 * XsltParameter is a tupple of a name and a object.
 */
public class XsltParameter {
	private String name;
	private Object value;
	
	public XsltParameter(String name, Object value){
		this.name = name;
		this.value  = value;
	}
	public XsltParameter(){
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
