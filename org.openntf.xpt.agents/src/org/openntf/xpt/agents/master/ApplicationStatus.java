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
