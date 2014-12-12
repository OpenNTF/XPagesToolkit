package org.openntf.xpt.core.minifier;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Plugin;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.ResourceLoader.UrlDojoLocaleResource;

public class XPTDojoLocalResource extends UrlDojoLocaleResource {
	private final Plugin m_Plugin;

	public XPTDojoLocalResource(DojoLibrary dojoLibrary, String name, String baseUrl, Plugin plugin) {
		super(dojoLibrary, name, baseUrl);
		m_Plugin = plugin;
	}

	@Override
	protected URL getResourceURL(String baseUrl, String name) throws IOException {
		String path = baseUrl + StringUtil.replace(name, '.', '/') + ".js"; // $NON-NLS-1$
		URL url = ExtLibUtil.getResourceURL(m_Plugin.getBundle(), path);
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
