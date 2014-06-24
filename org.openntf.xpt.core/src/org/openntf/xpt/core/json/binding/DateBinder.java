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

import java.util.Date;

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.utils.JSONSupport;

import com.ibm.domino.services.util.JsonWriter;

public class DateBinder extends AbstractBaseBinder<Date> implements IJSONBinder<Date> {
	private static DateBinder m_Binder;

	private DateBinder() {

	}

	public static DateBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new DateBinder();
		}
		return m_Binder;
	}
	public void process2JSON(JsonWriter jsWriter, Object objCurrent, String strJSONProperty, String strJAVAField, boolean showEmptyValue,
			Class<?> containerClass) {
		try {
			Date datValue = getValue(objCurrent, strJAVAField);
			JSONSupport.writeDate(jsWriter, strJSONProperty, datValue, showEmptyValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void processValue2JSON(JsonWriter jsWriter, Object value) {
		try {
			jsWriter.outDateLiteral((Date) value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
