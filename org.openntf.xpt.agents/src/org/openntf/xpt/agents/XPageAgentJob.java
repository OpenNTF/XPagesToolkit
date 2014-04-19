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

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Session;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.Platform;
import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.SessionCloner;
import com.ibm.domino.xsp.module.nsf.platform.NotesPlatform;

public abstract class XPageAgentJob extends Job implements IXPageAgent {

	private ExecutionMode m_ExecMode;
	private String m_DatabasePath;
	private FacesContext m_FacesContext;

	public FacesContext getFacesContext() {
		return m_FacesContext;
	}

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

	public void initCode(NSFComponentModule modCurrent, SessionCloner sesCloner, FacesContext fc) {
		m_Module = modCurrent;
		m_Cloner = sesCloner;
		m_FacesContext = fc;
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
		NotesPlatform nPlatform = null;
		ClassLoader currentClassLoader = null;
		try {
			NotesContext contextCheck = NotesContext.getCurrentUnchecked();
			if (contextCheck != null) {
				throw new IllegalStateException();
			}
			if (m_Module.isDestroyed()) {
				throw new IllegalStateException();
			}
			m_Module.updateLastModuleAccess();

			context = new NotesContext(m_Module);
			NotesContext.initThread(context);
			sesCurrent = m_Cloner.getSession();

			Platform platform = Platform.getInstance();
			nPlatform = (platform instanceof NotesPlatform) ? (NotesPlatform) platform : null;
			if (nPlatform != null) {
				nPlatform.openingXPagesViewPart();
				nPlatform.addXPagesSecurityManager();
			}
			m_Logger.info("Execute Code");
			m_Logger.info("Session EffectiveUser: " + sesCurrent.getEffectiveUserName());
			setAgentTaskStatus(AgentTaskStatus.RUNNING);

			// SWITCHING the ClassLoader
			currentClassLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>() {
				public ClassLoader run() throws Exception {
					ClassLoader rcCL = Thread.currentThread().getContextClassLoader();
					Thread.currentThread().setContextClassLoader(m_Module.getModuleClassLoader());
					return rcCL;
				}
			});

			AccessControlContext currentACLContext = null;
			if (System.getSecurityManager() != null) {
				currentACLContext = ((ModuleClassLoader) m_Module.getModuleClassLoader()).getAccessControlContext();
			}
			Integer result = 0;
			if (currentACLContext != null) {
				final Session sesFin = sesCurrent;
				result = AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() {
					public Integer run() throws Exception {
						m_Logger.info("Running priviledged!");
						Database ndbCurrent = null;
						String strServer = sesFin.getServerName();
						if (strServer != null && m_DatabasePath != null) {
							ndbCurrent = sesFin.getDatabase(strServer, m_DatabasePath);
						}
						int rc = executeCode(sesFin, ndbCurrent);
						if (ndbCurrent != null) {
							ndbCurrent.recycle();
						}
						return rc;

					}
				}, currentACLContext);

			} else {
				Database ndbCurrent = null;
				String strServer = sesCurrent.getServerName();
				if (strServer != null && m_DatabasePath != null) {
					ndbCurrent = sesCurrent.getDatabase(strServer, m_DatabasePath);
				}

				result = executeCode(sesCurrent, ndbCurrent);
				if (ndbCurrent != null) {
					ndbCurrent.recycle();
				}

			}
			if (getAgentTaskStatus() == AgentTaskStatus.RUNNING) {
				setAgentTaskStatus(AgentTaskStatus.FINISHED);
			}
			m_Logger.info("Execute Code -> DONE with code: " + result);
			// Thread.sleep(5000);

		} catch (Exception e) {
			setAgentTaskStatus(AgentTaskStatus.FINISHED_WITH_ERROR);
			e.printStackTrace();

		} finally {
			try {
				if (m_Cloner != null) {
					m_Cloner.recycle();
					m_Logger.info("m_Cloner.recycle()");
				}
				if (currentClassLoader != null) {
					final ClassLoader cFin = currentClassLoader;
					AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
						public Void run() throws Exception {
							Thread.currentThread().setContextClassLoader(cFin);
							return null;
						}
					});
				}
				if (nPlatform != null) {
					nPlatform.closingXPagesViewPart();
					m_Logger.info("nPlatform.closingViewPart()");
				}
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
