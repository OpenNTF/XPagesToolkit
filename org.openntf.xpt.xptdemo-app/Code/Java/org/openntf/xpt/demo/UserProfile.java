/*
 * © Copyright WebGate Consulting AG, 2013
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
package org.openntf.xpt.demo;

import java.io.Serializable;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form="User", PrimaryFieldClass=String.class,PrimaryKeyField="UserName", View="UserByName")
public class UserProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName="UserName")
	private String m_UserName;
	@DominoEntity(FieldName="Street")
	private String m_Street;
	@DominoEntity(FieldName="Location")
	private String m_Location;
	@DominoEntity(FieldName="ZIP")
	private String m_ZIP;
	@DominoEntity(FieldName ="showWelcomeBox")
	private boolean m_ShowWelcomeBox;
	
	public void setUserName(String userName) {
		m_UserName = userName;
	}
	public String getUserName() {
		return m_UserName;
	}
	public void setStreet(String street) {
		m_Street = street;
	}
	public String getStreet() {
		return m_Street;
	}
	public void setZIP(String zIP) {
		m_ZIP = zIP;
	}
	public String getZIP() {
		return m_ZIP;
	}
	public void setLocation(String location) {
		m_Location = location;
	}
	public String getLocation() {
		return m_Location;
	}
	public void setShowWelcomeBox(boolean showWelcomeBox) {
		m_ShowWelcomeBox = showWelcomeBox;
	}
	public boolean isShowWelcomeBox() {
		return m_ShowWelcomeBox;
	}
	
}
