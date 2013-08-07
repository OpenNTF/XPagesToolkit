package org.openntf.xpt.core.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class ValueBindingSupport {

	public static String getValue(String strValue, String strValueName, UIComponent uic, String strDefault, FacesContext context) {
		if (strValue != null) {
			return strValue;
		}
		ValueBinding vb = uic.getValueBinding(strValueName);
		if (vb != null) {
			return (String) vb.getValue(context);
		}
		return strDefault;
	}

	public static String getValue(String strValue, String strValueName, ValueBindingObjectImpl uic, String strDefault, FacesContext context) {
		if (strValue != null) {
			return strValue;
		}
		ValueBinding vb = uic.getValueBinding(strValueName);
		if (vb != null) {
			return (String) vb.getValue(context);
		}
		return strDefault;
	}
}
