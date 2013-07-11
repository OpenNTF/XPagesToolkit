package org.openntf.xpt.core.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.openntf.xpt.core.Activator;

import com.ibm.commons.util.StringUtil;

public class XPTLibUtils {

	public static String getXptLibVersion() {
		try {
			String s = AccessController.doPrivileged(new PrivilegedAction<String>() {
				public String run() {
					Object o = Activator.instance.getBundle().getHeaders().get("Bundle-Version"); // $NON-NLS-1$
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
        if(StringUtil.isNotEmpty(s1)) {
            if(StringUtil.isNotEmpty(s2)) {
                return s1 + " " + s2;
            }
            return s1;
        } else {
            if(StringUtil.isNotEmpty(s2)) {
                return s2;
            }
            return "";
        }
    }

    public static String concatStyles(String s1, String s2) {
        if(StringUtil.isNotEmpty(s1)) {
            if(StringUtil.isNotEmpty(s2)) {
                return s1 + ";" + s2;
            }
            return s1;
        } else {
            if(StringUtil.isNotEmpty(s2)) {
                return s2;
            }
            return "";
        }
    }


}
