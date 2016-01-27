package org.openntf.xpt.core.rest.impl;

import org.openntf.xpt.core.rest.IRestMethod;

public abstract class AbstractRestMethod extends BaseJson2ObjectRestBinding implements IRestMethod {

	private final String methodName;

	public AbstractRestMethod(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String getMethodName() {
		return this.methodName;
	}

}
