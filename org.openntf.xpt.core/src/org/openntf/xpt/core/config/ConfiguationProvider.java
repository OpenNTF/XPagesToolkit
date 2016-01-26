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

package org.openntf.xpt.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandProvider;

public class ConfiguationProvider {

	public static final String EXT_POINT_ID = "org.openntf.xpt.part";
	private static ConfiguationProvider m_Provider;


	private List<String> m_XspConfig;
	private List<String> m_FacesConfig;
	private List<CommandProvider> m_CommandProvider;

	private ConfiguationProvider() {

	}
	public static synchronized ConfiguationProvider getInstance() {
		if (m_Provider == null) {
			m_Provider = new ConfiguationProvider();
		}
		return m_Provider;
	}

	public synchronized String[] getXspConfigFiles(
			IPartConfiguration[] arrConfig) {
		if (m_XspConfig == null) {
			initCP(arrConfig);
		}
		return m_XspConfig != null ? m_XspConfig.toArray(new String[m_XspConfig
				.size()]) : new String[] {};
	}

	private void initCP(IPartConfiguration[] arrConfig) {
		m_XspConfig = new ArrayList<String>();
		m_FacesConfig = new ArrayList<String>();
		m_CommandProvider = new ArrayList<CommandProvider>();
		m_FacesConfig
				.add("org/openntf/xpt/core/config/xpt-core-faces-config.xml");
		m_XspConfig.add("org/openntf/xpt/core/config/xpt-i18n.xsp-config");
		for (IPartConfiguration lib : arrConfig) {
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

	public synchronized String[] getFacesConfigFiles(
			IPartConfiguration[] arrConfig) {
		if (m_FacesConfig == null) {
			initCP(arrConfig);
		}
		return m_FacesConfig != null ? m_FacesConfig
				.toArray(new String[m_FacesConfig.size()]) : new String[] {};

	}

	public synchronized CommandProvider[] getCommandProvider(
			IPartConfiguration[] arrConfig) {
		if (m_CommandProvider == null) {
			initCP(arrConfig);
		}
		return m_CommandProvider != null ? m_CommandProvider
				.toArray(new CommandProvider[m_CommandProvider.size()])
				: new CommandProvider[] {};

	}

}
