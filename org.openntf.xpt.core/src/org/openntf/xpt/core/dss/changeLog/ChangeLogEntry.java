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
package org.openntf.xpt.core.dss.changeLog;

import java.io.Serializable;
import java.util.Date;

public class ChangeLogEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_PrimaryKey;
	private String m_ObjectClass;
	private String m_ObjectField;
	private String m_StorageField;
	private Date m_Date;
	private Object m_OldValue;
	private Object m_NewValue;
	private String m_User;
	private StorageAction m_Action;

	public String getUser() {
		return m_User;
	}

	public String getPrimaryKey() {
		return m_PrimaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		m_PrimaryKey = primaryKey;
	}

	public String getObjectClass() {
		return m_ObjectClass;
	}

	public void setObjectClass(String objectClass) {
		m_ObjectClass = objectClass;
	}

	public String getObjectField() {
		return m_ObjectField;
	}

	public void setObjectField(String objectField) {
		m_ObjectField = objectField;
	}

	public String getStorageField() {
		return m_StorageField;
	}

	public void setStorageField(String storageField) {
		m_StorageField = storageField;
	}

	public Date getDate() {
		return m_Date;
	}

	public void setDate(Date date) {
		m_Date = date;
	}

	public Object getOldValue() {
		return m_OldValue;
	}

	public void setOldValue(Object oldValue) {
		m_OldValue = oldValue;
	}

	public Object getNewValue() {
		return m_NewValue;
	}

	public void setNewValue(Object newValue) {
		m_NewValue = newValue;
	}

	public void setUser(String strUser) {
		m_User = strUser;

	}

	public StorageAction getAction() {
		return m_Action;
	}

	public void setAction(StorageAction action) {
		m_Action = action;
	}

}
