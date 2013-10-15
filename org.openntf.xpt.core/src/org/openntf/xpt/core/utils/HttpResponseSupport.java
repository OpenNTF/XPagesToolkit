/*
 * © Copyright WebGate Consulting AG, 2012
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

import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.services.HttpServiceConstants;
import com.ibm.domino.services.rest.RestServiceEngine;

public class HttpResponseSupport {

	public static void setJSONUTF8ContentType(HttpServletResponse resp) {
		resp.setContentType(
				HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON_UTF8);
	}

	public static void setBINARYContentType(HttpServletResponse resp) {
		resp.setContentType(
				HttpServiceConstants.CONTENTTYPE_BINARY);
	}


	public static void setHTMLUTF8ContentType(HttpServletResponse resp) {
		resp.setContentType(
				HttpServiceConstants.CONTENTTYPE_TEXT_HTML_UTF8);
	}

	
	public static void setJSONUTF8ContentType(RestServiceEngine engine) {
		engine.getHttpResponse().setContentType(
				HttpServiceConstants.CONTENTTYPE_APPLICATION_JSON_UTF8);
		try {
			engine.getHttpRequest().setCharacterEncoding(
					HttpServiceConstants.ENCODING_UTF8);
		} catch (Exception e) {
		}
	}

	public static void setBINARYContentType(RestServiceEngine engine) {
		engine.getHttpResponse().setContentType(
				HttpServiceConstants.CONTENTTYPE_BINARY);
		try {
			engine.getHttpRequest().setCharacterEncoding(
					HttpServiceConstants.ENCODING_BINARY);
		} catch (Exception e) {
		}
	}

	public static void setHTMLUTF8ContentType(RestServiceEngine engine) {
		engine.getHttpResponse().setContentType(
				HttpServiceConstants.CONTENTTYPE_TEXT_HTML_UTF8);
		try {
			engine.getHttpRequest().setCharacterEncoding(
					HttpServiceConstants.ENCODING_UTF8);
		} catch (Exception e) {
		}
	}
}
