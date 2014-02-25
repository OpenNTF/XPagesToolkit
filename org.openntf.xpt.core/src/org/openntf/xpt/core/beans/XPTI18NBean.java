package org.openntf.xpt.core.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.openntf.xpt.core.i18n.I18NService;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class XPTI18NBean {

	public static final String BEAN_NAME = "xptI18NBean"; //$NON-NLS-1$

	
	public static XPTI18NBean get(FacesContext context) {
		XPTI18NBean bean = (XPTI18NBean) context.getApplication().getVariableResolver()
				.resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTI18NBean get() {
		return get(FacesContext.getCurrentInstance());
	}
	

	private String getCurrentLanguage(){
		String strLanguage = null;
		String strEffectiveUserName = null;
		try {
				strLanguage =ExtLibUtil.getXspContext().getLocaleString();
				strLanguage = FacesContextEx.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage();
				for (String strLngTest : getAllLanguages()) {
					if (strLanguage.toLowerCase().startsWith(strLngTest.toLowerCase())) {
						strLanguage = setLanguageForUser(strEffectiveUserName, strLngTest);
						break;
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strLanguage;
	}
	
	private String setLanguageForUser(String strEffectiveUserName, String strLanguage) {
		ExtLibUtil.getXspContext().setLocaleString(strLanguage);
		return strLanguage;
	}
	
	public List<String> getAllLanguages(){
		List<String> languages = new ArrayList<String>();
		FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
		for(Locale lang : Locale.getAvailableLocales()){
			languages.add(lang.getLanguage());
		}
		return languages;
	}
	
	public String getDefaultLanguage(){
		return FacesContext.getCurrentInstance().getApplication().getDefaultLocale().getLanguage();
	}
	
	public String getValue(String strKey) {
		return getValue(strKey, getCurrentLanguage());
	}

	public String getValue(String strKey, String strLanguage) {
		return I18NService.getInstance().getValue(strKey, strLanguage);
	}
	
}
