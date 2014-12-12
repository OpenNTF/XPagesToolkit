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

/**
 * The xptI18NBean delivers a set of methods to handle all language related stuff
 * @author Lena Troxler, Christian Guedemann
 *
 */
public final class XPTI18NBean {

	public static final String BEAN_NAME = "xptI18NBean"; //$NON-NLS-1$

	public static XPTI18NBean get(FacesContext context) {
		XPTI18NBean bean = (XPTI18NBean) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTI18NBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	/**
	 * Evaluates the current language
	 * @return the current language as ISO Code
	 */
	public String getCurrentLanguage() {
		String language = null;
		String effectiveUserName = null;
		try {

			String contextLanguage = ExtLibUtil.getXspContext().getLocaleString();
			for (String languageTest : getAllLanguages()) {
				if (contextLanguage.toLowerCase().startsWith(languageTest.toLowerCase())) {
					language = setLanguageForUser(effectiveUserName, languageTest);
					break;
				}
			}
			if (language == null) {
				language = setLanguageForUser(effectiveUserName, getDefaultLanguage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return language;
	}

	
	/**
	 * Set the langauge for a User
	 * @param effectiveUserName
	 * @param language
	 * @return
	 */
	private String setLanguageForUser(String effectiveUserName, String language) {
		ExtLibUtil.getXspContext().setLocaleString(language);
		return language;
	}

	/**
	 * All languages of the current application
	 * @return list of iso codes
	 */
	public List<String> getAllLanguages() {
		return I18NServiceProvider.getInstance().getAllLanguages();
	}

	/**
	 * The default language of this application
	 * @return iso code of the default language
	 */
	public String getDefaultLanguage() {
		return I18NServiceProvider.getInstance().getDefaultLanguage();
	}

	/**
	 * Language value for key using the current language
	 * @param key
	 * @return 
	 */
	public String getValue(String key) {
		return getValue(key, getCurrentLanguage());
	}

	/**
	 * Language value for key using the specified language
	 * @param key
	 * @param language
	 * @return
	 */
	public String getValue(String key, String language) {
		return I18NServiceProvider.getInstance().getValue(key, language);

	}

	/**
	 * List of keys
	 * @return List of keys
	 */
	public List<String> getKeys() {
		return I18NServiceProvider.getInstance().getKeys();
	}

	/**
	 * Builds the JS representation of the language values as a JSON object.
	 * @param varName Name of the js variable
	 * @param languageForce Language to force
	 * @return a string with the JSON object
	 */
	public String getJSRepresentation(String varName, String languageForce) {
		II18NService service = I18NServiceProvider.getInstance().getI18NProvider();
		if (service == null) {
			return "var " + varName + " = {}";
		}
		String strLanguage = StringUtil.isEmpty(languageForce) ? getCurrentLanguage() : languageForce;
		Set<String> keys = service.getKeys(strLanguage);
		if (keys == null) {
			return "var " + varName + " = {}";
		}
		StringWriter strWriter = new StringWriter();
		try {
			JsonWriter jsWriter = new JsonWriter(strWriter, true);
			jsWriter.startObject();
			for (String strKey : keys) {
				String strValue = service.getValue(strKey, strLanguage);
				jsWriter.startProperty(strKey.replace(".", "_"));
				jsWriter.outStringLiteral(strValue != null ? strValue : "");
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
