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
package org.openntf.xpt.core.json.binding;

import java.lang.reflect.Method;

import org.openntf.xpt.core.json.JSONEmptyValueStrategy;
import org.openntf.xpt.core.json.JSONService;

import com.ibm.domino.services.util.JsonWriter;

public class BusinessObjectBinder implements IJSONBinder<Object> {
	private static BusinessObjectBinder m_Binder;

	private BusinessObjectBinder() {

	}

	public static BusinessObjectBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new BusinessObjectBinder();
		}
		return m_Binder;
	}

	public Object getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (Object) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void process2JSON(JsonWriter jsWriter, Object objCurrent, String strJSONProperty, String strJAVAField, JSONEmptyValueStrategy strategy,
			Class<?> containerClass) {
		try {
			Object boValue = getValue(objCurrent, strJAVAField);
			if (strategy != JSONEmptyValueStrategy.NOPROPERTY || boValue != null) {
				jsWriter.startProperty(strJSONProperty);
				if (boValue != null) {
					JSONService.getInstance().process2JSON(jsWriter, boValue);
				} else {
					strategy.writeJSONValue(jsWriter);
				}
				jsWriter.endProperty();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processValue2JSON(JsonWriter jsWriter, Object value) {
		try {
			JSONService.getInstance().process2JSON(jsWriter, value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
