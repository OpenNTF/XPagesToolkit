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
package org.openntf.xpt.core.dss.changeLog;

import java.util.List;

import lotus.domino.Database;
import lotus.domino.Session;

public interface IChangeLogProcessor {

	/**
	 * Invokes the changeLogging in the ChangeLogProvider
	 * @param cle - the current changelog entrie
	 * @return
	 */
	public int doChangeLog(ChangeLogEntry cle, Session sesCurrent, Database ndbSource);

	public List<ChangeLogEntry> getAllChangeLogEntries(String strObjectClassName, String strPK);

	public List<ChangeLogEntry> getAllChangeLogEntries4Attribute(String strObjectClassName, String strPK, String strObjectMember);
}
