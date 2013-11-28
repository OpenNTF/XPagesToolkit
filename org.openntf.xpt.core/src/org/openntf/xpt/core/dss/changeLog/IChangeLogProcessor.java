package org.openntf.xpt.core.dss.changeLog;

import java.util.List;

public interface IChangeLogProcessor {

	public int doChangeLog(Object objCurrent, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField, StorageAction action);

	public List<ChangeLogEntry> getAllChangeLogEntries(String strObjectClassName, String strPK);

	public List<ChangeLogEntry> getAllChangeLogEntries4Attribugte(String strObjectClassName, String strPK, String strObjectMember);
}
