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

import com.ibm.designer.runtime.Application;
import com.ibm.domino.services.util.JsonWriter;

public class JSONService {

	private static final String ORG_OPENNTF_XPT_CORE_JSON_SERVICE = "org.openntf.xpt.core.json.JsonService";

	private final JsonBinderContainer container = new JsonBinderContainer();

	private JSONService() {

	}

	public static JSONService getTestInstance() {
		return new JSONService();
	}

	public static JSONService getInstance() {
		JSONService js = (JSONService) Application.get().getObject(ORG_OPENNTF_XPT_CORE_JSON_SERVICE);
		if (js == null) {
			js = new JSONService();
			Application.get().putObject(ORG_OPENNTF_XPT_CORE_JSON_SERVICE, js);
		}
		return js;
	}

	public int process2JSON(JsonWriter jsWriter, Object obj) {
		return container.process2JSON(jsWriter, obj);
	}

	public boolean hasBinderDefinition(Class<?> cl) {
		return container.hasBinderDefinition(cl);

	}

}
