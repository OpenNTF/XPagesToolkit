/**
 * Copyright 2014, WebGate Consulting AG
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
