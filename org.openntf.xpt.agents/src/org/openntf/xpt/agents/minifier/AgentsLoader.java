package org.openntf.xpt.agents.minifier;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openntf.xpt.agents.XPTAgentActivator;
import org.openntf.xpt.agents.resources.XPTAgentResourceProvider;
import org.openntf.xpt.agents.resources.XPTModulePath;

import com.ibm.commons.util.DoubleMap;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.CSSResource;
import com.ibm.xsp.minifier.DojoResource;
import com.ibm.xsp.minifier.ResourceLoader;

public class AgentsLoader extends ResourceLoader {

	public static class AgentsDojoLocaleResource extends UrlDojoLocaleResource {
		public AgentsDojoLocaleResource(DojoLibrary dojoLibrary, String name, String baseUrl) {
			super(dojoLibrary, name, baseUrl);
		}

		@Override
		protected URL getResourceURL(String baseUrl, String name) throws IOException {
			String path = baseUrl + StringUtil.replace(name, '.', '/') + ".js"; // $NON-NLS-1$
			URL url = ExtLibUtil.getResourceURL(XPTAgentActivator.getInstance().getBundle(), path);
			return url;
		}

		// TEMP XPages bug
		@Override
		protected String getModulePath(String locale) {
			String s = super.getModulePath(locale);
			s = StringUtil.replace(s, "..", ".");
			return s;
		}
	}

	// HELPER CLASSES
	public static class AgentsCSSResource extends UrlCSSResource {
		public AgentsCSSResource(DojoLibrary dojoLibrary, String name, URL url) {
			super(dojoLibrary, name, url);
		}

		@Override
		protected String calculateUrlPrefix() {
			String s = super.calculateUrlPrefix();
			// If we try to access a resource through a servlet, add the
			// prefix...
			if (s.startsWith("/.ibmxspres/")) { // $NON-NLS-1$
				s = "/xsp" + s; // $NON-NLS-1$
			}
			return s;
		}
	}

	
	public AgentsLoader() {
		// TODO Auto-generated constructor stub
	}


	// Resources
	private HashMap<String, CSSResource> cssResources = new HashMap<String, CSSResource>();

	@Override
	public CSSResource getCSSResource(String name, DojoLibrary dojoLibrary) throws IOException {
		CSSResource r = cssResources.get(name);
		if (r == null) {
			synchronized (this) {
				r = cssResources.get(name);
				if (r == null) {
					r = loadCSSResource(name, dojoLibrary);
					if (r != null) {
						cssResources.put(name, r);
					}
				}
			}
		}
		return r;
	}

	public CSSResource loadCSSResource(String name, DojoLibrary dojoLibrary) {
		if (name.startsWith(XPTModulePath.XPT_AGENTS_MODUL_PATH)) { // $NON-NLS-1$
			String path = XPTAgentResourceProvider.RESOURCES_WEB_XPT + name.substring(XPTModulePath.XPT_AGENTS_MODUL_PATH.length()); // $NON-NLS-1$
			URL u = ExtLibUtil.getResourceURL(XPTAgentActivator.getInstance().getBundle(), path);
			if (u != null) {
				return new AgentsCSSResource(dojoLibrary, name, u);
			}
		}
		return null;
	}

	@Override
	public DojoResource getDojoResource(String name, DojoLibrary dojoLibrary) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, DojoResource> dojoResources = (Map<String, DojoResource>) dojoLibrary.getDojoResources();

		DojoResource r = dojoResources.get(name);
		if (r == null) {
			synchronized (this) {
				r = dojoResources.get(name);
				if (r == null) {
					r = loadDojoResource(name, dojoLibrary);
					if (r != null) {
						dojoResources.put(name, r);
					}
				}
			}
		}
		return r;

	}

	protected DojoResource loadDojoResource(String name, DojoLibrary dojoLibrary) {
		if (name.startsWith("xptagents.")) { // $NON-NLS-1$
			String dojoName = name.substring(10);
			String path = XPTAgentResourceProvider.RESOURCES_WEB_XPT + StringUtil.replace(dojoName, '.', '/') + ".js"; // $NON-NLS-1$
			URL u = ExtLibUtil.getResourceURL(XPTAgentActivator.getInstance().getBundle(), path);
			if (u != null) {
				return new UrlDojoResource(dojoLibrary, name, u);
			}
		}
		if (name.startsWith("!xptagents.")) { // $NON-NLS-1$
			String dojoName = name.substring(11);
			return new AgentsDojoLocaleResource(dojoLibrary, dojoName, XPTAgentResourceProvider.RESOURCES_WEB_XPT);
		}
		return null;
	}

	@Override
	public void loadDojoShortcuts(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		super.loadDojoShortcuts(alias, prefix);
		if (alias != null) {
			alias.put("@XTAa", "xptagents.progressbar.agentcontroller");
			alias.put("@XTAb", "xptagents.login.controller");
		}
		if (prefix != null) {
			prefix.put("XTA", "xptagents");
			prefix.put("2XTAa", "xptagents.progressbar");
			prefix.put("2XTAb", "xptagents.login");
		}
	}
	
	@Override
	public void loadCSSShortcuts(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		super.loadCSSShortcuts(alias, prefix);
		if (alias != null) {
			alias.put("@XTAa","/.ibmxspres/.xptagents/css/progressbar.css");
			alias.put("@XTAb","/.ibmxspres/.xptagents/css/progressbar.css");
			
		}
		if (prefix != null) {
			prefix.put("XTA", "/.ibmxspres/.xptagents/");
			prefix.put("XTAa", "/.ibmxspres/.xptagents/css/");
		}
	}
}
