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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openntf.xpt.core.XPTRuntimeException;

import com.ibm.xsp.model.DataObject;

public class PropertiesDataObject implements DataObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Properties m_Properties;

	@Override
	public Class<?> getType(Object arg0) {
		Object obj = getValue(arg0);
		if (obj != null) {
			return obj.getClass();
		}
		return null;
	}

	@Override
	public Object getValue(Object arg0) {
		if (m_Properties != null && m_Properties.containsKey("" + arg0)) {
			return m_Properties.getProperty("" + arg0);
		}
		return null;
	}

	@Override
	public boolean isReadOnly(Object arg0) {
		return false;
	}

	@Override
	public void setValue(Object arg0, Object arg1) {
		if (m_Properties != null) {
			m_Properties.setProperty("" + arg0, "" + arg1);
		}
	}



	public void initProperties(InputStream isCurrent) {
		try {
			m_Properties = new Properties();
			m_Properties.load(isCurrent);
		} catch (Exception e) {
			throw new XPTRuntimeException("initProperties failed!",e);
		}
	}
	
	public Properties getProperties() {
		return m_Properties;
	}
	
	public List<String> getPropertieNames() {
		if (m_Properties == null) {
			return null;
		}
		return new ArrayList<String>(m_Properties.stringPropertyNames());
	}

}
