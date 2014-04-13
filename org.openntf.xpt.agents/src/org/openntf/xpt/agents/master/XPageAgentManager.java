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
package org.openntf.xpt.agents.master;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.util.EntityUtils;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import org.openntf.xpt.core.utils.logging.LoggerFactory;

;

public class XPageAgentManager {

	private static final XPageAgentManager m_Manager = new XPageAgentManager();

	private Properties m_AGMRProperties;

	private MainMasterJob m_Job;

	private String m_Datapath;

	private HashMap<String, Application> m_ApplicationRegistry = new HashMap<String, Application>();

	private XPageAgentManager() {

	}

	public static XPageAgentManager getInstance() {
		return m_Manager;
	}

	public Properties getAMGRProperties() {
		if (m_AGMRProperties == null) {
			initAMGRProperties();
		}
		return m_AGMRProperties;
	}

	private void initAMGRProperties() {
		m_AGMRProperties = new Properties();
		try {
			File flCheck = new File(getDataPath() + "/xpageagent.properties");
			if (flCheck.exists()) {
				m_AGMRProperties.load(new FileReader(flCheck));
				readApplicationsFromProperties();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveAMGRProperties() {
		try {
			File flCheck = new File(getDataPath() + "/xpageagent.properties");
			m_AGMRProperties.store(new FileWriter(flCheck), "saved: " + new Date().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startJob() {
		System.out.println("CALL START JOB");
		m_Job = new MainMasterJob("XPagesAMGR");
		m_Job.schedule(1000);
	}

	public void checkTasks() {
		try {
			Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
			if (isServer()) {
				logCurrent.fine("checkTasks()");
				logCurrent.fine("AMGR Properies size:" + getAMGRProperties().keySet().size());
				for (Application app : m_ApplicationRegistry.values()) {
					if (app.isReadyToCheck()) {
						executeCheck(app);
					}
				}
			} else {
				logCurrent.info("I m not running on a Server, stopping the Master job");
				m_Job.setRunning(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executeCheck(Application app) {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		try {
			logCurrent.info("executeCheck for: " + app.getPath());
			DefaultHttpClient httpClient = new DefaultHttpClient();

			httpClient = (DefaultHttpClient) ClientSSLResistanceExtender.wrapClient(httpClient);
			httpClient.setRedirectStrategy(new DefaultRedirectStrategy());
			String strNSFURL = app.getPath();
			String strRedirection = strNSFURL + "/xsp/xpage.agent?action=check";
			HttpGet getRequestINIT = new HttpGet(strNSFURL);

			HttpGet getRequest = new HttpGet(strRedirection);
			getRequest.addHeader(BasicScheme.authenticate(app.getCredentias(), "UTF-8", false));
			getRequestINIT.addHeader(BasicScheme.authenticate(app.getCredentias(), "UTF-8", false));
			HttpResponse hsrINTI = httpClient.execute(getRequestINIT);
			app.setLastStatus(hsrINTI.getStatusLine().getStatusCode());
			app.setLastReason(hsrINTI.getStatusLine().getReasonPhrase());
			app.setLastTry(new Date());
			if (hsrINTI.getStatusLine().getStatusCode() == 200) {
				EntityUtils.consume(hsrINTI.getEntity());
				HttpResponse hsr = httpClient.execute(getRequest);
				logCurrent.fine("Result from executeCheck: " + EntityUtils.toString(hsr.getEntity()));
			} else {
				EntityUtils.consume(hsrINTI.getEntity());
				logCurrent.severe(app.getPath() + " does not response! -> " + hsrINTI.getStatusLine().toString());
			}
			logCurrent.info("executeCheck done");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getDataPath() {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		if (m_Datapath == null) {
			try {
				logCurrent.fine("Initialize the notes context");
				NotesThread.sinitThread();
				Session sesCurrent = NotesFactory.createSession();
				m_Datapath = sesCurrent.getEnvironmentString("Directory", true);
				logCurrent.fine("DIRECTORY == " + m_Datapath);
				sesCurrent.recycle();
				NotesThread.stermThread();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return m_Datapath;
	}

	public ExecutionUserProperties registerNewApplication(String strUNID, String strPath, String strUser, String strPassword) {

		ExecutionUserProperties exProp = PasswordService.getInstance().checkPassword(strUser, strPassword, strPath);
		if (exProp.isLoggedIn()) {
			Application appNew = new Application();
			if (m_ApplicationRegistry.containsKey(strUNID)) {
				appNew = m_ApplicationRegistry.get(strUNID);
			} else {
				m_ApplicationRegistry.put(strUNID, appNew);
			}
			appNew.setUNID(strUNID);
			appNew.setPath(strPath);
			appNew.setUserID(strUser);
			appNew.setCredValues(strUser, PasswordService.getInstance().encrypt(strPassword));
			pushApplication2Properties(appNew, PasswordService.getInstance().encrypt(strPassword));
		}
		return exProp;
	}

	public ApplicationStatus getApplicationStatus(String strUNID) {
		ApplicationStatus asCurrent = new ApplicationStatus();
		asCurrent.setActive(false);
		if (!m_ApplicationRegistry.containsKey(strUNID)) {
			return asCurrent;
		}
		Application app = m_ApplicationRegistry.get(strUNID);
		asCurrent.setActive(true);
		asCurrent.setUserName(app.getUserID());
		asCurrent.setLastStatus(app.getLastStatus());
		return asCurrent;
	}

	public boolean unregisterApplication(String strUNID) {
		m_ApplicationRegistry.remove(strUNID);
		m_AGMRProperties.remove(strUNID + "_PATH");
		m_AGMRProperties.remove(strUNID + "_USER");
		m_AGMRProperties.remove(strUNID + "_PW");
		saveAMGRProperties();
		return true;
	}

	private void pushApplication2Properties(Application app, String strPasword) {
		if (m_AGMRProperties == null) {
			initAMGRProperties();
		}
		String strAPPIDs = m_AGMRProperties.getProperty("apps");
		if (strAPPIDs == null || !strAPPIDs.contains(app.getUNID())) {
			if (strAPPIDs == null || "".equals(strAPPIDs)) {
				strAPPIDs = app.getUNID();
			} else {
				strAPPIDs = strAPPIDs + "," + app.getUNID();
			}
			m_AGMRProperties.setProperty("apps", strAPPIDs);
		}
		m_AGMRProperties.setProperty(app.getUNID() + "_PATH", app.getPath());
		m_AGMRProperties.setProperty(app.getUNID() + "_USER", app.getUserID());
		m_AGMRProperties.setProperty(app.getUNID() + "_PW", strPasword);
		saveAMGRProperties();
	}

	private void readApplicationsFromProperties() {
		Logger logCurrent = LoggerFactory.getLogger(getClass().getCanonicalName());
		if (m_AGMRProperties == null) {
			return;
		}
		String strAPPIDs = m_AGMRProperties.getProperty("apps");
		if (strAPPIDs == null || "".equals(strAPPIDs)) {
			return;
		}
		String[] unids = strAPPIDs.split(",");
		for (String strAPPID : unids) {
			Application app = new Application();
			app.setUNID(strAPPID);
			app.setPath(m_AGMRProperties.getProperty(strAPPID + "_PATH"));
			app.setUserID(m_AGMRProperties.getProperty(strAPPID + "_USER"));
			app.setCredValues(app.getUserID(), m_AGMRProperties.getProperty(strAPPID + "_PW"));
			m_ApplicationRegistry.put(strAPPID, app);
		}
		logCurrent.info("Read Applications has found " + m_ApplicationRegistry.size() + " Application(s) to watch.");

	}

	private boolean isServer() {
		boolean blRC = false;
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		try {
			if (NotesContext.getCurrentUnchecked() != null && NotesContext.getCurrentUnchecked().getCurrentSession() != null) {
				Session sesCurrent = NotesContext.getCurrentUnchecked().getCurrentSession();
				blRC = sesCurrent.isOnServer();
				logCurrent.fine("isOnServer == " + blRC);
			} else {
				logCurrent.fine("No NotesContext available!");
				NotesThread.sinitThread();
				Session sesCurrent = NotesFactory.createSession();
				blRC = sesCurrent.isOnServer();
				logCurrent.fine("isOnServer == " + blRC);
				sesCurrent.recycle();
				NotesThread.stermThread();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blRC;
	}
}
