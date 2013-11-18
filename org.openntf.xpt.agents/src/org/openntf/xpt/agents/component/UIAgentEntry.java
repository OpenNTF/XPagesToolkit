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
package org.openntf.xpt.agents.component;

import java.io.Serializable;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.openntf.xpt.agents.XPageAgentEntry;
import org.openntf.xpt.agents.XPageAgentRegistry;


public class UIAgentEntry implements Serializable, StateHolder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_AgentAlias;
	//NOT SAVE via save/restore. Serialized Object has no binding to the Registry!
	private XPageAgentEntry m_Entry;
	private boolean m_Transient;
	private boolean m_DetailsOpen = false;;

	public UIAgentEntry() {

	}

	public UIAgentEntry(XPageAgentEntry entry) {
		super();
		m_AgentAlias = entry.getAlias();
	}

	public boolean isDetailsOpen() {
		return m_DetailsOpen;
	}

	public void setDetailsOpen(boolean detailsOpen) {
		m_DetailsOpen = detailsOpen;
	}

	public XPageAgentEntry getEntry() {
		if (m_Entry == null) {
			m_Entry = XPageAgentRegistry.getInstance().getXPageAgent(m_AgentAlias);
		}
		return m_Entry;
	}


	@Override
	public boolean isTransient() {
		return m_Transient;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		m_DetailsOpen = (Boolean) values[0];
		m_AgentAlias = (String) values[1];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = m_DetailsOpen;
		values[1] = m_AgentAlias;
		return values;
	}

	@Override
	public void setTransient(boolean arg0) {
		m_Transient = arg0;

	}

}
