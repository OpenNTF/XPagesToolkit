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
package org.openntf.xpt.core.dss.binding.util;

import java.util.List;

import lotus.domino.Database;

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.utils.RoleAndGroupProvider;

public enum BaseEncryptionBinderSupport {
	INSTANCE;

	public boolean hasAccess(Definition def, Database ndbSource) {
		try {
			String strUser = ndbSource.getParent().getEffectiveUserName();
			return hasAccess(def, RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(strUser, ndbSource));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean hasAccess(Definition def, List<String> myRoles) {
		if (def.getEncRoles() != null) {
			String[] roles = def.getEncRoles();
			for (String role : roles) {
				if (myRoles.contains(role)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
