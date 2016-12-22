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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.json.DefinitionFactory;
import org.openntf.xpt.core.json.JSONEmptyValueStrategy;
import org.openntf.xpt.core.json.binding.BinderProcessParameter;
import org.openntf.xpt.core.json.binding.IJSONBinder;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;

public class ListBinder extends AbstractBaseBinder<List<?>>implements IJSONBinder<List<?>> {
	private static ListBinder m_Binder = new ListBinder();

	private ListBinder() {

	}

	public static ListBinder getInstance() {
		return m_Binder;
	}

	public void process2JSON(BinderProcessParameter parameter) {
		// TODO: Resolve direct dependency
		try {
			List<?> lstValues = getValue(parameter.getObject(), parameter.getJavaField());
			IJSONBinder<?> innerBinder = DefinitionFactory.getJSONBinder(parameter.getJsonBinderContainer(), parameter.getContainerClass());
			if (parameter.getStrategy() != JSONEmptyValueStrategy.NOPROPERTY || (lstValues != null && lstValues.size() > 0)) {
				parameter.getWriter().startProperty(parameter.getJsonProperty());
				if (lstValues != null && lstValues.size() > 0) {
					parameter.getWriter().startArray();
					for (Object obj : lstValues) {
						parameter.getWriter().startArrayItem();
						innerBinder.processValue2JSON(parameter, obj);
						parameter.getWriter().endArrayItem();
					}
					parameter.getWriter().endArray();
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
			// TODO: Implement List in List
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void processJson2Value(BinderProcessParameter parameter) {
		JsonJavaFactory factory = JsonJavaFactory.instanceEx;
		IJSONBinder<?> innerBinder = DefinitionFactory.getJSONBinder(parameter.getJsonBinderContainer(), parameter.getContainerClass());
		try {
			Object arrDates = factory.getProperty(parameter.getJson(), parameter.getJsonProperty());
			List<?> values = buildValues(arrDates, innerBinder, parameter, factory);
			setValue(parameter.getObject(), parameter.getJavaField(), values, List.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private List<?> buildValues(Object arrDates, IJSONBinder<?> innerBinder, BinderProcessParameter parameter, JsonFactory factory)
			throws JsonException, InstantiationException, IllegalAccessException {
		if (innerBinder instanceof BusinessObjectBinder) {
			return buildBusinessValues(arrDates, parameter, factory);
		} else {
			return buildSimpleList(arrDates, factory);
		}
	}

	private List<?> buildSimpleList(Object jsonArray, JsonFactory factory) throws JsonException {
		List<Object> values = new ArrayList<Object>();
		for (Iterator<Object> itValues = factory.iterateArrayValues(jsonArray); itValues.hasNext();) {
			values.add(itValues.next());
		}
		return values;
	}

	private List<?> buildBusinessValues(Object jsonArray, BinderProcessParameter parameter, JsonFactory factory)
			throws JsonException, InstantiationException, IllegalAccessException {
		List<Object> values = new ArrayList<Object>();
		for (Iterator<Object> itObject = factory.iterateArrayValues(jsonArray); itObject.hasNext();) {

			JsonJavaObject jsonObject = (JsonJavaObject) itObject.next();
			Object element = parameter.getContainerClass().newInstance();
			parameter.getJsonBinderContainer().processJson2Object(jsonObject, element);
			values.add(element);
		}
		return values;
	}

}
