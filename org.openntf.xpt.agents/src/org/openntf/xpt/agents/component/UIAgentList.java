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

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.openntf.xpt.agents.XPageAgentEntry;
import org.openntf.xpt.agents.XPageAgentRegistry;


import com.ibm.xsp.util.StateHolderUtil;

public class UIAgentList extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.agents.component.uiagentlist"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.agents.component.uiagentlist"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.agents.component.uiagentlist"; //$NON-NLS-1$

	private List<UIAgentEntry> m_Entries;

	public UIAgentList() {
		super();
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public List<UIAgentEntry> getAllEntries() {
		if (m_Entries == null) {
			m_Entries = new ArrayList<UIAgentEntry>();
			for (XPageAgentEntry age : XPageAgentRegistry.getInstance().getAllAgents()) {
				m_Entries.add(new UIAgentEntry(age));
			}
		}
		return m_Entries;
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = StateHolderUtil.saveList(context, getAllEntries());
		return values;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_Entries = StateHolderUtil.restoreList(context, this, values[1]);
	}
}
