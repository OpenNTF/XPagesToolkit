/**
 * Copyright 2013, WebGate Consulting AG
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

import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Document;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.encryption.EncryptionDateBinder;
import org.openntf.xpt.core.dss.binding.encryption.EncryptionDoubleBinder;
import org.openntf.xpt.core.dss.binding.encryption.EncryptionStringBinder;
import org.openntf.xpt.core.utils.RoleAndGroupProvider;
import org.openntf.xpt.core.utils.XPTLibUtils;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.AbstractDataSource;

/**
 * xptBean delivers a various set of useful shortcuts to methods.
 * 
 * @author Christian Guedemann
 * 
 */
public final class XPTBean {

	public static final String BEAN_NAME = "xptBean"; //$NON-NLS-1$

	public static XPTBean get(FacesContext context) {
		return (XPTBean) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
	}

	public static XPTBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	/**
	 * Version of the XPagesToolkit
	 * 
	 * @return version as string based on the bundle version of the
	 *         XPTCorePlugin
	 */
	public String getXptVersion() {
		return XPTLibUtils.getXptLibVersion();
	}

	/**
	 * Finds a data source by searching direct in the named panel
	 * 
	 * @param panelId
	 * @param dateSourceName
	 * @return the data source or null if there is no datasource in the panel
	 *         with the specified datasource name
	 */
	public AbstractDataSource findDataSource(String panelId, String dateSourceName) {
		return XPTLibUtils.getDatasource(panelId, dateSourceName, false);
	}

	/**
	 * Finds a data source by searching direct in the named panel and then top
	 * down
	 * 
	 * @param panelId
	 * @param datasSourceName
	 * @return the data source or null if there is no data source in the panel
	 *         with the specified data source name
	 */
	public AbstractDataSource findDataSourceDeep(String panelId, String datasSourceName) {
		return XPTLibUtils.getDatasource(panelId, datasSourceName, true);
	}

	/**
	 * All groups and roles of the current user
	 * 
	 * @return A list with all groups and roles of the current user
	 */
	public List<String> getMyGroupsAndRoles() {
		return RoleAndGroupProvider.getInstance().getMyGroupsAndRoles();
	}

	/**
	 * All groups and roles of a specified user
	 * 
	 * @param userName
	 * @return A list with all groups and roles of the specified user
	 */
	public List<String> getGroupsAndRolesOf(String userName) {
		return RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(userName, ExtLibUtil.getCurrentDatabase());

	}

	/**
	 * All groups an roles of specified user in a defined database
	 * 
	 * @param userName
	 * @param targetDatabase
	 * @return A list with all groups and roles
	 */
	public List<String> getGroupsAndRolesOf(String userName, Database targetDatabase) {
		return RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(userName, targetDatabase);

	}

	/**
	 * Unencrypt a value of a field in a document, based on the roles
	 * 
	 * @param doc
	 * @param fieldName
	 * @param roles
	 * @return Unencrypted value if allowd by the roles, otherwise a blank
	 *         string
	 */
	public String getDecryptedStringValue(Document doc, String fieldName, String[] roles) {
		Definition def = Definition.buildDefinition4Decryption(fieldName, roles, false, EncryptionStringBinder.getInstance());
		try {
			return EncryptionStringBinder.getInstance().getValueFromStore(doc, doc.getItemValue(fieldName), def);
		} catch (Exception ex) {
			throw new XPTRuntimeException(ex);
		}
	}

	/**
	 * Unencrypt a value of a field in a document, based on the roles
	 * 
	 * @param doc
	 * @param fieldName
	 * @param roles
	 * @param dateOnly
	 * @return Unencrypted date if allowed by the roles, otherwise null
	 */
	public Date getDecryptedDateValue(Document doc, String fieldName, String[] roles, boolean dateOnly) {
		Definition def = Definition.buildDefinition4Decryption(fieldName, roles, dateOnly, EncryptionDateBinder.getInstance());
		try {
			return EncryptionDateBinder.getInstance().getValueFromStore(doc, doc.getItemValue(fieldName), def);
		} catch (Exception ex) {
			throw new XPTRuntimeException(ex);
		}
	}

	/**
	 * Unencrypt a value of a field in a document, based on the roles
	 * 
	 * @param doc
	 * @param fieldName
	 * @param roles
	 * @return Unencrypted Double if allowed by the roles, otherwise null
	 */
	public Double getDecryptedDoubleValue(Document doc, String fieldName, String[] roles) {
		Definition def = Definition.buildDefinition4Decryption(fieldName, roles, false, EncryptionDoubleBinder.getInstance());
		try {
			return EncryptionDoubleBinder.getInstance().getValueFromStore(doc, doc.getItemValue(fieldName), def);
		} catch (Exception ex) {
			throw new XPTRuntimeException(ex);
		}
	}

}
