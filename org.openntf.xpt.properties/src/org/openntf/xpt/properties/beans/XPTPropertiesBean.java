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
package org.openntf.xpt.properties.beans;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

import javax.faces.context.FacesContext;

import org.openntf.xpt.core.properties.storage.StorageService;

public class XPTPropertiesBean {

	public static final String BEAN_NAME = "xptPropertiesBean"; //$NON-NLS-1$

	public static XPTPropertiesBean get(FacesContext context) {
		XPTPropertiesBean bean = (XPTPropertiesBean) context.getApplication()
				.getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTPropertiesBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	public Properties getProperties(String strDatabase, String strFileName) {
		final String finDB = strDatabase;
		final String finFile = strFileName;

		return AccessController
				.doPrivileged(new PrivilegedAction<Properties>() {

					@Override
					public Properties run() {
						return StorageService.getInstance()
								.getPropertiesFromFile(finDB, finFile);
					}
				});
	}

}
