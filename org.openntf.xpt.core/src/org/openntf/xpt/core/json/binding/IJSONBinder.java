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
package org.openntf.xpt.core.json.binding;

import com.ibm.domino.services.util.JsonWriter;

public interface IJSONBinder<T> {

	public void process2JSON(JsonWriter jsWriter, Object objCurrent, String strJSONProperty, String strJAVAField, boolean showEmptyValue,
			Class<?> containerClass);

	public T getValue(Object objCurrent, String strJavaField);

	public void processValue2JSON(JsonWriter jsWriter, Object value);
}
