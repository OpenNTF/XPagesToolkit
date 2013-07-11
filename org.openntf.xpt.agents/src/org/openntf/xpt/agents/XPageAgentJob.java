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
package org.openntf.xpt.agents;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Session;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.core.utils.logging.LoggerFactory;


import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.SessionCloner;

public abstract class XPageAgentJob extends Job implements IXPageAgent {

	private ExecutionMode m_ExecMode;
	private String m_DatabasePath;

	private int m_TaskCompletion;
	private AgentTaskStatus m_AgentTaskStatus = AgentTaskStatus.SCHEDULED;
	private String m_CurrentTaskStatus;
	private NSFComponentModule m_Module;
	private SessionCloner m_Cloner;
	private HashMap<String, String> m_ExecutionProperties;

	private final String m_JobID = UUID.randomUUID().toString();
	private Logger m_Logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

	public XPageAgentJob(String name) {
		super(name);
	}

	public ExecutionMode getExecMode() {
		return m_ExecMode;
	}

	public void setExecMode(ExecutionMode execMode) {
		m_ExecMode = execMode;
	}

	public String getDatabasePath() {
		return m_DatabasePath;
	}

	public void setDatabasePath(String databasePath) {
		m_DatabasePath = databasePath;
	}

	public Logger getLogger() {
		return m_Logger;
	}

	public void setLogger(Logger logger) {
		m_Logger = logger;
	}

	public String getJobID() {
		return m_JobID;
	}

	public int getTaskCompletion() {
		return m_TaskCompletion;
	}

	public void setTaskCompletion(int taskCompletion) {
		m_TaskCompletion = taskCompletion;
	}

	public String getCurrentTaskStatus() {
		return m_CurrentTaskStatus;
	}

	public void setCurrentTaskStatus(String currentTaskStatus) {
		m_CurrentTaskStatus = currentTaskStatus;
	}

	public void initCode(NSFComponentModule modCurrent, SessionCloner sesCloner) {
		m_Module = modCurrent;
		m_Cloner = sesCloner;
	}

	public HashMap<String, String> getExecutionProperties() {
		return m_ExecutionProperties;
	}

	public void setExecutionProperties(HashMap<String, String> executionProperties) {
		m_ExecutionProperties = executionProperties;
	}

	public AgentTaskStatus getAgentTaskStatus() {
		return m_AgentTaskStatus;
	}

	public void setAgentTaskStatus(AgentTaskStatus agentTaskStatus) {
		m_AgentTaskStatus = agentTaskStatus;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		try {
			return executeJob();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.CANCEL_STATUS;
	}



	private IStatus executeJob() {
		NotesContext context = null;
		Session sesCurrent = null;
		try {
			context = new NotesContext(m_Module);
			NotesContext.initThread(context);
			sesCurrent = m_Cloner.getSession();
			m_Logger.info("Execute Code");
			m_Logger.info("Session EffectiveUser: " + sesCurrent.getEffectiveUserName());
			setAgentTaskStatus(AgentTaskStatus.RUNNING);
			Database ndbCurrent = null;
			String strServer = sesCurrent.getServerName();
			if (strServer != null && m_DatabasePath != null) {
				ndbCurrent = sesCurrent.getDatabase(strServer, m_DatabasePath);
			}
			executeCode(sesCurrent, ndbCurrent);
			if (getAgentTaskStatus() == AgentTaskStatus.RUNNING) {
				setAgentTaskStatus(AgentTaskStatus.FINISHED);
			}
			ndbCurrent.recycle();
			m_Logger.info("Execute Code -> DONE");
			Thread.sleep(5000);

		} catch (Exception e) {
			setAgentTaskStatus(AgentTaskStatus.FINISHED_WITH_ERROR);
			e.printStackTrace();

		} finally {
			try {
				if (context != null) {
					NotesContext.termThread();
					m_Logger.info("NotesContext.termThread");
				} 
				if (sesCurrent != null) {
					sesCurrent.recycle();
					m_Logger.info("Session recycled");
				}
				m_Logger.info("XPageAgentJob Ended!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Status.OK_STATUS;
	}

	abstract public int executeCode(Session sesCurrent, Database ndbCurrent);

}
