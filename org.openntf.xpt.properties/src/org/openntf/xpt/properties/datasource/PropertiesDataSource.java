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
package org.openntf.xpt.properties.datasource;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.model.AbstractDataSource;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.model.DataSource;

public class PropertiesDataSource extends AbstractDataSource implements
		DataSource {

	private String m_DatabaseName;
	private String m_FileName;

	@Override
	protected String composeUniqueId() {
		StringBuilder sb = new StringBuilder();
		sb.append("PROPS|");
		String strPrefix = getRequestParamPrefix();
		if (!StringUtil.isEmpty(strPrefix)) {
			sb.append(strPrefix);
			sb.append("|");
		}
		sb.append(getDatabaseName() + "/" + getFileName());
		return sb.toString();
	}

	@Override
	public Object getDataObject() {
		return ((PropertiesDataContainer) getDataContainer()).getPDO();
	}

	@Override
	public boolean isReadonly() {
		return false;
	}

	@Override
	public DataContainer load(FacesContext arg0) throws IOException {
		return new PropertiesDataContainer(getBeanId(), getUniqueId(),
				getDatabaseName(), getFileName());
	}

	@Override
	public void readRequestParams(FacesContext arg0,
			Map<String, Object> requestMap) {
		String strDB = (String) requestMap
				.get(prefixRequestParam("propertiesDB"));
		String strFile = (String) requestMap
				.get(prefixRequestParam("propertiesFile"));
		if (!StringUtil.isEmpty(strDB)) {
			m_DatabaseName = strDB;
		}
		if (!StringUtil.isEmpty(strFile)) {
			m_FileName = strFile;
		}
	}

	@Override
	public boolean save(FacesContext arg0, DataContainer arg1)
			throws FacesExceptionEx {
		return ((PropertiesDataContainer) getDataContainer()).executeSave();
	}

	public String getDatabaseName() {
		if (m_DatabaseName != null) {
			return m_DatabaseName;
		}
		ValueBinding vb = getValueBinding("databaseName");
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		}
		return null;
	}

	public void setDatabaseName(String databaseName) {
		m_DatabaseName = databaseName;
	}

	public String getFileName() {
		if (m_FileName != null) {
			return m_FileName;
		}
		ValueBinding vb = getValueBinding("fileName");
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		}
		return null;
	}

	public void setFileName(String fileName) {
		m_FileName = fileName;
	}

	// SAVE and RESTOR of Datas
	@Override
	public Object saveState(FacesContext arg0) {
		Object[] state = new Object[3];
		state[0] = super.saveState(arg0);
		state[1] = m_DatabaseName;
		state[2] = m_FileName;
		return state;
	}

	@Override
	public void restoreState(FacesContext arg0, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(arg0, values[0]);
		m_DatabaseName = (String) values[1];
		m_FileName = (String) values[2];
	}

}
