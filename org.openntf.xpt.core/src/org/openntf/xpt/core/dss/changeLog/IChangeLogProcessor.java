package org.openntf.xpt.core.dss.changeLog;

import java.util.List;

public interface IChangeLogProcessor {

	public int doChangeLog(ChangeLogEntry cle);

	public List<ChangeLogEntry> getAllChangeLogEntries(String strObjectClassName, String strPK);

	public List<ChangeLogEntry> getAllChangeLogEntries4Attribute(String strObjectClassName, String strPK, String strObjectMember);
}
