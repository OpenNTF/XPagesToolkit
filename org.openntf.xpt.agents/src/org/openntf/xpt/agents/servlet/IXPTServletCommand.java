package org.openntf.xpt.agents.servlet;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.beans.XPTAgentBean;

public interface IXPTServletCommand {

	public void processActionH( HttpServletResponse resp, XPTAgentBean agentBean, FacesContext context);
}
