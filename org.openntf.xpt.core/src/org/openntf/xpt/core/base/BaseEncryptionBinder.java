package org.openntf.xpt.core.base;

import java.util.HashMap;
import java.util.List;

import org.openntf.xpt.core.utils.RoleAndGroupProvider;

public class BaseEncryptionBinder {

	public BaseEncryptionBinder() {
		super();
	}

	public boolean hasAccess(HashMap<String, Object> addValues) {
		return hasAccess(addValues, RoleAndGroupProvider.getInstance().getMyGroupsAndRoles());
	}
	
	public boolean hasAccess(HashMap<String, Object> addValues, List<String> myRoles) {
		if (addValues != null && addValues.size() > 0) {
			if (addValues.containsKey("encRoles")) {
				String[] roles = (String[]) addValues.get("encRoles");
				for (String role : roles) {
					if (myRoles.contains(role)) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}
}
