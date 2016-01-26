package org.openntf.xpt.core.json.binding;

import org.openntf.xpt.core.json.Definition;
import org.openntf.xpt.core.json.JSONEmptyValueStrategy;
import org.openntf.xpt.core.json.JsonBinderContainer;

import com.ibm.domino.services.util.JsonWriter;

public class BinderProcessParameter {

	private final JsonBinderContainer container;
	private final JsonWriter writer;
	private final Object object;
	private final String jsonProperty;
	private final String javaField;
	private final JSONEmptyValueStrategy strategy;
	private final Class<?> containerClass;

	public static BinderProcessParameter buildParameter(JsonBinderContainer container, JsonWriter writer, Object obj ) {
		return new BinderProcessParameter(container,writer, obj, null,null,null,null);
	}

	private BinderProcessParameter(JsonBinderContainer container, JsonWriter writer, Object objCurrent, String strJSONProperty, String strJAVAField, JSONEmptyValueStrategy strategy, Class<?> containerClass) {
		super();
		this.container = container;
		this.writer = writer;
		this.object = objCurrent;
		this.jsonProperty = strJSONProperty;
		this.javaField = strJAVAField;
		this.strategy = strategy;
		this.containerClass = containerClass;
	}

	public JsonWriter getWriter() {
		return writer;
	}

	public Object getObject() {
		return object;
	}

	public String getJsonProperty() {
		return jsonProperty;
	}

	public String getJavaField() {
		return javaField;
	}

	public JSONEmptyValueStrategy getStrategy() {
		return strategy;
	}

	public Class<?> getContainerClass() {
		return containerClass;
	}

	public BinderProcessParameter applyDefinition(Definition def) {
		return new BinderProcessParameter(container,writer, object, def.getJSONProperty(), def.getJAVAField(), def.getEmptyValueStrategy(), def.getContainerClass());
	}

	public JsonBinderContainer getJsonBinderContainer() {
		return container;
	}
}
