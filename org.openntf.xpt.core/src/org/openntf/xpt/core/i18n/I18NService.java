package org.openntf.xpt.core.i18n;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.openntf.xpt.core.beans.XPTI18NBean;

import com.ibm.designer.runtime.Application;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.context.FacesContextEx;



public class I18NService {
	private static final String I18N_SERVICE_KEY = "xpt.dss.encryption"; // $NON-NLS-1$

	private static final String I18N_DATAPROVIDER_SERVICE = "org.openntf.xpt.core.dss.Encryption"; // $NON-NLS-1$

//	private static final String PREF_PROVIDER = "xpt.dss.encryption.provider";
	
	private List<AbstractI18NPropertiesService> m_I18NServices;
	
//	private I18NServiceProvider m_I18NService;

	private static final String PROPERTIES = ".properties";
	
	private HashMap<String, HashMap<String,String>> m_LangContainer = new HashMap<String, HashMap<String,String>>();
	
	public static I18NService getInstance() {
		I18NService cls = (I18NService) Application.get().getObject(I18N_SERVICE_KEY);
		if (cls == null) {
			synchronized (I18NService.class) {
				cls = (I18NService) Application.get().getObject(I18N_SERVICE_KEY);
				if (cls == null) {
					cls = new I18NService();
					Application.get().putObject(I18N_SERVICE_KEY, cls);

				}

			}
		}
		return cls;
	}
	
	
	public List<AbstractI18NPropertiesService> getI18NProviders() {
		if (m_I18NServices == null) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {

				@SuppressWarnings("unchecked")
				@Override
				public Void run() {
					m_I18NServices = ApplicationEx.getInstance().findServices(I18N_DATAPROVIDER_SERVICE);

					return null;
				}
			});
		}
		return m_I18NServices;
	}
	
	/*
	public I18NServiceProvider getI18NProvider() {
		if (m_I18NService == null) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {
					String providersProp = ApplicationEx.getInstance()
							.getApplicationProperty(PREF_PROVIDER, null);
					List<I18NServiceProvider> allI18NProviders = getI18NProviders();
					
					for (I18NServiceProvider p:  allI18NProviders) {
						if (p.getName().equalsIgnoreCase(providersProp)) {
							m_I18NService = p;
							break;
						}
					}
					if (m_I18NService == null) {
						if(allI18NProviders.size() > 0){
							m_I18NService = allI18NProviders.get(0);
						}else{
							//"No Provider found. Assign UNID Provider!");
							//m_I18NService = new UNIDKeyProvider();
						}
					}

					return null;
				}
			});
		}
		return m_I18NService;
	}
	*/
	
	public String getValue(String strKey, String strLanguage) {

		HashMap<String, String> propValues = new HashMap<String, String>();
	
		if (!m_LangContainer.containsKey(strLanguage)) {
			
			for (AbstractI18NPropertiesService i18nProv : getI18NProviders()) {
				 for(String strFile : i18nProv.getPropertyFileNames()){
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

/*
	private List<String> getPropertyFileNames(){
		return null;
	}
*/

}
