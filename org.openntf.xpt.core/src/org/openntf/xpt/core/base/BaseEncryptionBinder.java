package org.openntf.xpt.core.base;

import java.util.HashMap;

import org.openntf.xpt.core.utils.RoleAndGroupProvider;

public class BaseEncryptionBinder {

	public BaseEncryptionBinder() {
		super();
	}

	public boolean hasAccess(HashMap<String, Object> addValues) {
		if (addValues != null && addValues.size() > 0) {
			if (addValues.containsKey("encRoles")) {
				String[] roles = (String[]) addValues.get("encRoles");
				for (String role : roles) {
					if (RoleAndGroupProvider.getInstance().getMyGroupsAndRoles().contains(role)) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}
}
