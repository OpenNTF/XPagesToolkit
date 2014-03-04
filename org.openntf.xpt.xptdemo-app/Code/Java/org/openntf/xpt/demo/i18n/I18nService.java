package org.openntf.xpt.demo.i18n;

import java.util.ArrayList;
import java.util.List;

import org.openntf.xpt.core.i18n.AbstractI18NPropertiesService;

public class I18nService extends AbstractI18NPropertiesService{

	@Override
	public List<String> getPropertyFileNames() {
		List<String> propertyFileNames = new ArrayList<String>();
		propertyFileNames.add("contact");
		propertyFileNames.add("keywords");
		propertyFileNames.add("general");
		return propertyFileNames;
	}

	public List<String> getAllLanguages() {
		List<String> languages = new ArrayList<String>();
		languages.add("en");
		languages.add("de");
		return languages;
	}

	public String getDefaultLanguage() {
		return "en";
	}

}
