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
package org.openntf.xpt.core.beans;

import java.util.List;

import javax.faces.context.FacesContext;

import lotus.domino.Database;

import org.openntf.xpt.core.utils.RoleAndGroupProvider;
import org.openntf.xpt.core.utils.XPTLibUtils;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.AbstractDataSource;

public class XPTBean {

	public static final String BEAN_NAME = "xptBean"; //$NON-NLS-1$

	public static XPTBean get(FacesContext context) {
		XPTBean bean = (XPTBean) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	public String getXptVersion() {
		return XPTLibUtils.getXptLibVersion();
	}

	public AbstractDataSource findDataSource(String strCompID, String strDSId) {
		return XPTLibUtils.getDatasource(strCompID, strDSId);
	}

	public List<String> getMyGroupsAndRoles() {
		return RoleAndGroupProvider.getInstance().getMyGroupsAndRoles();
	}

	public List<String> getGroupsAndRolesOf(String strUserName) {
		return RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(strUserName, ExtLibUtil.getCurrentDatabase());

	}

	public List<String> getGroupsAndRolesOf(String strUserName, Database ndbTarget) {
		return RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(strUserName, ndbTarget);

	}

}
