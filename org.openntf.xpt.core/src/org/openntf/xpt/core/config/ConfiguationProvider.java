/*
 * © Copyright WebGate Consulting AG, 2013
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
package org.openntf.xpt.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class ConfiguationProvider {

	public static final String EXT_POINT_ID = "org.openntf.xpt.part";
	private static ConfiguationProvider m_Provider;

	private ConfiguationProvider() {

	}

	private List<String> m_XspConfig;
	private List<String> m_FacesConfig;
	private List<CommandProvider> m_CommandProvider;

	public static synchronized ConfiguationProvider getInstance() {
		if (m_Provider == null) {
			m_Provider = new ConfiguationProvider();
		}
		return m_Provider;
	}

	public synchronized String[] getXspConfigFiles() {
		if (m_XspConfig == null) {
			initCP();
		}
		return m_XspConfig != null ? m_XspConfig.toArray(new String[m_XspConfig.size()]) : new String[] {};
	}

	private void initCP() {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		m_XspConfig = new ArrayList<String>();
		m_FacesConfig = new ArrayList<String>();
		m_CommandProvider = new ArrayList<CommandProvider>();
		m_FacesConfig.add("org/openntf/xpt/core/config/xpt-core-faces-config.xml");
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXT_POINT_ID);
		try {
			for (IConfigurationElement e : config) {
				String strElementID = e.getAttribute("ID");
				logCurrent.info("Adding Part: " + strElementID);
				Object objRC = e.createExecutableExtension("class");
				if (objRC instanceof IPartConfiguration) {
					IPartConfiguration lib = (IPartConfiguration) objRC;
					if (lib.getCommandProvider() != null) {
						m_CommandProvider.add(lib.getCommandProvider());
					}
					if (lib.getFacesConfigFiles() != null) {
						m_FacesConfig.addAll(Arrays.asList(lib.getFacesConfigFiles()));
					}
					if (lib.getXspConfigFiles() != null) {
						m_XspConfig.addAll(Arrays.asList(lib.getXspConfigFiles()));
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public synchronized String[] getFacesConfigFiles() {
		if (m_FacesConfig == null) {
			initCP();
		}
		return m_FacesConfig != null ? m_FacesConfig.toArray(new String[m_FacesConfig.size()]) : new String[] {};

	}

	public synchronized CommandProvider[] getCommandProvider() {
		if (m_CommandProvider == null) {
			initCP();
		}
		return m_CommandProvider != null ? m_CommandProvider.toArray(new CommandProvider[m_CommandProvider.size()]) : new CommandProvider[] {};

	}

}
