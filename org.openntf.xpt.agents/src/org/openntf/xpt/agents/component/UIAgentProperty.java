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
package org.openntf.xpt.agents.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class UIAgentProperty extends ValueBindingObjectImpl {

	private String m_Key;
	private String m_Value;

	public String getKey() {
		if (m_Key != null) {
			return m_Key;
		}
		ValueBinding vb = getValueBinding("key");
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		}
		return null;

	}

	public void setKey(String key) {
		m_Key = key;
	}

	public String getValue() {
		if (m_Value != null) {
			return m_Value;
		}
		ValueBinding vb = getValueBinding("value");
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		}
		return null;

	}

	public void setValue(String value) {
		m_Value = value;
	}

	@Override
	public void restoreState(FacesContext context, Object valCurrent) {
		Object[] values = (Object[]) valCurrent;
		super.restoreState(context, values[0]);
		m_Key = (String) values[1];
		m_Value = (String) values[2];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = m_Key;
		values[2] = m_Value;
		return values;
	}
}
