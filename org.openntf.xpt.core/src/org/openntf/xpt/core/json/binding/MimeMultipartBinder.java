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
package org.openntf.xpt.core.json.binding;

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.json.JSONEmptyValueStrategy;
import org.openntf.xpt.core.utils.JSONSupport;

import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.http.MimeMultipart;

public class MimeMultipartBinder extends AbstractBaseBinder<MimeMultipart> implements IJSONBinder<MimeMultipart> {
	private static MimeMultipartBinder m_Binder;

	private MimeMultipartBinder() {

	}

	public static MimeMultipartBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new MimeMultipartBinder();
		}
		return m_Binder;
	}

	public void process2JSON(JsonWriter jsWriter, Object objCurrent, String strJSONProperty, String strJAVAField,JSONEmptyValueStrategy strategy, Class<?> containerClass) {
		try {
			MimeMultipart mpart = getValue(objCurrent, strJAVAField);
			String strValue = mpart == null ? null : mpart.getContentAsText();
			JSONSupport.writeString(jsWriter, strJSONProperty, strValue, strategy);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processValue2JSON(JsonWriter jsWriter, Object value) {
		try {
			jsWriter.outStringLiteral((String) value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}