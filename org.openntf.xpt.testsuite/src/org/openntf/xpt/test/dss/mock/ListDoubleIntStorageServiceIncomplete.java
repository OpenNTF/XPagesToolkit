package org.openntf.xpt.test.dss.mock;

import org.openntf.xpt.core.dss.AbstractStorageService;

public class ListDoubleIntStorageServiceIncomplete extends AbstractStorageService<ListDoubleIntTestMock> {
	private static ListDoubleIntStorageServiceIncomplete m_Service = new ListDoubleIntStorageServiceIncomplete();

	public static ListDoubleIntStorageServiceIncomplete getInstance() {
		return m_Service;
	}

	@Override
	protected ListDoubleIntTestMock createObject() {
		return null;
	}

}
