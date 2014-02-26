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
package org.openntf.xpt.oneui.ressources;

import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.openntf.xpt.oneui.XPTOneUIActivator;

import com.ibm.xsp.resource.DojoModuleResource;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;

public class XPTONEUIResourceProvider extends BundleResourceProvider {

	public XPTONEUIResourceProvider() {
		super(XPTOneUIActivator.getInstance().getBundle(), XPT_PREFIX);
	}

	protected URL getResourceURL(HttpServletRequest arg0, String name) {
		String path = RESOURCES_WEB_XPT + name; // $NON-NLS-1$
		int fileNameIndex = path.lastIndexOf('/');
		String fileName = path.substring(fileNameIndex + 1);
		path = path.substring(0, fileNameIndex + 1);
		// see http://www.osgi.org/javadoc/r4v42/org/osgi/framework/Bundle.html
		// #findEntries%28java.lang.String,%20java.lang.String,%20boolean%29
		Enumeration<?> urls = getBundle().findEntries(path, fileName, false/* recursive */);
		if (null != urls && urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			if (null != url) {
				return url;
			}
		}
		return null; // no match, 404 not found.

	}

	public static final DojoModuleResource XPTONEUI_NAMEPICKER_TYPEAHED_DATASTORE = new DojoModuleResource("xptoneui.typeahead.ReadStore");
	public static final DojoModuleResource XPTONEUI_NAMEPICKER_TYPEAHED_WIDGET = new DojoModuleResource("xptoneui.typeahead.widget");
	public static final DojoModuleResource XPTONEUI_NAMEPICKER_TYPEAHED_DATASTORE_161 = new DojoModuleResource("xptoneui.typeahead.pre17.ReadStore");
	public static final DojoModuleResource XPTONEUI_NAMEPICKER_TYPEAHED_WIDGET_161 = new DojoModuleResource("xptoneui.typeahead.pre17.widget");
	private static final String RESOURCES_WEB_XPT = "/resources/web/xpt/oneui/";
	public static final String XPT_PREFIX = ".xptoneui";
}
