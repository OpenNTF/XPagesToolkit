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

import java.util.Date;

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.json.binding.BinderProcessParameter;
import org.openntf.xpt.core.json.binding.IJSONBinder;
import org.openntf.xpt.core.utils.JSONSupport;

public class DateBinder extends AbstractBaseBinder<Date>implements IJSONBinder<Date> {
	private static DateBinder m_Binder = new DateBinder();

	private DateBinder() {

	}

	public static DateBinder getInstance() {
		return m_Binder;
	}

	public void process2JSON(BinderProcessParameter parameter) {
		try {
			Date datValue = getValue(parameter.getObject(), parameter.getJavaField());
			JSONSupport.writeDate(parameter.getWriter(), parameter.getJsonProperty(), datValue, parameter.getStrategy());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processValue2JSON(BinderProcessParameter parameter, Object value) {
		try {
			parameter.getWriter().outDateLiteral((Date) value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void processJson2Value(BinderProcessParameter parameter) {
		try {
			Date value = parameter.getJson().getJavaDate(parameter.getJsonProperty());
			setValue(parameter.getObject(), parameter.getJavaField(), value, Date.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
