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

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.utils.ServiceSupport;

public class Definition {

	private final String m_NotesField;
	private final String m_JavaField;
	private final IBinder<?> m_Binder;
	private final boolean m_ChangeLog;
	private final boolean m_Encrypted;
	private final String[] m_EncRoles;
	private final boolean m_DateOnly;
	private final boolean m_Author;
	private final boolean m_Formula;
	private final boolean m_Names;
	private final boolean m_Reader;
	private final boolean m_ReadOnly;
	private final String m_ShowNameAs;
	private final boolean m_WriteOnly;
	private final Class<?> m_InnerClass;
	private final Type m_GenericType;

	public static Definition buildDefiniton(DominoStore ds, DominoEntity de, IBinder<?> binCurrent, Field fld) {
		return new Definition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(ds, fld.getName()), binCurrent, de.changeLog(), de.encrypt(), de.encRoles(), de.dateOnly(), de.isAuthor(),
				de.isFormula(), de.isNames(), de.isReader(), de.readOnly(), de.showNameAs(), de.writeOnly(), fld.getType(), fld.getGenericType());
	}

	private Definition(String notesField, String javaField, IBinder<?> binCurrent, boolean changeLog, boolean encrypted, String[] encRoles, boolean dateOnly, boolean isAuthor, boolean isFormula,
			boolean isNames, boolean isReader, boolean readOnly, String strShowNameAs, boolean writeOnly, Class<?> innerClass, Type genericType) {
		m_NotesField = notesField;
		m_JavaField = javaField;
		m_Binder = binCurrent;
		m_ChangeLog = changeLog;
		m_Encrypted = encrypted;
		m_EncRoles = encRoles;
		m_DateOnly = dateOnly;
		m_Author = isAuthor;
		m_Formula = isFormula;
		m_Names = isNames;
		m_Reader = isReader;
		m_ReadOnly = readOnly;
		m_ShowNameAs = strShowNameAs;
		m_WriteOnly = writeOnly;
		m_InnerClass = innerClass;
		m_GenericType = genericType;
	}

	public String getNotesField() {
		return m_NotesField;
	}

	public String getJavaField() {
		return m_JavaField;
	}

	public IBinder<?> getBinder() {
		return m_Binder;
	}

	public boolean isChangeLog() {
		return m_ChangeLog;
	}

	public boolean isEncrypted() {
		return m_Encrypted;
	}

	public String[] getEncRoles() {
		return m_EncRoles;
	}

	public boolean isDateOnly() {
		return m_DateOnly;
	}

	public boolean isAuthor() {
		return m_Author;
	}

	public boolean isFormula() {
		return m_Formula;
	}

	public boolean isNames() {
		return m_Names;
	}

	public boolean isReader() {
		return m_Reader;
	}

	public boolean isReadOnly() {
		return m_ReadOnly;
	}

	public String getShowNameAs() {
		return m_ShowNameAs;
	}

	public boolean isWriteOnly() {
		return m_WriteOnly;
	}

	public Class<?> getInnerClass() {
		return m_InnerClass;
	}

	public Type getGenericType() {
		return m_GenericType;
	}

}
