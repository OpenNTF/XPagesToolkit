package org.openntf.xpt.core.i18n;


import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
					 if (strLanguage.equals(XPTI18NBean.get().getDefaultLanguage())) {
							strFile += PROPERTIES;
						} else {
							strFile += "_" + strLanguage + PROPERTIES;
						}
						Properties prop = null;
					 	prop = getPropertiesFromFile(strFile);
						if (prop != null) {
							for(String key : prop.stringPropertyNames()){
								propValues.put(key, prop.getProperty(key));
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
	
	private Properties getPropertiesFromFile(String fileName) {
		Properties prop = new Properties();

		try {
			prop.load(FacesContextEx.getCurrentInstance().getExternalContext().getResourceAsStream(fileName));
			return prop;

		} catch (Exception e) {
			// Heisst, dass dieses PropFile nicht existiert.
			return null;
		}
	}

}
