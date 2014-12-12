package org.openntf.xpt.rss.minifier;

import org.openntf.xpt.core.minifier.AbstractResourceLoader;
import org.openntf.xpt.rss.XPTRSSActivator;
import org.openntf.xpt.rss.resources.XPTRSSModulePath;
import org.openntf.xpt.rss.resources.XPTRSSResourceProvider;

import com.ibm.commons.util.DoubleMap;

public class RSSLoader extends AbstractResourceLoader {

	public RSSLoader() {
		super(XPTRSSActivator.getInstance(), XPTRSSResourceProvider.RESOURCES_WEB_XPT, "xptrss.", XPTRSSModulePath.XPT_RSS_MODUL_PATH);
	}

	@Override
	public void loadDojoShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		if (alias != null) {
			alias.put("@XTRa", "xptrss.list.feedcontroller");
		}
		if (prefix != null) {
			prefix.put("XTR", "xptrss");
			prefix.put("2XTRa", "xptrss.list");
		}
	}

	@Override
	public void loadCSSShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		if (alias != null) {
			alias.put("@XTRa", "/.ibmxspres/.xptrss/css/rss.css");
		}
		if (prefix != null) {
			prefix.put("XTR", "/.ibmxspres/.xptrss/");
			prefix.put("2XTRa", "/.ibmxspres/.xptrss/css/");
		}
	}
}
