package org.openntf.xpt.core.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRestMethod {

	public String getMethodName();

	public void execute(HttpServletRequest request, HttpServletResponse response) throws XPTRestException;
}
