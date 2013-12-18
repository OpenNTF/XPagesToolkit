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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Session;

import com.ibm.designer.runtime.Application;
import com.ibm.xsp.application.ApplicationEx;

public class ChangeLogService {
	private static final String CL_SERVICE_KEY = "xpt.dss.changelogger"; // $NON-NLS-1$

	private static final String CL_DATAPROVIDER_SERVICE = "org.openntf.xpt.core.dss.Changelog"; // $NON-NLS-1$

	private List<IChangeLogProcessor> m_CLServices;

	public static ChangeLogService getInstance() {
		ChangeLogService cls = (ChangeLogService) Application.get().getObject(CL_SERVICE_KEY);
		if (cls == null) {
			synchronized (ChangeLogService.class) {
				cls = (ChangeLogService) Application.get().getObject(CL_SERVICE_KEY);
				if (cls == null) {
					cls = new ChangeLogService();
					Application.get().putObject(CL_SERVICE_KEY, cls);

				}

			}
		}
		return cls;
	}

	public List<IChangeLogProcessor> getChangeLogProcessors() {
		if (m_CLServices == null) {
			m_CLServices = AccessController.doPrivileged(new PrivilegedAction<List<IChangeLogProcessor>>() {
				@SuppressWarnings("unchecked")
				@Override
				public List<IChangeLogProcessor> run() {
					List<IChangeLogProcessor> cl = ApplicationEx.getInstance().findServices(CL_DATAPROVIDER_SERVICE);
					return cl;
				}
			});
		}
		return m_CLServices;
	}

	public boolean checkChangeLog(Object objCurrent, String strPK, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField,
			StorageAction action, String strUserName, Session sesCurrent, Database ndbCurrent) {
		if (objValueNew == null && objValueOld == null) {
			return false;
		}
		if (objValueNew == null || objValueOld == null) {
			return processChangeLog(objCurrent, strPK, objValueOld, objValueNew, strObjectMember, strStorageField, action, strUserName, sesCurrent, ndbCurrent);
		}
		if (objValueNew instanceof Comparable<?> && objValueOld instanceof Comparable<?>) {
			@SuppressWarnings("unchecked")
			Comparable<Object> valO1 = (Comparable<Object>) objValueNew;
			@SuppressWarnings("unchecked")
			Comparable<Object> valO2 = (Comparable<Object>) objValueOld;
			if (valO1.compareTo(valO2) == 0) {
				return false;
			}
			return processChangeLog(objCurrent, strPK, objValueOld, objValueNew, strObjectMember, strStorageField, action, strUserName, sesCurrent, ndbCurrent);
		}
		if (objValueNew.getClass().isArray() && objValueOld.getClass().isArray()) {
			List<?> lstO1 = Arrays.asList(objValueNew);
			List<?> lstO2 = Arrays.asList(objValueOld);
			return compareListValues(objCurrent, strPK, objValueOld, objValueNew, strObjectMember, strStorageField, lstO1, lstO2, action, strUserName,
					sesCurrent, ndbCurrent);
		}
		if (objValueNew instanceof List<?> && objValueOld instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<?> lstO1 = (List<Object>) objValueNew;
			@SuppressWarnings("unchecked")
			List<?> lstO2 = (List<Object>) objValueOld;
			return compareListValues(objCurrent, strPK, objValueOld, objValueNew, strObjectMember, strStorageField, lstO1, lstO2, action, strUserName,
					sesCurrent, ndbCurrent);
		}

		return false;
	}

	private boolean compareListValues(Object objCurrent, String strPK, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField,
			List<?> lstO1, List<?> lstO2, StorageAction action, String strUserName, Session sesCurrent, Database ndbCurrent) {
		if (lstO1.size() == lstO2.size()) {
			int nCount = 0;
			for (Object objTest : lstO1) {
				Object obj2 = lstO2.get(nCount);
				if (!objTest.equals(obj2)) {
					return processChangeLog(objCurrent, strPK, objValueOld, objValueNew, strObjectMember, strStorageField, action, strUserName, sesCurrent,
							ndbCurrent);
				}
				nCount++;
			}

		} else {
			return processChangeLog(objCurrent, strPK, objValueOld, objValueNew, strObjectMember, strStorageField, action, strUserName, sesCurrent, ndbCurrent);

		}
		return false;
	}

	private boolean processChangeLog(Object objCurrent, String strPK, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField,
			StorageAction action, String strUserName, Session sesCurrent, Database ndbCurrent) {
		ChangeLogEntry cle = new ChangeLogEntry();
		cle.setAction(action);
		cle.setDate(new Date());
		cle.setNewValue(objValueNew);
		cle.setObjectClass(objCurrent.getClass().getCanonicalName());
		cle.setObjectField(strObjectMember);
		cle.setOldValue(objValueOld);
		cle.setStorageField(strStorageField);
		cle.setUser(strUserName);
		cle.setPrimaryKey(strPK);

		for (IChangeLogProcessor processor : getChangeLogProcessors()) {
			processor.doChangeLog(cle, sesCurrent, ndbCurrent);
		}
		return true;
	}

	public List<ChangeLogEntry> getChangeLog(String strObjectClass, String strPK) {
		List<ChangeLogEntry> lstCL = new ArrayList<ChangeLogEntry>();
		if (getChangeLogProcessors() != null) {
			for (IChangeLogProcessor processor : m_CLServices) {
				lstCL.addAll(processor.getAllChangeLogEntries(strObjectClass, strPK));
			}
		}
		return lstCL;
	}
}
