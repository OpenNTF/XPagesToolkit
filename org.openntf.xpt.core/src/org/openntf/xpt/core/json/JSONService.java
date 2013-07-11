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
package org.openntf.xpt.core.json;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;
import org.openntf.xpt.core.json.binding.Java2JSONBinder;

import com.ibm.designer.runtime.Application;
import com.ibm.domino.services.util.JsonWriter;

public class JSONService {

	private static final String ORG_OPENNTF_XPT_CORE_JSON_SERVICE = "org.openntf.xpt.core.json.JsonService";

	private HashMap<String, Java2JSONBinder> m_Binders = new HashMap<String, Java2JSONBinder>();
	private HashMap<String, JSONObject> m_JSONObject = new HashMap<String, JSONObject>();

	private JSONService() {

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
		if (!m_JSONObject.containsKey(obj.getClass().getCanonicalName())) {
			if (!obj.getClass().isAnnotationPresent(JSONObject.class)) {
				return -1;
			}
			m_JSONObject.put(obj.getClass().getCanonicalName(), obj.getClass().getAnnotation(JSONObject.class));
			Java2JSONBinder js2Binder = buildBinder(obj.getClass());
			if (js2Binder == null) {
				return -2;
			}
			m_Binders.put(obj.getClass().getCanonicalName(), js2Binder);
		}
		try {
			jsWriter.startObject();
			m_Binders.get(obj.getClass().getCanonicalName()).processJSON(jsWriter, obj);
			jsWriter.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	private Java2JSONBinder buildBinder(Class<?> currentClass) {
		Java2JSONBinder j2jsonBinder = new Java2JSONBinder();
		JSONObject jo = m_JSONObject.get(currentClass.getCanonicalName());
		for (Field fldCurrent : currentClass.getDeclaredFields()) {
			if (fldCurrent.isAnnotationPresent(JSONEntity.class)) {
				JSONEntity je = fldCurrent.getAnnotation(JSONEntity.class);
				Definition def = DefinitionFactory.getDefinition(fldCurrent, je, jo);
				if (def != null) {
					j2jsonBinder.addDefinition(def);
				}
			}

		}
		return j2jsonBinder;
	}

	public boolean hasBinderDefinition(Class<?> cl) {
		if (!m_JSONObject.containsKey(cl.getCanonicalName())) {
			if (!cl.isAnnotationPresent(JSONObject.class)) {
				return false;
			}
		}
		return true;

	}
}
