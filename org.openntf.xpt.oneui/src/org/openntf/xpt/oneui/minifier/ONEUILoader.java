package org.openntf.xpt.oneui.minifier;

import org.openntf.xpt.core.minifier.AbstractResourceLoader;
import org.openntf.xpt.oneui.XPTOneUIActivator;
import org.openntf.xpt.oneui.ressources.XPTONEUIModulePath;
import org.openntf.xpt.oneui.ressources.XPTONEUIResourceProvider;

import com.ibm.commons.util.DoubleMap;
//import com.ibm.xsp.extlib.plugin.DominoPluginActivator;

public class ONEUILoader extends AbstractResourceLoader {

	public ONEUILoader() {
		super(XPTOneUIActivator.getInstance(), XPTONEUIResourceProvider.RESOURCES_WEB_XPT, "xptoneui.", XPTONEUIModulePath.XPT_ONEUI_MODUL_PATH);
	}

	@Override
	public void loadDojoShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
		if (alias != null) {
			alias.put("@XTOa", "xptoneui.typeahead.ReadStore");
			alias.put("@XTOb", "xptoneui.typeahead.widget");
			alias.put("@XTOc", "xptoneui.typeahead.pre17.ReadStore");
			alias.put("@XTOd", "xptoneui.typeahead.pre17.widget");
		}
		if (prefix != null) {
			prefix.put("XTO", "xptoneui");
			prefix.put("2XTOa", "xptoneui.typeahead");
			prefix.put("2XTOb", "xptoneui.typeahead.pre17");
		}
	}

	@Override
	public void loadCSSShortcutsXPT(DoubleMap<String, String> alias, DoubleMap<String, String> prefix) {
	}
}
