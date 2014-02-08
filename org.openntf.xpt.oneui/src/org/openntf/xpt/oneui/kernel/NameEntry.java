package org.openntf.xpt.oneui.kernel;

import java.io.Serializable;

public final class NameEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String m_Value;
	private final String m_Label;
	private final String m_ResultLine;

	public NameEntry(String value, String label, String resultLine) {
		super();
		m_Value = value;
		m_Label = label;
		m_ResultLine = resultLine;
	}

	public String getValue() {
		return m_Value;
	}

	public String getLabel() {
		return m_Label;
	}

	public String getResultLine() {
		return m_ResultLine;
	}

}
