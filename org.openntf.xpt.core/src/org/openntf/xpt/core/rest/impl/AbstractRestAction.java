package org.openntf.xpt.core.rest.impl;

import org.openntf.xpt.core.rest.IRestAction;

public abstract class AbstractRestAction extends BaseJson2ObjectRestBinding implements IRestAction {

	private final String actionName;
	private final String[] supportedMethods;
	
	public AbstractRestAction(String actionName, String[] supportedMethods) {
		this.actionName = actionName;
		this.supportedMethods = supportedMethods;
	}

	@Override
	public String getActionName() {
		return actionName;
	}

	@Override
	public String[] getSupportedMethods() {
		return supportedMethods;
	}

}
