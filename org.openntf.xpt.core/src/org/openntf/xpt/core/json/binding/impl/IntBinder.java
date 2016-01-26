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

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.json.binding.BinderProcessParameter;
import org.openntf.xpt.core.json.binding.IJSONBinder;
import org.openntf.xpt.core.utils.JSONSupport;

public class IntBinder extends AbstractBaseBinder<Integer>implements IJSONBinder<Integer> {
	private static IntBinder m_Binder = new IntBinder();

	private IntBinder() {
	}

	public static IntBinder getInstance() {
		return m_Binder;
	}

	@Override
	public void process2JSON(BinderProcessParameter parameter) {
		try {
			Integer nValue = getValue(parameter.getObject(), parameter.getJavaField());
			JSONSupport.writeInt(parameter.getWriter(), parameter.getJsonProperty(), nValue, parameter.getStrategy());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processValue2JSON(BinderProcessParameter parameter, Object value) {
		try {
			parameter.getWriter().outIntLiteral((Integer) value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void processJson2Value(BinderProcessParameter parameter) {
		int value = parameter.getJson().getInt(parameter.getJsonProperty());
		setValue(parameter.getObject(), parameter.getJavaField(), value, Integer.class, Integer.TYPE);
	}

}
