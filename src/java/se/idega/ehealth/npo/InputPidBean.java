/*
 * Created on 2005-sep-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.ehealth.npo;

import javax.faces.context.FacesContext;

/**
 * @author Magnus Öström
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InputPidBean {

	private String pid;
	/**
	 * @return Returns the pid.
	 */
	public String getpid() {
		return pid;
	}
	/**
	 * @param pid The pid to set.
	 */
	public void setpid(String pid) {
		this.pid = pid;
	}
	
	/***
	 * 
	 * @return ok when button has been pressed.
	 */
	public String inputPidEntered()
	{
		NpoSessionBean npoSessionBean = (NpoSessionBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("npoSessionBean");
		npoSessionBean.getTicketBean().setinputPID(this.pid);
		npoSessionBean.setinputPid(this.pid);
		return "ok";
	}
}
