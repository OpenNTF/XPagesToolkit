package org.openntf.xpt.agents.servlet.commands;

import java.io.PrintWriter;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.beans.XPTAgentBean;
import org.openntf.xpt.agents.servlet.IXPTServletCommand;

public class CheckAgents implements IXPTServletCommand {

	@Override
	public void processActionH(HttpServletResponse resp, XPTAgentBean agentBean, FacesContext context) {
		try {
			PrintWriter pwCurrent = resp.getWriter();
			pwCurrent.append("Check scheduling: " + agentBean.checkSchedule() + " started.");
			pwCurrent.append("OK");
			pwCurrent.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
