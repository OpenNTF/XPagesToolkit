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

import com.ibm.xsp.http.MimeMultipart;

public class MimeMultipartBinder extends AbstractBaseBinder<MimeMultipart> implements IJSONBinder<MimeMultipart> {
	private static MimeMultipartBinder m_Binder = new MimeMultipartBinder();

	private MimeMultipartBinder() {

	}

	public static MimeMultipartBinder getInstance() {
		return m_Binder;
	}

	@Override
	public void process2JSON(BinderProcessParameter parameter) {
		try {
			MimeMultipart mpart = getValue(parameter.getObject(), parameter.getJavaField());
			String strValue = mpart == null ? null : mpart.getContentAsText();
			JSONSupport.writeString(parameter.getWriter(), parameter.getJsonProperty(), strValue, parameter.getStrategy());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processValue2JSON(BinderProcessParameter parameter, Object value) {
		try {
			parameter.getWriter().outStringLiteral((String) value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void processJson2Value(BinderProcessParameter parameter) {
		String value = parameter.getJson().getString(parameter.getJsonProperty());
		MimeMultipart mp = MimeMultipart.fromHTML(value);
		setValue(parameter.getObject(), parameter.getJavaField(), mp, MimeMultipart.class);

	}

}