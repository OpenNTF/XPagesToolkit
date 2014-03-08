package org.openntf.xpt.oneui.minifier;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openntf.xpt.oneui.XPTOneUIActivator;
import org.openntf.xpt.oneui.ressources.XPTONEUIModulePath;
import org.openntf.xpt.oneui.ressources.XPTONEUIResourceProvider;

import com.ibm.commons.util.DoubleMap;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.DojoLibrary;
//import com.ibm.xsp.extlib.plugin.DominoPluginActivator;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.CSSResource;
import com.ibm.xsp.minifier.DojoResource;
import com.ibm.xsp.minifier.ResourceLoader;

public class ONEUILoader extends ResourceLoader {

	public static class ONUEIDojoLocaleResource extends UrlDojoLocaleResource {
		public ONUEIDojoLocaleResource(DojoLibrary dojoLibrary, String name, String baseUrl) {
			super(dojoLibrary, name, baseUrl);
		}

		@Override
		protected URL getResourceURL(String baseUrl, String name) throws IOException {
			String path = baseUrl + StringUtil.replace(name, '.', '/') + ".js"; // $NON-NLS-1$
			URL url = ExtLibUtil.getResourceURL(XPTOneUIActivator.getInstance().getBundle(), path);
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
	public static class ONEUICSSResource extends UrlCSSResource {
		public ONEUICSSResource(DojoLibrary dojoLibrary, String name, URL url) {
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

	public ONEUILoader() {
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
		if (name.startsWith(XPTONEUIModulePath.XPT_ONEUI_MODUL_PATH)) { // $NON-NLS-1$
			String path = XPTONEUIResourceProvider.RESOURCES_WEB_XPT + name.substring(XPTONEUIModulePath.XPT_ONEUI_MODUL_PATH.length()); // $NON-NLS-1$
			URL u = ExtLibUtil.getResourceURL(XPTOneUIActivator.getInstance().getBundle(), path);
			if (u != null) {
				return new ONEUICSSResource(dojoLibrary, name, u);
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
		if (name.startsWith("xptoneui.")) { // $NON-NLS-1$
			String dojoName = name.substring(9);
			String path = XPTONEUIResourceProvider.RESOURCES_WEB_XPT + StringUtil.replace(dojoName, '.', '/') + ".js"; // $NON-NLS-1$
			URL u = ExtLibUtil.getResourceURL(XPTOneUIActivator.getInstance().getBundle(), path);
			if (u != null) {
				return new UrlDojoResource(dojoLibrary, name, u);
			}
		}
		if (name.startsWith("!xptoneui.")) { // $NON-NLS-1$
			String dojoName = name.substring(10);
			return new ONUEIDojoLocaleResource(dojoLibrary, dojoName, XPTONEUIResourceProvider.RESOURCES_WEB_XPT);
		}
		return null;
	}

	@Override
	public void loadDojoShortcuts(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		super.loadDojoShortcuts(alias, prefix);
		if (alias != null) {
			alias.put("@XTOa", "xptoneui.typeahead.ReadStore");
			alias.put("@XTOb", "xptoneui.typeahead.widget");
			alias.put("@XTOc", "xptoneui.typeahead.pre17.ReadStore");
			alias.put("@XTOd", "xptoneui.typeahead.pre17.widget");
		}
		if (prefix != null) {
			prefix.put("XTO", "xptoneui");
			prefix.put("2XTOa", "xptoneui.typeahead");
			prefix.put("2XTOb", "xptoneui.typeahead.pre17");
		}
	}
}
