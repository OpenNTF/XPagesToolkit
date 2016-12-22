/**
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
package org.openntf.xpt.oneui.kernel;

import java.io.Serializable;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;

@JSONObject()
public final class NameEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONEntity(jsonproperty = "value")
	private final String m_Value;
	@JSONEntity(jsonproperty = "label")
	private final String m_Label;
	@JSONEntity(jsonproperty = "line")
	private final String m_ResultLine;
	@JSONEntity(jsonproperty = "linehl")
	private String m_ResultLineHL;

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

	public String getResultLineHL() {
		if (m_ResultLineHL == null) {
			return m_ResultLine;
		}
		return m_ResultLineHL;
	}

	public void buildResultLineHL(String strSearch) {
		int start = m_ResultLine.toLowerCase().indexOf(strSearch.toLowerCase());
		int stop = start + 3 + strSearch.length();

		StringBuilder sb = new StringBuilder(m_ResultLine);
		if (start > -1) {
			sb.insert(start, "<b>");
			sb.insert(stop, "</b>");
		}
		m_ResultLineHL = sb.toString();
	}

}
