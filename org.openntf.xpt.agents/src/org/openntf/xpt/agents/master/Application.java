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

import java.util.Calendar;
import java.util.Date;

import org.apache.http.auth.UsernamePasswordCredentials;

public class Application {

	private String m_UNID;
	private String m_Path;
	private String m_UserID;
	private Date m_LastTry;
	private int m_LastStatus;
	private String m_LastReason;

	private UsernamePasswordCredentials m_Cred;

	public String getUNID() {
		return m_UNID;
	}

	public void setUNID(String uNID) {
		m_UNID = uNID;
	}

	public String getPath() {
		return m_Path;
	}

	public void setPath(String path) {
		m_Path = path;
	}

	public String getUserID() {
		return m_UserID;
	}

	public void setUserID(String userID) {
		m_UserID = userID;
	}

	public UsernamePasswordCredentials getCredentias() {
		return m_Cred;
	}

	public void setCredValues(String strUser, String password) {
		m_Cred = new UsernamePasswordCredentials(strUser, PasswordService.getInstance().decrypt(password));
	}

	public Date getLastTry() {
		return m_LastTry;
	}

	public void setLastTry(Date lastTry) {
		m_LastTry = lastTry;
	}

	public int getLastStatus() {
		return m_LastStatus;
	}

	public void setLastStatus(int lastStatus) {
		m_LastStatus = lastStatus;
	}

	public String getLastReason() {
		return m_LastReason;
	}

	public void setLastReason(String lastReason) {
		m_LastReason = lastReason;
	}
	
	public boolean isReadyToCheck() {
		
		if (m_LastStatus == 200 || m_LastTry == null) {
			return true;
		}
		Calendar calNextRun = Calendar.getInstance();
		calNextRun.setTime(m_LastTry);
		calNextRun.add(Calendar.MINUTE, 1);
		return calNextRun.getTime().before(new Date());

	}

}
