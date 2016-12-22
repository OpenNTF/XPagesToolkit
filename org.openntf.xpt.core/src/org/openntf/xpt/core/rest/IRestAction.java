package org.openntf.xpt.core.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRestAction {

	public String getActionName();

	public String[] getSupportedMethods();

	public void execute(HttpServletRequest request, HttpServletResponse response) throws XPTRestException;
}
