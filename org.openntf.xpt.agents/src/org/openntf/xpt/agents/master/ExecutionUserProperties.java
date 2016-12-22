/*
 * Copyright 2013, WebGate Consulting AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.xpt.agents.master;

import java.io.Serializable;
import java.util.List;

public class ExecutionUserProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean m_LoggedIn = false;
	private String m_UserName = "";
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
