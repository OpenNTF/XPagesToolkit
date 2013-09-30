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
package org.openntf.xpt.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.ibm.designer.runtime.directory.DirectoryUser;
import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.extlib.util.ExtLibUtil;

import lotus.domino.Database;
import lotus.domino.Name;

public class RoleAndGroupProvider {
	private static RoleAndGroupProvider m_Provider;

	private RoleAndGroupProvider() {

	}

	public static RoleAndGroupProvider getInstance() {
		if (m_Provider == null) {
			m_Provider = new RoleAndGroupProvider();
		}
		return m_Provider;
	}

	@SuppressWarnings("unchecked")
	public List<String> getMyGroupsAndRoles() {
		List<String> lstRC = new ArrayList<String>();
		try {
			XSPContext xsp = ExtLibUtil.getXspContext();
			DirectoryUser dirUser = xsp.getUser();
			Name nonUser = ExtLibUtil.getCurrentSession().createName(ExtLibUtil.getCurrentSession().getEffectiveUserName());
			lstRC.add(nonUser.getCanonical());
			lstRC.add(nonUser.getAbbreviated());
			lstRC.add(nonUser.getCommon());
			lstRC.addAll(dirUser.getGroups());
			lstRC.addAll(dirUser.getRoles());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;

	}

	@SuppressWarnings("unchecked")
	public List<String> getGroupsAndRolesOf(String strUser, Database ndbTarget) {
		List<String> lstRC = new ArrayList<String>();
		try {
			Name nonUser = ExtLibUtil.getCurrentSession().createName(strUser);
			lstRC.add(nonUser.getCanonical());
			lstRC.add(nonUser.getAbbreviated());
			lstRC.add(nonUser.getCommon());
			lstRC.addAll(new lotus.notes.addins.DominoServer(ndbTarget.getServer()).getNamesList(nonUser.getCanonical()));
			lstRC.addAll(ndbTarget.queryAccessRoles(nonUser.getAbbreviated()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;

	}
}
