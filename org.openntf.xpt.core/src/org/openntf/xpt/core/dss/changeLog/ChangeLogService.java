package org.openntf.xpt.core.dss.changeLog;

import java.security.AccessController;
import java.security.PrivilegedAction;
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
}
