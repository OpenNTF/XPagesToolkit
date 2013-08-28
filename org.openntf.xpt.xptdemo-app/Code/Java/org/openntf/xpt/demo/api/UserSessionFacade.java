package org.openntf.xpt.demo.api;

import org.openntf.xpt.demo.UserProfile;
import org.openntf.xpt.demo.UserStorageService;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class UserSessionFacade {

	public UserProfile createMyUser() {
		UserProfile uRC = new UserProfile();
		try {
			uRC.setUserName(ExtLibUtil.getCurrentSession().getEffectiveUserName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uRC;
	}

	public void saveUser(UserProfile usp) {
		UserStorageService.getInstance().saveUser(usp);
	}
}
