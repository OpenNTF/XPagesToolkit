/**
 * Copyright 2014, WebGate Consulting AG
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
package org.openntf.xpt.core.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class ValueBindingSupport {

	private ValueBindingSupport() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(T value, String propertyName, ValueBindingObjectImpl uic, T defaultValue, FacesContext context) {
		if (value != null) {
			return value;
		}
		ValueBinding vb = uic.getValueBinding(propertyName);
		if (vb != null) {
			return (T) vb.getValue(context);
		}
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(T value, String propertyName, UIComponent uic, T defaultValue, FacesContext context) {
		if (value != null) {
			return value;
		}
		ValueBinding vb = uic.getValueBinding(propertyName);
		if (vb != null) {
			return (T) vb.getValue(context);
		}
		return defaultValue;
	}

}
