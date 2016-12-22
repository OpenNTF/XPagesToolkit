/*
 * Copyright 2013, WebGate Consulting AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
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
