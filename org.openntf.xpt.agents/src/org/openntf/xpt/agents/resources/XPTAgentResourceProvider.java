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
package org.openntf.xpt.agents.resources;

import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.openntf.xpt.agents.XPTAgentActivator;

import com.ibm.xsp.resource.DojoModuleResource;
import com.ibm.xsp.resource.StyleSheetResource;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;

public class XPTAgentResourceProvider extends BundleResourceProvider {

	private static final String CSS_PROGRESSBAR = "/.ibmxspres/.xptagents/css/progressbar.css";
	private static final String CSS_AGENTLIST = "/.ibmxspres/.xptagents/css/agentlist.css";
	public static final String RESOURCES_WEB_XPT = "/resources/web/xpt/xptagents/";
	public static final String XPT_PREFIX = ".xptagents";

	public static final String DOJO_XPT_PROGRESSBAR_AGENTCONTROLlER = "xptagents.progressbar.agentcontroller";
	public static final String DOJO_XPT_AGENTLISTCONROLLER = "xptagents.login.controller";

	public XPTAgentResourceProvider() {
		super(XPTAgentActivator.getInstance().getBundle(), XPT_PREFIX);
	}

	@Override
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

	public static final DojoModuleResource XPTAGENTS_PROGRESSBAR_AGENTCONTROLLER = new DojoModuleResource(DOJO_XPT_PROGRESSBAR_AGENTCONTROLlER);
	public static final StyleSheetResource XPTAGENTS_PROGRESSBAR_CSS = new StyleSheetResource(CSS_PROGRESSBAR);
	public static final DojoModuleResource XPTAGENTS_PROGRESSBAR_DOJO = new DojoModuleResource("dijit.ProgressBar");
	public static final DojoModuleResource XPTAGENTS_EXLIB = new DojoModuleResource("extlib.dijit.ExtLib");
	public static final StyleSheetResource XPTAGENTS_AGENTLIST_CSS = new StyleSheetResource(CSS_AGENTLIST);
	public static final DojoModuleResource XPTAGENTS_AGENTLIST_CONTROLLER = new DojoModuleResource(DOJO_XPT_AGENTLISTCONROLLER);


}
