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

	private final String jsonProperty;
	private final String javaField;
	private JSONEmptyValueStrategy emptyValueStrategy = JSONEmptyValueStrategy.NOPROPERTY;
	private final IJSONBinder<?> binder;
	private final Class<?> containerClass;

	public Definition(String property, String field, JSONEmptyValueStrategy strategy, IJSONBinder<?> binder, Class<?> containterClass) {
		super();
		this.jsonProperty = property;
		this.javaField = field;
		this.emptyValueStrategy = strategy;
		this.binder = binder;
		this.containerClass = containterClass;
	}

	public void process2JSON(BinderProcessParameter parameter) {
		binder.process2JSON(parameter.applyDefinition(this));
	}

	public void processJson2Object(BinderProcessParameter parameter) {
		if (parameter.getJson().containsKey(jsonProperty)) {
			binder.processJson2Value(parameter.applyDefinition(this));
		}

	}
	public JSONEmptyValueStrategy getEmptyValueStrategy() {
		return emptyValueStrategy;
	}

	public String getJSONProperty() {
		return jsonProperty;
	}

	public String getJAVAField() {
		return javaField;
	}

	public Class<?> getContainerClass() {
		return containerClass;
	}


}
