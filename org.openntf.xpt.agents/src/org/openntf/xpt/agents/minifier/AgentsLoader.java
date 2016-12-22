/*
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
package org.openntf.xpt.agents.minifier;

import org.openntf.xpt.agents.XPTAgentActivator;
import org.openntf.xpt.agents.resources.XPTAgentResourceProvider;
import org.openntf.xpt.agents.resources.XPTModulePath;
import org.openntf.xpt.core.minifier.AbstractResourceLoader;

import com.ibm.commons.util.DoubleMap;

public class AgentsLoader extends AbstractResourceLoader {

	public AgentsLoader() {
		super(XPTAgentActivator.getInstance(), XPTAgentResourceProvider.RESOURCES_WEB_XPT, "xptagents.", XPTModulePath.XPT_AGENTS_MODUL_PATH);
	}

	@Override
	public void loadDojoShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
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
	public void loadCSSShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		if (alias != null) {
			alias.put("@XTAa", "/.ibmxspres/.xptagents/css/progressbar.css");
			alias.put("@XTAb", "/.ibmxspres/.xptagents/css/progressbar.css");

		}
		if (prefix != null) {
			prefix.put("XTA", "/.ibmxspres/.xptagents/");
			prefix.put("2XTAa", "/.ibmxspres/.xptagents/css/");
		}
	}
}
