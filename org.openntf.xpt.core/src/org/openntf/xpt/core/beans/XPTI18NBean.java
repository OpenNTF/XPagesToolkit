package org.openntf.xpt.core.beans;

import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.openntf.xpt.core.i18n.I18NServiceProvider;
import org.openntf.xpt.core.i18n.II18NService;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class XPTI18NBean {

	public static final String BEAN_NAME = "xptI18NBean"; //$NON-NLS-1$

	public static XPTI18NBean get(FacesContext context) {
		XPTI18NBean bean = (XPTI18NBean) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTI18NBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	public String getCurrentLanguage() {
		String strLanguage = null;
		String strEffectiveUserName = null;
		try {

			String strLocLang = ExtLibUtil.getXspContext().getLocaleString();
			// System.out.println(strLocLang);
			// System.out.println("UND: " +
			// FacesContextEx.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage());
			// String strLocLang =
			// FacesContextEx.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage();
			for (String strLngTest : getAllLanguages()) {
				if (strLocLang.toLowerCase().startsWith(strLngTest.toLowerCase())) {
					strLanguage = setLanguageForUser(strEffectiveUserName, strLngTest);
					break;
				}
			}
			if (strLanguage == null) {
				strLanguage = setLanguageForUser(strEffectiveUserName, getDefaultLanguage());
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

	public List<String> getAllLanguages() {
		return I18NServiceProvider.getInstance().getAllLanguages();
	}

	public String getDefaultLanguage() {
		// return
		// FacesContext.getCurrentInstance().getApplication().getDefaultLocale().getLanguage();
		return I18NServiceProvider.getInstance().getDefaultLanguage();
	}

	public String getValue(String strKey) {
		return getValue(strKey, getCurrentLanguage());
	}

	public String getValue(String strKey, String strLanguage) {
		return I18NServiceProvider.getInstance().getValue(strKey, strLanguage);

	}

	public String getJSRepresentation(String varName, String languageForce) {
		II18NService service = I18NServiceProvider.getInstance().getI18NProvider();
		if (service == null) {
			return "";
		}
		Set<String> keys = service.getKeys();
		if (keys == null) {
			return null;
		}
		String strLanguage = StringUtil.isEmpty(languageForce) ? getDefaultLanguage() : languageForce;
		StringWriter strWriter = new StringWriter();
		try {
			JsonWriter jsWriter = new JsonWriter(strWriter, true);
			jsWriter.startObject();
			for (String strKey : keys) {
				jsWriter.startProperty(strKey);
				jsWriter.outStringLiteral(service.getValue(strKey, strLanguage));
				jsWriter.endProperty();
			}
			jsWriter.endObject();
			jsWriter.close();
		} catch (Exception ex) {
		}
		StringBuilder sb = new StringBuilder();
		sb.append("var " + varName + " = ");
		sb.append(strWriter.toString());
		return sb.toString();
	}

}
