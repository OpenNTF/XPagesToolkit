package org.openntf.xpt.agents.master;

import java.io.Serializable;
import java.util.List;

public class ExecutionUserProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean m_LoggedIn;
	private String m_UserName;
	private int m_AccessLevel;
	private List<String> m_Roles;
	public boolean isLoggedIn() {
		return m_LoggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		m_LoggedIn = loggedIn;
	}
	public String getUserName() {
		return m_UserName;
	}
	public void setUserName(String userName) {
		m_UserName = userName;
	}
	public int getAccessLevel() {
		return m_AccessLevel;
	}
	public void setAccessLevel(int accessLevel) {
		m_AccessLevel = accessLevel;
	}
	public List<String> getRoles() {
		return m_Roles;
	}
	public void setRoles(List<String> roles) {
		m_Roles = roles;
	}

}
