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
package org.openntf.xpt.objectlist.datasource;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.openntf.xpt.core.utils.ServiceSupport;

import com.ibm.xsp.model.ViewRowData;

public class ObjectListDataEntry implements ViewRowData, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object m_BO;

	public ObjectListDataEntry(Object bO) {
		super();
		m_BO = bO;
	}

	public Object getBO() {
		return m_BO;
	}

	public void setBO(Object bO) {
		m_BO = bO;
	}

	@Override
	public ColumnInfo getColumnInfo(String arg0) {
		return null;
	}

	@Override
	public Object getColumnValue(String arg0) {
		return getValue(arg0);
	}

	@Override
	public String getOpenPageURL(String arg0, boolean arg1) {
		return "#";
	}

	@Override
	public Object getValue(String field) {
		String strGetter = ServiceSupport.makeGetter(field);
		try {
			Method mt = m_BO.getClass().getMethod(strGetter);
			return mt.invoke(m_BO);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public boolean isReadOnly(String arg0) {
		return true;
	}

	@Override
	public void setColumnValue(String arg0, Object arg1) {
	}

}
