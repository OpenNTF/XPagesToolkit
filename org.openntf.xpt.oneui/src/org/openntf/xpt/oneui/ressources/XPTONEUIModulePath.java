package org.openntf.xpt.oneui.ressources;

import com.ibm.xsp.resource.DojoModulePathResource;

public class XPTONEUIModulePath extends DojoModulePathResource {

	public static final String XPT_ONEUI_MODUL_PATH = "/.ibmxspres/" + XPTONEUIResourceProvider.XPT_PREFIX;

	public XPTONEUIModulePath() {
		super("xptoneui", XPT_ONEUI_MODUL_PATH);
	}

}
