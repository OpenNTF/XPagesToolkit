package org.openntf.xpt.agents.master;

import java.io.Serializable;

public class ApplicationStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_UserName = "";
	private boolean m_Active = false;
	private int m_LastStatus = 0;
	public String getUserName() {
		return m_UserName;
	}
	public void setUserName(String userName) {
		m_UserName = userName;
	}
	public int getLastStatus() {
		return m_LastStatus;
	}
	public void setLastStatus(int lastStatus) {
		m_LastStatus = lastStatus;
	}
	public boolean isActive() {
		return m_Active;
	}
	public void setActive(boolean active) {
		m_Active = active;
	}
	
}
