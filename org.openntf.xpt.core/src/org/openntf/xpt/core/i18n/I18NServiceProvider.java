package org.openntf.xpt.core.i18n;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import com.ibm.designer.runtime.Application;
import com.ibm.xsp.application.ApplicationEx;



public class I18NServiceProvider  {
	private static final String I18N_SERVICE_KEY = "xpt.core.i18n"; // $NON-NLS-1$

	private static final String I18N_DATAPROVIDER_SERVICE = "org.openntf.xpt.core.I18n"; // $NON-NLS-1$

//	private static final String PREF_PROVIDER = "xpt.dss.i18n.provider";
	
	private List<II18NService> m_I18NServices;
	
	private II18NService m_I18NService;

	
	

	public static I18NServiceProvider getInstance() {
		I18NServiceProvider cls = (I18NServiceProvider) Application.get().getObject(I18N_SERVICE_KEY);
		if (cls == null) {
			synchronized (I18NServiceProvider.class) {
				cls = (I18NServiceProvider) Application.get().getObject(I18N_SERVICE_KEY);
				if (cls == null) {
					cls = new I18NServiceProvider();
					Application.get().putObject(I18N_SERVICE_KEY, cls);

				}

			}
		}
		return cls;
	}
	
	
	public List<II18NService> getI18NProviders() {
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
	
	
	public II18NService getI18NProvider() {
		if (m_I18NService == null) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {
					List<II18NService> allI18NProviders = getI18NProviders();
					/*String providersProp = ApplicationEx.getInstance()
							.getApplicationProperty(PREF_PROVIDER, null);
				
					
					for (II18NService p:  allI18NProviders) {
						if (p.getName().equalsIgnoreCase(providersProp)) {
							m_I18NService = p;
							break;
						}
					}*/
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
	
	
	
	public String getValue(String strKey, String strLanguage) {

		II18NService i18nProv = getI18NProvider();
		if(i18nProv.getValue(strKey, strLanguage) != null)
			return i18nProv.getValue(strKey, strLanguage);	 
		return null;
	}


	public String getDefaultLanguage() {
		II18NService i18nProv = getI18NProvider();
		return i18nProv.getDefaultLanguage();
	}
	
	public List<String> getAllLanguages() {
		II18NService i18nProv = getI18NProvider();
		return i18nProv.getAllLanguages();
	}
/*
	private List<String> getPropertyFileNames(){
		return null;
	}
*/

}
