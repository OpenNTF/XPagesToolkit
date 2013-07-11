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
package org.openntf.xpt.core.json;

import org.openntf.xpt.core.json.binding.IJSONBinder;

import com.ibm.domino.services.util.JsonWriter;

public class Definition {

	private String m_JSONProperty;
	private String m_JAVAField;
	private boolean m_showEmptyValues;
	private IJSONBinder<?> m_Binder;
	private Class<?> m_ContainerClass;

	public Definition(String property, String field, boolean showEmptyValues, IJSONBinder<?> binder, Class<?> containterClass) {
		super();
		m_JSONProperty = property;
		m_JAVAField = field;
		m_showEmptyValues = showEmptyValues;
		m_Binder = binder;
		m_ContainerClass = containterClass;
	}

	public void process2JSON(JsonWriter jsWriter, Object objCurrent) {
		m_Binder.process2JSON(jsWriter, objCurrent, m_JSONProperty, m_JAVAField, m_showEmptyValues, m_ContainerClass);
	}
}
