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
package org.openntf.xpt.core.json.binding;

import java.util.List;

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.json.DefinitionFactory;
import org.openntf.xpt.core.json.JSONEmptyValueStrategy;

import com.ibm.domino.services.util.JsonWriter;

public class ListBinder extends AbstractBaseBinder<List<?>> implements IJSONBinder<List<?>> {
	private static ListBinder m_Binder;

	private ListBinder() {

	}

	public static ListBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new ListBinder();
		}
		return m_Binder;
	}

	public void process2JSON(JsonWriter jsWriter, Object objCurrent, String strJSONProperty, String strJAVAField, JSONEmptyValueStrategy strategy, Class<?> containerClass) {
		try {
			List<?> lstValues = getValue(objCurrent, strJAVAField);
			IJSONBinder<?> innerBinder = DefinitionFactory.getJSONBinder(containerClass);
			if (strategy != JSONEmptyValueStrategy.NOPROPERTY || (lstValues != null && lstValues.size() > 0)) {
				jsWriter.startProperty(strJSONProperty);
				if (lstValues != null && lstValues.size() > 0) {
					jsWriter.startArray();
					for (Object obj : lstValues) {
						jsWriter.startArrayItem();
						innerBinder.processValue2JSON(jsWriter, obj);
						jsWriter.endArrayItem();
					}
					jsWriter.endArray();
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
			// TODO: Implement List in List
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
