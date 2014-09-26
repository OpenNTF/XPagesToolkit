/*
 * © Copyright WebGate Consulting AG, 2014
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
package org.openntf.xpt.objectlist.datasource;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.dss.SingleObjectStore;
import org.openntf.xpt.core.utils.ValueBindingSupport;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.binding.MethodBindingEx;
import com.ibm.xsp.model.AbstractDataSource;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.util.StateHolderUtil;

public class DSSObjectDataSource extends AbstractDataSource {

	public static final String[] DSSOBJECT_SSJS_NAME = { "cDSSObject" };
	private String m_DatabaseName;
	private String m_ObjectId;
	private String m_Action;
	private MethodBinding m_ObjectStorageService;
	private MethodBinding m_PostOpen;
	private MethodBinding m_PostNew;
	private MethodBinding m_PostSave;
	private MethodBinding m_QuerySave;

	@Override
	protected String composeUniqueId() {
		StringBuilder sb = new StringBuilder();
		sb.append("dssObjectDataStore");
		String strRequestParam = getRequestParamPrefix();
		if (!StringUtil.isEmpty(strRequestParam)) {
			sb.append(strRequestParam);
		}

		String strDBName = getDatabaseName();
		if (!StringUtil.isEmpty(strDBName)) {
			sb.append(strDBName);
		}

		String strObjectId = getObjectId();
		if (!StringUtil.isEmpty(strObjectId)) {
			sb.append(strObjectId);
		}
		return sb.toString();
	}

	@Override
	public Object getDataObject() {
		DSSObjectDataContainer dc = (DSSObjectDataContainer) getDataContainer();
		return dc == null ? null : dc.getDSSObject();
	}

	@Override
	public boolean isReadonly() {
		return getDataObject() == null;
	}

	@Override
	public DataContainer load(FacesContext context) throws IOException {
		DSSObject dssObject = buildDSSObject(context);
		if (dssObject != null) {
			if (dssObject.isNew()) {
				invoke(context, m_PostNew, dssObject);
			} else {
				invoke(context, m_PostOpen, dssObject);
			}
		}

		return new DSSObjectDataContainer(getBeanId(), composeUniqueId(), dssObject);
	}

	@Override
	public void readRequestParams(FacesContext arg0, Map<String, Object> paramMap) {
		String strDatabase = (String) paramMap.get(prefixRequestParam("databaseName"));
		if (StringUtil.isNotEmpty(strDatabase)) {
			m_DatabaseName = strDatabase;
		}
		String strObjectId = (String) paramMap.get(prefixRequestParam("objectId"));
		if (StringUtil.isNotEmpty(strObjectId)) {
			m_ObjectId = strObjectId;
		}
		String strAction = (String) paramMap.get(prefixRequestParam("action"));
		if (StringUtil.isNotEmpty(strAction)) {
			m_Action = strAction;
		}
	}

	@Override
	public boolean save(FacesContext context, DataContainer container) throws FacesExceptionEx {
		DSSObject dssObject = ((DSSObjectDataContainer) container).getDSSObject();
		SingleObjectStore<?> sos = getObjectStore();
		invoke(context, m_QuerySave, dssObject);
		if (sos.saveObject(dssObject.getBO(), getDatabaseName())) {
			invoke(context, m_PostSave, dssObject);
			return true;
		}
		return false;
	}

	public String getDatabaseName() {
		return ValueBindingSupport.getValue(m_DatabaseName, "databaseName", this, "", getFacesContext());
	}

	public void setDatabaseName(String databaseName) {
		m_DatabaseName = databaseName;
	}

	public String getObjectId() {
		return ValueBindingSupport.getValue(m_ObjectId, "objectId", this, "", getFacesContext());
	}

	public void setObjectId(String objectId) {
		m_ObjectId = objectId;
	}

	public String getAction() {
		return ValueBindingSupport.getValue(m_Action, "action", this, "", getFacesContext());
	}

	public void setAction(String action) {
		m_Action = action;
	}

	public MethodBinding getObjectStorageService() {
		return m_ObjectStorageService;
	}

	public void setObjectStorageService(MethodBinding objectStorageService) {
		m_ObjectStorageService = objectStorageService;
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[9];
		values[0] = super.saveState(context);
		values[1] = m_DatabaseName;
		values[2] = m_ObjectId;
		values[3] = m_Action;
		values[4] = StateHolderUtil.saveMethodBinding(context, m_ObjectStorageService);
		values[5] = StateHolderUtil.saveMethodBinding(context, m_PostNew);
		values[6] = StateHolderUtil.saveMethodBinding(context, m_PostOpen);
		values[7] = StateHolderUtil.saveMethodBinding(context, m_PostSave);
		values[8] = StateHolderUtil.saveMethodBinding(context, m_QuerySave);
		
		return values;
	}

	@Override
	public void restoreState(FacesContext context, Object val) {
		Object[] values = (Object[]) val;
		super.restoreState(context, values[0]);
		m_DatabaseName = (String) values[1];
		m_ObjectId = (String) values[2];
		m_Action = (String) values[3];
		m_ObjectStorageService = StateHolderUtil.restoreMethodBinding(context, getComponent(), values[4]);
		m_PostNew = StateHolderUtil.restoreMethodBinding(context, getComponent(), values[5]);
		m_PostOpen = StateHolderUtil.restoreMethodBinding(context, getComponent(), values[6]);
		m_PostSave = StateHolderUtil.restoreMethodBinding(context, getComponent(), values[7]);
		m_QuerySave = StateHolderUtil.restoreMethodBinding(context, getComponent(), values[8]);
	}

	private DSSObject buildDSSObject(FacesContext context) {
		String strAction = getAction();
		String strObjectID = getObjectId();
		SingleObjectStore<?> sos = getObjectStore();
		Object obj = null;
		boolean isEdit = false;
		boolean isNew = false;
		if (StringUtil.isEmpty(strAction) || "newObject".equals(strAction)) {
			obj = sos.newObject();
			isEdit = true;
			isNew = true;
		} else {
			if (!StringUtil.isEmpty(strObjectID)) {
				obj = sos.getObjectByID(strObjectID, getDatabaseName());
				if ("editObject".equals(strAction)) {
					isEdit = true;
				}
			}
		}
		return obj == null ? null : new DSSObject(obj, isEdit, isNew);
	}

	private SingleObjectStore<?> getObjectStore() {
		try {

			Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
			if (m_ObjectStorageService != null) {
				logCurrent.info("Exectue BuildValues");
				Object objCurrent = m_ObjectStorageService.invoke(getFacesContext(), null);
				logCurrent.info(objCurrent.getClass().getCanonicalName());
				if (objCurrent instanceof SingleObjectStore<?>) {
					return (SingleObjectStore<?>) objCurrent;
				}
			}
			throw new XPTRuntimeException("No ObjectStorageService defined!");

		} catch (Exception ex) {
			throw new XPTRuntimeException("Error during getObjectStore (SingleObjectStore)", ex);
		}
	}

	private void invoke(FacesContext context, MethodBinding method, DSSObject dssObject) {
		if (method != null) {
			Object[] params = null;
			if (method instanceof MethodBindingEx) {
				params = new Object[] { dssObject.getBO() };
				((MethodBindingEx) method).setComponent(this.getComponent());
				((MethodBindingEx) method).setParamNames(DSSOBJECT_SSJS_NAME);
			}
			method.invoke(context, params);
		}
	}

	public MethodBinding getPostOpen() {
		return m_PostOpen;
	}

	public void setPostOpen(MethodBinding postOpen) {
		m_PostOpen = postOpen;
	}

	public MethodBinding getPostNew() {
		return m_PostNew;
	}

	public void setPostNew(MethodBinding postNew) {
		m_PostNew = postNew;
	}

	public MethodBinding getPostSave() {
		return m_PostSave;
	}

	public void setPostSave(MethodBinding postSave) {
		m_PostSave = postSave;
	}

	public MethodBinding getQuerySave() {
		return m_QuerySave;
	}

	public void setQuerySave(MethodBinding querySave) {
		m_QuerySave = querySave;
	}

}
