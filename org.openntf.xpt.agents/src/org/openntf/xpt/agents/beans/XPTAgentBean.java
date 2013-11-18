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
package org.openntf.xpt.agents.beans;

import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.openntf.xpt.agents.XPageAgentRegistry;
import org.openntf.xpt.agents.master.ApplicationStatus;
import org.openntf.xpt.agents.master.ExecutionUserProperties;
import org.openntf.xpt.agents.master.XPageAgentManager;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.jscript.types.FBSValue;

public class XPTAgentBean {

	public static final String BEAN_NAME = "xptAgentBean"; //$NON-NLS-1$

	public static XPTAgentBean get(FacesContext context) {
		XPTAgentBean bean = (XPTAgentBean) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTAgentBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	public String registerExecutionProperties(HashMap<String, String> hsProperties) {
		return XPageAgentRegistry.getInstance().addExecutionProperties(hsProperties);
	}

	public String executeAgentUI(String strAlias, HashMap<String, String> hsProperties) {
		String strPropID = XPageAgentRegistry.getInstance().addExecutionProperties(hsProperties);
		return XPageAgentRegistry.getInstance().executeJobUI(strAlias, strPropID);
	}

	public String executeAgentUI(String strAliasName) {
		return XPageAgentRegistry.getInstance().executeJobUI(strAliasName, null);
	}

	public FBSValue getJobStatus(String strJobID) {
		return XPageAgentRegistry.getInstance().getJobStatus(strJobID);
	}

	public int checkSchedule() {
		return XPageAgentRegistry.getInstance().checkSchedule();
	}

	public ExecutionUserProperties registerApplication2Master(String strUser, String strPassword) {
		try {
			String strHost = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
			int nNSF = strHost.toLowerCase().indexOf(".nsf");
			String strNSFURL = strHost.substring(0, nNSF) + ".nsf";
			return XPageAgentManager.getInstance().registerNewApplication(NotesContext.getCurrentUnchecked().getCurrentDatabase().getReplicaID(), strNSFURL,
					strUser, strPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ApplicationStatus getApplicationStatus() {
		try {
			return XPageAgentManager.getInstance().getApplicationStatus(NotesContext.getCurrentUnchecked().getCurrentDatabase().getReplicaID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean unregisterApplication() {
		try {
			return XPageAgentManager.getInstance().unregisterApplication(NotesContext.getCurrentUnchecked().getCurrentDatabase().getReplicaID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
