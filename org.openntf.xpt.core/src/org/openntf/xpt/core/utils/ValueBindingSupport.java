package org.openntf.xpt.core.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class ValueBindingSupport {

	private ValueBindingSupport() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(T value, String propertyName, ValueBindingObjectImpl uic, T defaultValue, FacesContext context) {
		if (value != null) {
			return value;
		}
		ValueBinding vb = uic.getValueBinding(propertyName);
		if (vb != null) {
			return (T) vb.getValue(context);
		}
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(T value, String propertyName, UIComponent uic, T defaultValue, FacesContext context) {
		if (value != null) {
			return value;
		}
		ValueBinding vb = uic.getValueBinding(propertyName);
		if (vb != null) {
			return (T) vb.getValue(context);
		}
		return defaultValue;
	}

}
