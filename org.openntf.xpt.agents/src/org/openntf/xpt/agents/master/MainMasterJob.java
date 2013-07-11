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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class MainMasterJob extends Job {

	private boolean m_Running = true;

	public MainMasterJob(String name) {
		super(name);
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		try {
			if (!isRunning()) {
				return Status.CANCEL_STATUS;
			}
			XPageAgentManager.getInstance().checkTasks();
			if (isRunning()) {
				schedule(60000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}

	public boolean isRunning() {
		return m_Running;
	}

	public void setRunning(boolean running) {
		m_Running = running;
	}

}
