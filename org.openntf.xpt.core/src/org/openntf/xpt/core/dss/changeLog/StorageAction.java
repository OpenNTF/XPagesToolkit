package org.openntf.xpt.core.dss.changeLog;

public enum StorageAction {
	CREATE, MODIFY, DELETE;

	public boolean isDelete() {
		return this == DELETE;
	}

	public boolean isModify() {
		return this == MODIFY;
	}

	public boolean isCreate() {
		return this == CREATE;
	}
}
