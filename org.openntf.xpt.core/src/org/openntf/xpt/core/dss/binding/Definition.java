/*
 * © Copyright WebGate Consulting AG, 2012
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
package org.openntf.xpt.core.dss.binding;

import java.util.HashMap;

public class Definition {

	private String m_NotesField;
	private String m_JavaField;
	private IBinder<?> m_Binder;
	private boolean m_ChangeLog;
	private HashMap<String, Object> m_AdditionalValues;

	public Definition(String notesField, String javaField, IBinder<?> binCurrent, boolean changeLog, HashMap<String, Object> addValues) {
		m_NotesField = notesField;
		m_JavaField = javaField;
		m_Binder = binCurrent;
		m_AdditionalValues = addValues;
		setChangeLog(changeLog);
	}

	public String getNotesField() {
		return m_NotesField;
	}

	public void setNotesField(String notesField) {
		m_NotesField = notesField;
	}

	public String getJavaField() {
		return m_JavaField;
	}

	public void setJavaField(String javaField) {
		m_JavaField = javaField;
	}

	public IBinder<?> getBinder() {
		return m_Binder;
	}

	public void setBinder(IBinder<?> binCurrent) {
		m_Binder = binCurrent;
	}

	public void setAdditionalValues(HashMap<String, Object> additionalValues) {
		m_AdditionalValues = additionalValues;
	}

	public HashMap<String, Object> getAdditionalValues() {
		return m_AdditionalValues;
	}

	public boolean isChangeLog() {
		return m_ChangeLog;
	}

	public void setChangeLog(boolean changeLog) {
		m_ChangeLog = changeLog;
	}

}
