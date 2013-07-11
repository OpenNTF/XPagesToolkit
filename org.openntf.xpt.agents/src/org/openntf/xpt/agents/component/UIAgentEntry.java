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

import com.ibm.xsp.util.FacesUtil;


public class UIAgentEntry implements Serializable, StateHolder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private XPageAgentEntry m_Entry;
	private boolean m_Transient;
	private boolean m_DetailsOpen = false;;

	public UIAgentEntry() {

	}

	public UIAgentEntry(XPageAgentEntry entry) {
		super();
		m_Entry = entry;
	}

	public boolean isDetailsOpen() {
		return m_DetailsOpen;
	}

	public void setDetailsOpen(boolean detailsOpen) {
		m_DetailsOpen = detailsOpen;
	}

	public XPageAgentEntry getEntry() {
		return m_Entry;
	}

	public void setEntry(XPageAgentEntry entry) {
		m_Entry = entry;
	}

	@Override
	public boolean isTransient() {
		return m_Transient;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		m_DetailsOpen = (Boolean) values[0];
		m_Entry = (XPageAgentEntry) FacesUtil.objectFromSerializable(context, values[1]);
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = m_DetailsOpen;
		values[1] = FacesUtil.objectToSerializable(context, m_Entry);
		// values[1] = StateHolderUtil.saveObjectState(context, m_Entry);
		return values;
	}

	@Override
	public void setTransient(boolean arg0) {
		m_Transient = arg0;

	}

}
