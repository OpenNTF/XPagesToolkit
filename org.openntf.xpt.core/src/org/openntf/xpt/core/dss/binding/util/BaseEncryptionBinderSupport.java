/*
 * � Copyright WebGate Consulting AG, 2013
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import lotus.domino.Database;

import org.openntf.xpt.core.dss.binding.Definition;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.designer.runtime.Application;

public enum BaseEncryptionBinderSupport {
	INSTANCE;

	private static final ProfilerType PROFILERTYPE = new ProfilerType("XPT.DSS.ENCRYPTIONBINDERSUPPORT");
	private static final String ORG_OPENNTF_XPT_CORE_DSS_END_USERMAP = "org.openntf.xpt.core.dss.enc.usermap";

	private static final List<String>EMPTYLIST = new LinkedList<String>();
	
	public boolean hasAccess(Definition def, Database ndbSource) {
		try {
			List<String> roles = getRolesForUser(ndbSource);
			return hasAccess(def, roles);
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

	private List<String> getRolesForUser(Database ndbSource) {
		if (Profiler.isEnabled()) {
			ProfilerAggregator pa = Profiler.startProfileBlock(PROFILERTYPE, "getRolesForUser");
			long startTime = Profiler.getCurrentTime();
			try {
				return _getRolesForUser(ndbSource);
			} finally {
				Profiler.endProfileBlock(pa, startTime);
			}

		} else {
			return _getRolesForUser(ndbSource);
		}
	}

	private List<String> _getRolesForUser(Database ndbSource) {
		try {
			String strUser = ndbSource.getParent().getEffectiveUserName();
			Map<String, List<String>> allRoles = getUserMap();
			String mapKey = strUser + "@@@" + ndbSource.getReplicaID();
			if (allRoles.containsKey(mapKey)) {
				return allRoles.get(mapKey);
			} else {
				@SuppressWarnings("unchecked")
				Vector<String> roles = ndbSource.queryAccessRoles(strUser);
				List<String> rolesList = new ArrayList<String>();
				rolesList.addAll(roles);
				allRoles.put(mapKey, rolesList);
				return rolesList;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return EMPTYLIST;
	}

	public synchronized Map<String, List<String>> getUserMap() {
		@SuppressWarnings("unchecked")
		Map<String, List<String>> userMap = (Map<String, List<String>>) Application.get().getObject(ORG_OPENNTF_XPT_CORE_DSS_END_USERMAP);
		if (userMap == null) {
			userMap = new ConcurrentHashMap<String, List<String>>();
			Application.get().putObject(ORG_OPENNTF_XPT_CORE_DSS_END_USERMAP, userMap);
		}
		return userMap;
	}

}
