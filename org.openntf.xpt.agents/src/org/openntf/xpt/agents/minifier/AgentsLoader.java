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
