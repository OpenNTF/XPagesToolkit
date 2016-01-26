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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.ibm.xsp.http.MimeMultipart;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;
import org.openntf.xpt.core.json.binding.BooleanBinder;
import org.openntf.xpt.core.json.binding.BusinessObjectBinder;
import org.openntf.xpt.core.json.binding.DateBinder;
import org.openntf.xpt.core.json.binding.DoubleBinder;
import org.openntf.xpt.core.json.binding.IJSONBinder;
import org.openntf.xpt.core.json.binding.IntBinder;
import org.openntf.xpt.core.json.binding.ListBinder;
import org.openntf.xpt.core.json.binding.MimeMultipartBinder;
import org.openntf.xpt.core.json.binding.StringBinder;
import org.openntf.xpt.core.utils.ServiceSupport;

public class DefinitionFactory {

	public static Definition getDefinition(Field fldCurrent, JSONEntity je, JSONObject jo) {

		IJSONBinder<?> jsBinder = getJSONBinder(fldCurrent.getType());
		Class<?> clInner = null;
		if (jsBinder != null) {
			clInner = getInnerClass(fldCurrent);
			Definition defCurrent = new Definition(je.jsonproperty(), ServiceSupport.buildCleanFieldNameCC(jo, fldCurrent.getName()), JSONEmptyValueStrategy.getStrategy(je.showEmptyValue(),
					je.showEmptyValueAsString()), jsBinder, clInner);
			return defCurrent;
		}
		return null;
	}

	private static Class<?> getInnerClass(Field fldCurrent) {
		Class<?> clInner = null;
		if (fldCurrent.getGenericType() instanceof ParameterizedType) {
			Type[] genericTypes = ((ParameterizedType) fldCurrent.getGenericType()).getActualTypeArguments();
			if (genericTypes.length > 0) {
				clInner = (Class<?>) genericTypes[0];
			}
		}
		return clInner;
	}

	public static IJSONBinder<?> getJSONBinder(Class<?> clCurrent) {
		if (clCurrent.equals(Boolean.class) || clCurrent.equals(Boolean.TYPE)) {
			return BooleanBinder.getInstance();
		}
		if (clCurrent.equals(String.class)) {
			return StringBinder.getInstance();
		}
		if (clCurrent.equals(Integer.class) || clCurrent.equals(Integer.TYPE)) {
			return IntBinder.getInstance();
		}
		if (clCurrent.equals(Double.class) || clCurrent.equals(Double.TYPE)) {
			return DoubleBinder.getInstance();
		}
		if (clCurrent.equals(Date.class)) {
			return DateBinder.getInstance();
		}
		if (clCurrent.equals(List.class)) {
			return ListBinder.getInstance();
		}
		if (clCurrent.equals(MimeMultipart.class)) {
			return MimeMultipartBinder.getInstance();
		}
		if (JSONService.getInstance().hasBinderDefinition(clCurrent)) {
			return BusinessObjectBinder.getInstance();
		}
		return null;
	}
}
