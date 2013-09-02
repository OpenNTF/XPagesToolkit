package org.openntf.xpt.demo.api;

import org.openntf.xpt.core.dss.DominoStorageService;
import org.openntf.xpt.demo.UserProfile;
import org.openntf.xpt.demo.UserStorageService;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class UserSessionFacade {

	public UserProfile getMyUser() {
		UserProfile uRC = new UserProfile();
		try {
			DominoStorageService.getInstance().getObject(uRC, ExtLibUtil.getCurrentSession().getEffectiveUserName(),
					ExtLibUtil.getCurrentSession().getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uRC;
	}

	public void saveUser(UserProfile usp) {
		UserStorageService.getInstance().saveUser(usp);
	}
}
