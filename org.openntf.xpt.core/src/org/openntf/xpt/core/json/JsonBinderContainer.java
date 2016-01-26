package org.openntf.xpt.core.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;
import org.openntf.xpt.core.json.binding.BinderProcessParameter;
import org.openntf.xpt.core.json.binding.Java2JSONBinder;
import org.openntf.xpt.core.utils.ServiceSupport;

import com.ibm.domino.services.util.JsonWriter;

public class JsonBinderContainer {
	private final HashMap<String, Java2JSONBinder> m_Binders = new HashMap<String, Java2JSONBinder>();
	private final HashMap<String, JSONObject> m_JSONObject = new HashMap<String, JSONObject>();

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
			BinderProcessParameter parameter = BinderProcessParameter.buildParameter(this,jsWriter, obj);
			m_Binders.get(obj.getClass().getCanonicalName()).processJSON(parameter);
			jsWriter.endObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	private Java2JSONBinder buildBinder(Class<?> currentClass) {
		Java2JSONBinder j2jsonBinder = new Java2JSONBinder();
		JSONObject jo = m_JSONObject.get(currentClass.getCanonicalName());
		Collection<Field> lstFields = ServiceSupport.getClassFields(currentClass);
		for (Field fldCurrent : lstFields) {
			if (fldCurrent.isAnnotationPresent(JSONEntity.class)) {
				JSONEntity je = fldCurrent.getAnnotation(JSONEntity.class);
				Definition def = DefinitionFactory.getDefinition(this, fldCurrent, je, jo);
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
