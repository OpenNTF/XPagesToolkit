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
package org.openntf.xpt.agents;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.openntf.xpt.agents.annotations.XPagesAgent;
import org.openntf.xpt.agents.registry.AmgrPropertiesHandler;
import org.openntf.xpt.core.dss.changeLog.ChangeLogService;
import org.openntf.xpt.core.dss.encryption.EncryptionService;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.designer.runtime.Application;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.SessionCloner;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.jscript.types.FBSNull;
import com.ibm.jscript.types.FBSNumber;
import com.ibm.jscript.types.FBSString;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener2;

public abstract class XPageAgentRegistry implements ApplicationListener2 {

	private static final String XPAGEAGENT_SERVICE_KEY = "xpt.agent.registry"; // $NON-NLS-1$

	private final HashMap<String, XPageAgentEntry> m_Agents = new HashMap<String, XPageAgentEntry>();

	private final HashMap<String, XPageAgentJob> m_RunningJobs = new HashMap<String, XPageAgentJob>();
	private final HashMap<String, HashMap<String, String>> m_ExecutionPropertyRegistry = new HashMap<String, HashMap<String, String>>();

	private AmgrPropertiesHandler m_AgentRunProperties;

	private Logger m_Logger;;

	private String m_DatabasePath;

	abstract public void registerAgents();

	public void addXPageAgent(XPageAgentEntry en) {
		m_Agents.put(en.getAlias(), en);
	}

	public XPageAgentEntry getXPageAgent(String strAlias) {
		if (m_AgentRunProperties == null) {
			initAgentRunProperties();
		}
		applyARP();
		return m_Agents.get(strAlias);
	}

	public int checkSchedule(FacesContext fc) {
		int nCount = 0;
		if (m_Agents == null) {
			return nCount;
		}
		if (m_AgentRunProperties == null) {
			initAgentRunProperties();
		}
		applyARP();

		m_Logger.info("checkSchedule");
		for (XPageAgentEntry agentEntry : m_Agents.values()) {
			if (agentEntry.readyToExecuteScheduled()) {
				nCount++;
				m_Logger.info("Execute: " + agentEntry.getAlias());
				initExecutionBE(agentEntry, fc);
			}
		}
		return nCount;
	}

	public String executeJobUI(String strAgentAlias, String strExectuionPropertiesID) {
		if (m_Agents == null) {
			return "<no agents>";
		}
		if (!m_Agents.containsKey(strAgentAlias)) {
			return "<agent " + strAgentAlias + " not found>";
		}

		// CHECK if a possible Encryption Provider is loaded
		EncryptionService.getInstance().agentLoadProvider();
		// CHECK if a possible ChangLogProvider is loaded
		ChangeLogService.getInstance().getChangeLogProcessors();

		XPageAgentEntry en = m_Agents.get(strAgentAlias);
		try {
			m_Logger.info("Agent found with alias: " + en.getAlias());
			final XPageAgentJob jbCurrent = buildAgentClass(en, FacesContext.getCurrentInstance());
			m_RunningJobs.put(jbCurrent.getJobID(), jbCurrent);
			jbCurrent.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					m_RunningJobs.remove(jbCurrent.getJobID());
				}
			});
			if (strExectuionPropertiesID != null) {
				jbCurrent.setExecutionProperties(m_ExecutionPropertyRegistry.get(strExectuionPropertiesID));
			}
			AccessController.doPrivileged(new PrivilegedAction<Object>() {

				@Override
				public Object run() {
					try {
						m_Logger.info("Execution scheduled");
						jbCurrent.schedule();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});
			deleteExecutionProperties(strExectuionPropertiesID);
			return jbCurrent.getJobID();
		} catch (Exception e) {
			m_Logger.log(Level.SEVERE, "Error in executeUI:", e);
		}

		return "<unkown error>";
	}

	public XPageAgentJob buildAgentClass(XPageAgentEntry agenEntry, FacesContext fc) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		XPageAgentJob jbCurrent = null;
		Class<?>[] clArgs = new Class<?>[1];
		clArgs[0] = String.class;
		Constructor<?> ct = agenEntry.getAgent().getConstructor(clArgs);
		Object[] obArgs = new Object[1];
		obArgs[0] = agenEntry.getTitle();
		jbCurrent = (XPageAgentJob) ct.newInstance(obArgs);
		jbCurrent.setExecMode(agenEntry.getExecutionMode());
		jbCurrent.setDatabasePath(m_DatabasePath);
		jbCurrent.initCode(NotesContext.getCurrent().getModule(), SessionCloner.getSessionCloner(), fc);

		return jbCurrent;
	}

	private void initExecutionBE(final XPageAgentEntry agentEntry, FacesContext fc) {
		try {
			final XPageAgentJob jbCurrent = buildAgentClass(agentEntry, fc);
			agentEntry.runScheduled();
			m_RunningJobs.put(jbCurrent.getJobID(), jbCurrent);
			jbCurrent.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					m_RunningJobs.remove(jbCurrent.getJobID());
					agentEntry.endSchedules();
				}
			});

			AccessController.doPrivileged(new PrivilegedAction<Object>() {

				@Override
				public Object run() {
					jbCurrent.schedule(1000);
					return null;
				}
			});
		} catch (Exception e) {
			m_Logger.log(Level.SEVERE, "Error during initExcecutionBE: ", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void initAgent(Class<?> aAgent) {
		try {
			if (aAgent.isAnnotationPresent(XPagesAgent.class)) {
				XPagesAgent xag = aAgent.getAnnotation(XPagesAgent.class);
				XPageAgentEntry age = XPageAgentEntry.buildXPagesAgentEntry((Class<XPageAgentJob>) aAgent, xag, false);
				addXPageAgent(age);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reshedJob() {

	}

	@Override
	public void applicationCreated(ApplicationEx app) {
		try {
			m_Logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());
			m_Logger.info("App started");
			Application.get().putObject(XPAGEAGENT_SERVICE_KEY, this);
			initApplication();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initApplication() {
		NSFComponentModule moduleCurrent = NotesContext.getCurrent().getModule();

		m_DatabasePath = moduleCurrent.getDatabasePath();
		m_Logger.info("MODUL - getDatabasePath(): " + moduleCurrent.getDatabasePath());
		registerAgents();

		m_Logger.info(m_Agents.size() + " Agents registered");
	}

	@Override
	public void applicationDestroyed(ApplicationEx arg0) {
		m_Logger.info("Application unloaded.");

	}

	@Override
	public void applicationRefreshed(ApplicationEx arg0) {
		try {
			m_Logger.info("Application refreshed.");
			Application.get().putObject(XPAGEAGENT_SERVICE_KEY, this);
			initApplication();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static XPageAgentRegistry getInstance(Application app) {
		XPageAgentRegistry f = (XPageAgentRegistry) app.getObject(XPAGEAGENT_SERVICE_KEY);
		return f;

	}

	public static XPageAgentRegistry getInstance() {
		return getInstance(Application.get());
	}

	public FBSValue getJobStatus(String strJOBID) {
		try {
			if (m_RunningJobs.containsKey(strJOBID)) {
				XPageAgentJob job = m_RunningJobs.get(strJOBID);
				ObjectObject objRC = new ObjectObject();
				objRC.put("status", FBSUtility.wrap(job.getAgentTaskStatus().toString()));
				objRC.put("title", FBSUtility.wrap(job.getName()));
				objRC.put("taskCompletion", FBSUtility.wrap(job.getTaskCompletion()));
				objRC.put("progressMessage", FBSUtility.wrap(job.getCurrentTaskStatus()));
				return objRC;

			} else {
				ObjectObject objRC = new ObjectObject();
				objRC.put("status", FBSUtility.wrap("nojob"));
				objRC.put("title", FBSString.emptyString);
				objRC.put("taskCompletion", FBSNumber.Zero);
				objRC.put("progressMessage", FBSString.emptyString);
				return objRC;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FBSNull.nullValue;
	}

	public String addExecutionProperties(HashMap<String, String> properties) {
		String strKey = UUID.randomUUID().toString();
		m_ExecutionPropertyRegistry.put(strKey, properties);
		return strKey;
	}

	public void deleteExecutionProperties(String strKey) {
		m_ExecutionPropertyRegistry.remove(strKey);
	}

	public List<XPageAgentEntry> getAllAgents() {
		if (m_AgentRunProperties == null) {
			initAgentRunProperties();
		}
		applyARP();
		return new ArrayList<XPageAgentEntry>(m_Agents.values());
	}

	private void initAgentRunProperties() {
		try {
			m_AgentRunProperties = new AmgrPropertiesHandler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void applyARP() {
		for (XPageAgentEntry ape : m_Agents.values()) {
			if (ape.getExecutionMode().isScheduled()) {
				if ("ON".equalsIgnoreCase(m_AgentRunProperties.getProperty(ape.getAlias())) && !ape.isActive()) {
					ape.setActive(true);
				}
			}
		}
	}

	private void saveARP() {
		try {
			m_AgentRunProperties.saveProperties();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void activateAgent(String strAgent, FacesContext fc) {
		XPageAgentEntry agentEntry = m_Agents.get(strAgent);
		if (agentEntry != null) {
			agentEntry.setActive(true);
			m_AgentRunProperties.setProperty(strAgent, "ON");
			if (agentEntry.readyToExecuteScheduled()) {
				initExecutionBE(agentEntry, fc);
			}
			saveARP();
		}
	}

	public void deActivateAgent(String strAgent) {
		XPageAgentEntry age = m_Agents.get(strAgent);
		if (age != null) {
			age.setActive(false);
			m_AgentRunProperties.setProperty(strAgent, "OFF");
			saveARP();
		}
	}

}
