package org.openntf.xpt.test.dss.mock;

import org.openntf.xpt.core.dss.AbstractStorageService;

public class ListDoubleIntStorageServiceComplete extends AbstractStorageService<ListDoubleIntTestMock> {

	private static ListDoubleIntStorageServiceComplete m_Service = new ListDoubleIntStorageServiceComplete();

	public static ListDoubleIntStorageServiceComplete getInstance() {
		return m_Service;
	}

	@Override
	protected ListDoubleIntTestMock createObject() {
		return new ListDoubleIntTestMock();
	}

}
