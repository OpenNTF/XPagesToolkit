package org.openntf.xpt.core.dss.changeLog;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;

import com.ibm.designer.runtime.Application;
import com.ibm.xsp.application.ApplicationEx;

public class ChangeLogService {
	private static final String CL_SERVICE_KEY = "xpt.dss.changelogger"; // $NON-NLS-1$

	private static final String CL_DATAPROVIDER_SERVICE = "org.openntf.core.dss.changelog"; // $NON-NLS-1$

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
			AccessController.doPrivileged(new PrivilegedAction<Void>() {

				@SuppressWarnings("unchecked")
				@Override
				public Void run() {
					m_CLServices = ApplicationEx.getInstance().findServices(CL_DATAPROVIDER_SERVICE);

					return null;
				}
			});
		}
		return m_CLServices;
	}

	public boolean checkChangeLog(Object objCurrent, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField,
			StorageAction action) {
		if (objValueNew == null && objValueNew == null) {
			return false;
		}
		if (objValueNew == null || objValueOld == null) {
			return processChangeLog(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, action);
		}
		if (objValueNew instanceof Comparable<?> && objValueOld instanceof Comparable<?>) {
			@SuppressWarnings("unchecked")
			Comparable<Object> valO1 = (Comparable<Object>) objValueNew;
			@SuppressWarnings("unchecked")
			Comparable<Object> valO2 = (Comparable<Object>) objValueOld;
			if (valO1.compareTo(valO2) == 0) {
				return false;
			}
			return processChangeLog(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, action);
		}
		if (objValueNew.getClass().isArray() && objValueOld.getClass().isArray()) {
			List<?> lstO1 = Arrays.asList(objValueNew);
			List<?> lstO2 = Arrays.asList(objValueOld);
			return compareListValues(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, lstO1, lstO2, action);
		}
		if (objValueNew instanceof List<?> && objValueOld instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<?> lstO1 = (List<Object>) objValueNew;
			@SuppressWarnings("unchecked")
			List<?> lstO2 = (List<Object>) objValueOld;
			return compareListValues(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, lstO1, lstO2, action);
		}

		return false;
	}

	private boolean compareListValues(Object objCurrent, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField, List<?> lstO1,
			List<?> lstO2, StorageAction action) {
		if (lstO1.size() == lstO2.size()) {
			int nCount = 0;
			for (Object objTest : lstO1) {
				Object obj2 = lstO2.get(nCount);
				if (!objTest.equals(obj2)) {
					return processChangeLog(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, action);
				}
				nCount++;
			}

		} else {
			return processChangeLog(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, action);

		}
		return false;
	}

	private boolean processChangeLog(Object objCurrent, Object objValueOld, Object objValueNew, String strObjectMember, String strStorageField,
			StorageAction action) {
		for (IChangeLogProcessor processor : getChangeLogProcessors()) {
			processor.doChangeLog(objCurrent, objValueOld, objValueNew, strObjectMember, strStorageField, action);
		}
		return true;
	}
}
