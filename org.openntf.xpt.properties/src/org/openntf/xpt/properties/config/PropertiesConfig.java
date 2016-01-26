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
package org.openntf.xpt.properties.config;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.openntf.xpt.core.config.IPartConfiguration;

public class PropertiesConfig implements IPartConfiguration {

	public PropertiesConfig() {
	}

	@Override
	public String[] getXspConfigFiles() {
		return new String[] {"org/openntf/xpt/properties/config/xpt-properties-datasource.xsp-config"};
	}

	@Override
	public String[] getFacesConfigFiles() {
		return new String[] {"org/openntf/xpt/properties/config/xpt-properties-faces-config.xml"};
	}

	@Override
	public CommandProvider getCommandProvider() {
		return null;
	}

}
