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
