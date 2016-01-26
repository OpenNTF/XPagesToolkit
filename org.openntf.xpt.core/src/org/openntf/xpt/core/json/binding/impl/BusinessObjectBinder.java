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
package org.openntf.xpt.core.json.binding.impl;

import java.lang.reflect.Method;

import org.openntf.xpt.core.json.JSONEmptyValueStrategy;
import org.openntf.xpt.core.json.binding.BinderProcessParameter;
import org.openntf.xpt.core.json.binding.IJSONBinder;

public class BusinessObjectBinder implements IJSONBinder<Object> {
	private static final BusinessObjectBinder m_Binder = new BusinessObjectBinder();

	private BusinessObjectBinder() {

	}

	public static BusinessObjectBinder getInstance() {
		return m_Binder;
	}
	@Override
	public Object getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void process2JSON(BinderProcessParameter parameter) {
		try {
			Object boValue = getValue(parameter.getObject(), parameter.getJavaField());
			if (parameter.getStrategy() != JSONEmptyValueStrategy.NOPROPERTY || boValue != null) {
				parameter.getWriter().startProperty(parameter.getJsonProperty());
				if (boValue != null) {
					parameter.getJsonBinderContainer().process2JSON(parameter.getWriter(), boValue);
				} else {
					parameter.getStrategy().writeJSONValue(parameter.getWriter());
				}
				parameter.getWriter().endProperty();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processValue2JSON(BinderProcessParameter parameter, Object value) {
		try {
			parameter.getJsonBinderContainer().process2JSON(parameter.getWriter(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
