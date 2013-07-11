package org.openntf.xpt.agents.component;

import javax.faces.context.FacesContext;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class UIAgentProperty extends ValueBindingObjectImpl {

	private String m_Key;
	private String m_Value;

	public String getKey() {
		return m_Key;
	}

	public void setKey(String key) {
		m_Key = key;
	}

	public String getValue() {
		return m_Value;
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
