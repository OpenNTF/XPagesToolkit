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
		// System.out.println(m_NewValue instanceof Vector<?>);
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
