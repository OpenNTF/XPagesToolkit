package org.openntf.xpt.oneui.config;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.openntf.xpt.core.config.IPartConfiguration;

public class OneUIConfig implements IPartConfiguration {

	public OneUIConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getXspConfigFiles() {
		return new String[] {"org/openntf/xpt/oneui/config/xpt-oneui.xsp-config"};
	}

	@Override
	public String[] getFacesConfigFiles() {
		return new String[] {"org/openntf/xpt/oneui/config/xpt-oneui-faces-config.xml"};
	}

	@Override
	public CommandProvider getCommandProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
