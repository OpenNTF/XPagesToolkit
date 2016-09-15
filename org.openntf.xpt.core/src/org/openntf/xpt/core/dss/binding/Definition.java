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
package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.dss.binding.embedded.EmbeddedObjectBinder;
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
	private final BinderContainer m_Container;
	private final int m_Counter;
	private final boolean m_NotesSummary;

	public static Definition buildDefiniton(DominoStore ds, DominoEntity de, IBinder<?> binCurrent, Field fld) {
		if (binCurrent instanceof EmbeddedObjectBinder) {
			return new Definition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(ds, fld.getName()), binCurrent, de.changeLog(), de.encrypt(), de.encRoles(), de.dateOnly(), de.isAuthor(),
					de.isFormula(), de.isNames(), de.isReader(), de.readOnly(), de.showNameAs(), de.writeOnly(), fld.getType(), fld.getGenericType(), new BinderContainer(de.FieldName()), -1,de.isNotesSummary());

		} else {
			return new Definition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(ds, fld.getName()), binCurrent, de.changeLog(), de.encrypt(), de.encRoles(), de.dateOnly(), de.isAuthor(),
					de.isFormula(), de.isNames(), de.isReader(), de.readOnly(), de.showNameAs(), de.writeOnly(), fld.getType(), fld.getGenericType(), null, -1,de.isNotesSummary());
		}
	}

	public static Definition buildDefinition4Decryption(String strFieldName, String[] arrRoles, boolean isDateOnly, IBinder<?> binCurrent) {
		return new Definition(strFieldName, "", binCurrent, false, true, arrRoles, isDateOnly, false, false, false, false, false, "", false, null, null, null, -1,true);

	}

	public static Definition buildDefinition4EO(DominoStore ds, DominoEntity de, IBinder<?> binCurrent, Field fld, String storePrefix) {
		return new Definition(de.embedded() ? de.FieldName() : storePrefix + "_" + de.FieldName(), ServiceSupport.buildCleanFieldNameCC(ds, fld.getName()), binCurrent, de.changeLog(), de.encrypt(),
				de.encRoles(), de.dateOnly(), de.isAuthor(), de.isFormula(), de.isNames(), de.isReader(), de.readOnly(), de.showNameAs(), de.writeOnly(), fld.getType(), fld.getGenericType(),
				de.embedded() ? new BinderContainer(storePrefix) : null, -1,de.isNotesSummary());
	}

	public static Definition cloneDefinition(Definition def, int nCounter) {
		return new Definition(def.m_NotesField, def.m_JavaField, def.m_Binder, def.m_ChangeLog, def.m_Encrypted, def.m_EncRoles, def.m_DateOnly, def.m_Author, def.m_Formula, def.m_Names,
				def.m_Reader, def.m_ReadOnly, def.m_ShowNameAs, def.m_WriteOnly, def.m_InnerClass, def.m_GenericType, def.m_Container, nCounter,def.m_NotesSummary);
	}

	private Definition(String notesField, String javaField, IBinder<?> binCurrent, boolean changeLog, boolean encrypted, String[] encRoles, boolean dateOnly, boolean isAuthor, boolean isFormula,
			boolean isNames, boolean isReader, boolean readOnly, String strShowNameAs, boolean writeOnly, Class<?> innerClass, Type genericType, BinderContainer bc, int counter, boolean notesSummary) {
		this.m_NotesField = notesField;
		this.m_JavaField = javaField;
		this.m_Binder = binCurrent;
		this.m_ChangeLog = changeLog;
		this.m_Encrypted = encrypted;
		this.m_EncRoles = encRoles;
		this.m_DateOnly = dateOnly;
		this.m_Author = isAuthor;
		this.m_Formula = isFormula;
		this.m_Names = isNames;
		this.m_Reader = isReader;
		this.m_ReadOnly = readOnly;
		this.m_ShowNameAs = strShowNameAs;
		this.m_WriteOnly = writeOnly;
		this.m_InnerClass = innerClass;
		this.m_GenericType = genericType;
		this.m_Container = bc;
		this.m_Counter = counter;
		this.m_NotesSummary = notesSummary;
	}

	public String getNotesField() {
		if (m_Counter > -1) {
			return m_NotesField + "_" + m_Counter;
		}
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

	public BinderContainer getContainer() {
		return m_Container;
	}

	public int getCounter() {
		return m_Counter;
	}

	public boolean isNotesSummary() {
		return m_NotesSummary;
	}

}
