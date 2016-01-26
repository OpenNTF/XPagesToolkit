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
package org.openntf.xpt.core.json;

import org.openntf.xpt.core.json.binding.BinderProcessParameter;
import org.openntf.xpt.core.json.binding.IJSONBinder;

public class Definition {

	private final String m_JSONProperty;
	private final String m_JAVAField;
	private JSONEmptyValueStrategy m_EmptyValueStrategy = JSONEmptyValueStrategy.NOPROPERTY;
	private final IJSONBinder<?> m_Binder;
	private final Class<?> m_ContainerClass;

	public Definition(String property, String field,JSONEmptyValueStrategy strategy, IJSONBinder<?> binder, Class<?> containterClass) {
		super();
		m_JSONProperty = property;
		m_JAVAField = field;
		m_EmptyValueStrategy = strategy;
		m_Binder = binder;
		m_ContainerClass = containterClass;
	}

	public void process2JSON(BinderProcessParameter parameter) {
		m_Binder.process2JSON(parameter.applyDefinition(this));
	}

	public JSONEmptyValueStrategy getEmptyValueStrategy() {
		return m_EmptyValueStrategy;
	}

	public String getJSONProperty() {
		return m_JSONProperty;
	}

	public String getJAVAField() {
		return m_JAVAField;
	}

	public Class<?> getContainerClass() {
		return m_ContainerClass;
	}
	
	
}
