/*
 * © Copyright WebGate Consulting AG, 2014
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
package org.openntf.xpt.core.i18n;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openntf.xpt.core.beans.XPTI18NBean;

import com.ibm.xsp.context.FacesContextEx;

public abstract class AbstractI18NPropertiesService implements II18NService {
	public abstract List<String> getPropertyFileNames();

	private HashMap<String, HashMap<String,String>> m_LangContainer = new HashMap<String, HashMap<String,String>>();

	private static final String PROPERTIES = ".properties";
	
	public String getValue(String strKey, String strLanguage) {

		HashMap<String, String> propValues = new HashMap<String, String>();
	
		if (!m_LangContainer.containsKey(strLanguage)) {
			
				 for(String strFile : getPropertyFileNames()){
					 String strPropFileName =  "";
					 if (strLanguage.equals(XPTI18NBean.get().getDefaultLanguage())) {
						 strPropFileName = strFile +  PROPERTIES;
						} else {
							strPropFileName = strFile +  "_" + strLanguage + PROPERTIES;
						}
						Properties prop = null;
					 	prop = getPropertiesFromFile(strPropFileName);
						if (prop != null) {
							for(String key : prop.stringPropertyNames()){
								propValues.put(strFile + "." + key, prop.getProperty(key));
							}
						}
				 
				 
				m_LangContainer.put(strLanguage, propValues);
			}
			
			
		}

		propValues = m_LangContainer.get(strLanguage);
		if (propValues == null) {
			///Shouldnt be!
		}
		return propValues.get(strKey);
	}

	@Override
	public Set<String> getKeys() {
		return m_LangContainer == null? null:m_LangContainer.keySet();
	}
	private Properties getPropertiesFromFile(String fileName) {
		Properties prop = new Properties();

		try {

			InputStream is = FacesContextEx.getCurrentInstance().getExternalContext().getResourceAsStream(fileName);
			BufferedReader r = new BufferedReader(
		                new InputStreamReader(is, "UTF-8"));
			prop.load(r);
			return prop;

		} catch (Exception e) {
			// Heisst, dass dieses PropFile nicht existiert.
			return null;
		}
	}
	

}
