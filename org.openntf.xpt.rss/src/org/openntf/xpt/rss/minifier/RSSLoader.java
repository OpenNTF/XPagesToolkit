/**
 * Copyright 2013, WebGate Consulting AG
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
