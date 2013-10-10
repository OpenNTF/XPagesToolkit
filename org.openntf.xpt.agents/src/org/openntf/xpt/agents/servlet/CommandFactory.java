package org.openntf.xpt.agents.servlet;

import java.util.HashMap;

import org.openntf.xpt.agents.servlet.commands.CheckAgents;
import org.openntf.xpt.agents.servlet.commands.CheckLogin;

public class CommandFactory {

	private static CommandFactory m_Factory;

	private CommandFactory() {

	}

	public static CommandFactory getInstance() {
		if (m_Factory == null) {
			m_Factory = new CommandFactory();
		}
		return m_Factory;
	}

	private HashMap<String, IXPTServletCommand> m_Commands;

	public IXPTServletCommand getCommand(String strCommand) {
		if (m_Commands == null) {
			initCommands();
		}
		return m_Commands.get(strCommand);
	}

	private void initCommands() {
		m_Commands = new HashMap<String, IXPTServletCommand>();
		m_Commands.put("check", new CheckAgents());
		m_Commands.put("checkLogin", new CheckLogin());

	}
}
