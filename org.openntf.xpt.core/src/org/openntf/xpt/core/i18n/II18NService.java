package org.openntf.xpt.core.i18n;

import java.util.List;

public interface II18NService {
	public List<String> getAllLanguages();
	public String getValue(String strKey, String strLanguage);
	public String getDefaultLanguage();
}
