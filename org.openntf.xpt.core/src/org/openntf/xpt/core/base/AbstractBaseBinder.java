/*
 * © Copyright WebGate Consulting AG, 2014
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
package org.openntf.xpt.core.base;

import java.lang.reflect.Method;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.utils.ServiceSupport;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

/**
 * Base Class for all binder, to encapsulate the getValue functionality.
 * 
 * @author Christian Guedemann
 * 
 * @param <T>
 */
public abstract class AbstractBaseBinder<T> {

	/**
	 * Get the value from a Object by invoking the getter to the Object. It's
	 * based on the bean convention.
	 * 
	 * @param obj
	 * @param javaFieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getValue(Object obj, String javaFieldName) {
		try {
			Method mt = ServiceSupport.getGetterMethod(obj.getClass(), javaFieldName);
			return (T) mt.invoke(obj);
		} catch (Exception ex) {
			LoggerFactory.logWarning(getClass(), "Error during do getValue()", ex);
			throw new XPTRuntimeException("Error during getValue()", ex);
		}
	}

}
