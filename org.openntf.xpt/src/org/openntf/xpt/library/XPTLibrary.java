/*
 * � Copyright WebGate Consulting AG, 2013
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
package org.openntf.xpt.library;

import org.openntf.xpt.agents.config.AgentConfig;
import org.openntf.xpt.core.config.ConfiguationProvider;
import org.openntf.xpt.core.config.IPartConfiguration;
import org.openntf.xpt.objectlist.config.ObjectListConfig;
import org.openntf.xpt.oneui.config.OneUIConfig;
import org.openntf.xpt.properties.config.PropertiesConfig;
import org.openntf.xpt.rss.config.RSSConfig;

import com.ibm.xsp.library.AbstractXspLibrary;

public class XPTLibrary extends AbstractXspLibrary {

	private static final IPartConfiguration[] ALL_CONFIG = { new AgentConfig(),
			new ObjectListConfig(), new OneUIConfig(), new PropertiesConfig(),
			new RSSConfig() };

	public XPTLibrary() {
	}

	@Override
	public String getLibraryId() {
		return "org.openntf.xpt.library";
	}

	@Override
	public String[] getXspConfigFiles() {
		return ConfiguationProvider.getInstance().getXspConfigFiles(ALL_CONFIG);
	}

	@Override
	public String getPluginId() {
		return "org.openntf.xpt";
	}

	public String[] getFacesConfigFiles() {
		return ConfiguationProvider.getInstance().getFacesConfigFiles(
				ALL_CONFIG);
	}

	public String[] getDependencies() {
		return new String[] { "com.ibm.xsp.core.library", // $NON-NLS-1$
				"com.ibm.xsp.extsn.library", // $NON-NLS-1$
				"com.ibm.xsp.domino.library", // $NON-NLS-1$
				"com.ibm.xsp.designer.library", // $NON-NLS-1$
				"com.ibm.xsp.extlib.library", // $NON-NLS-1$
		};
	}

	public IPartConfiguration[] getConfigs() {
		return ALL_CONFIG;
	}
}
