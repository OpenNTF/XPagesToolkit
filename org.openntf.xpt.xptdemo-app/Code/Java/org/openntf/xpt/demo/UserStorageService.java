package org.openntf.xpt.demo;

import org.openntf.xpt.core.dss.DominoStorageService;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class UserStorageService {

	private static UserStorageService m_Service;

	private UserStorageService() {

	}

	public static UserStorageService getInstance() {
		if (m_Service == null) {
			m_Service = new UserStorageService();
		}
		return m_Service;
	}

	public void saveUser(UserProfile usCurrent) {
		try {
			DominoStorageService.getInstance().saveObject(usCurrent, ExtLibUtil.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
