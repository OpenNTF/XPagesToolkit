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
package org.openntf.xpt.core.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.openntf.xpt.core.Activator;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.AbstractDataSource;

public class XPTLibUtils {

	public static String getXptLibVersion() {
		try {
			String s = AccessController.doPrivileged(new PrivilegedAction<String>() {
				public String run() {
					Object o = Activator.getInstance().getBundle().getHeaders().get("Bundle-Version"); // $NON-NLS-1$
					if (o != null) {
						return o.toString();
					}
					return null;
				}
			});
			if (s != null) {
				return s;
			}
		} catch (SecurityException ex) {
		}
		return "";
	}

	// ==============================================================================
	// Style utility
	// ==============================================================================

	public static String concatStyleClasses(String s1, String s2) {
		if (StringUtil.isNotEmpty(s1)) {
			if (StringUtil.isNotEmpty(s2)) {
				return s1 + " " + s2;
			}
			return s1;
		} else {
			if (StringUtil.isNotEmpty(s2)) {
				return s2;
			}
			return "";
		}
	}

	public static String concatStyles(String s1, String s2) {
		if (StringUtil.isNotEmpty(s1)) {
			if (StringUtil.isNotEmpty(s2)) {
				return s1 + ";" + s2;
			}
			return s1;
		} else {
			if (StringUtil.isNotEmpty(s2)) {
				return s2;
			}
			return "";
		}
	}

	public static AbstractDataSource getDatasource(String compName, String dsName, boolean deep) {
		UIComponent uiTop = FacesContext.getCurrentInstance().getViewRoot();
		@SuppressWarnings("deprecation")
		UIComponent uiFound = ExtLibUtil.getComponentFor(uiTop, compName);
		if (uiFound == null) {
			return null;
		}
		return getDatasourceFromUIComp(uiFound, dsName, deep);
	}

	private static AbstractDataSource getDatasourceFromUIComp(UIComponent uic, String dsName, boolean deep) {
		Object obj = uic.getAttributes().get("data");
		if (obj != null && obj instanceof AbstractDataSource) {
			return (AbstractDataSource) obj;
		}
		if (obj != null && obj instanceof List<?>) {
			List<?> lstDS = (List<?>) obj;
			for (Object obj2 : lstDS) {
				if (obj2 instanceof AbstractDataSource) {
					AbstractDataSource as = (AbstractDataSource) obj2;
					if (as.getVar().equals(dsName)) {
						return as;
					}
				}
			}
		}
		if (!deep) {
			return null;
		}
		if (uic.getChildCount() > 0) {
			for (Iterator<?> itChild = uic.getChildren().iterator(); itChild.hasNext();) {
				Object objChild = itChild.next();
				if (objChild instanceof UIComponent) {
					UIComponent uicChild = (UIComponent) objChild;
					AbstractDataSource adsRC = getDatasourceFromUIComp(uicChild, dsName, deep);
					if (adsRC != null) {
						return adsRC;
					}
				}
			}

		}
		return null;
	}

}
