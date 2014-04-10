package org.openntf.xpt.objectlist.datasource;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.openntf.xpt.core.utils.ValueBindingSupport;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.model.AbstractDataSource;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.util.StateHolderUtil;

public class DSSObjectDataSource extends AbstractDataSource {

	private String m_DatabaseName;
	private String m_ObjectId;
	private String m_Action;
	private MethodBinding m_ObjectStorageService;

	public DSSObjectDataSource() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String composeUniqueId() {
		StringBuilder sb = new StringBuilder();
		sb.append("dassObjectDataStore");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadonly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataContainer load(FacesContext arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
			m_ObjectId = strAction;
		}

	}

	@Override
	public boolean save(FacesContext arg0, DataContainer arg1) throws FacesExceptionEx {
		// TODO Auto-generated method stub
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
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = m_DatabaseName;
		values[2] = m_ObjectId;
		values[3] = m_Action;
		values[4] = StateHolderUtil.saveMethodBinding(context, m_ObjectStorageService);
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
	}
}
