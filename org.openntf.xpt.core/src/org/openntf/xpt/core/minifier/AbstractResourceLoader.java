package org.openntf.xpt.core.minifier;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;

import com.ibm.commons.util.DoubleMap;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.CSSResource;
import com.ibm.xsp.minifier.DojoResource;
import com.ibm.xsp.minifier.ResourceLoader;

public abstract class AbstractResourceLoader extends ResourceLoader {

	private final Plugin m_Plugin;
	private final String m_ResourcePath;
	private final String m_Prefix;
	private final String m_ModulPath;

	@Override
	public void loadCSSShortcuts(DoubleMap<String, String> arg0, DoubleMap<String, String> arg1) {
		super.loadCSSShortcuts(arg0, arg1);
		loadCSSShortcutsXPT(arg0, arg1);
	}

	@Override
	public void loadDojoShortcuts(DoubleMap<String, String> arg0, DoubleMap<String, String> arg1) {
		super.loadDojoShortcuts(arg0, arg1);
		loadDojoShortcutsXPT(arg0, arg1);
	}

	public abstract void loadCSSShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix);

	public abstract void loadDojoShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix);

	private HashMap<String, CSSResource> cssResources = new HashMap<String, CSSResource>();

	public AbstractResourceLoader(Plugin plugin, String resourcePath, String prefix, String modulPath) {
		m_Plugin = plugin;
		m_ResourcePath = resourcePath;
		m_Prefix = prefix;
		m_ModulPath = modulPath;
	}

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
		if (name.startsWith(m_ModulPath)) { // $NON-NLS-1$
			String path = m_ResourcePath + name.substring(m_ModulPath.length() + 1); // $NON-NLS-1$
			URL u = ExtLibUtil.getResourceURL(m_Plugin.getBundle(), path);
			if (u != null) {
				return new XPTCSSResource(dojoLibrary, name, u);
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
		if (name.startsWith(m_Prefix)) { // $NON-NLS-1$
			String dojoName = name.substring(m_Prefix.length());
			String path = m_ResourcePath + StringUtil.replace(dojoName, '.', '/') + ".js"; // $NON-NLS-1$
			URL u = ExtLibUtil.getResourceURL(m_Plugin.getBundle(), path);
			if (u != null) {
				return new UrlDojoResource(dojoLibrary, name, u);
			}
		}
		if (name.startsWith("!" + m_Prefix)) { // $NON-NLS-1$
			String dojoName = name.substring(m_Prefix.length() + 1);
			return new XPTDojoLocalResource(dojoLibrary, dojoName, m_ResourcePath, m_Plugin);
		}
		return null;
	}

}