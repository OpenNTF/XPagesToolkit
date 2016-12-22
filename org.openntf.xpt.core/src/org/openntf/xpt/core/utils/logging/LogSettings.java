/**
 * Copyright 2014, WebGate Consulting AG
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
package org.openntf.xpt.core.utils.logging;

import java.io.Serializable;
import java.util.logging.Level;

public class LogSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int m_LogLevel = 1;
	private int m_LogLevelConsole = 4;
	private int m_LogLevelFile = 1;

	public static String getLogLevelTXT(int nLevel) {
		switch (nLevel) {
		case 0:
			return "OFF";
		case 1:
			return "SEVERE";
		case 2:
			return "WARNING";
		case 3:
			return "INFO";
		case 4:
			return "FINE";
		case 5:
			return "FINER";
		case 6:
			return "FINEST";

		}
		return "ALL";
	}

	public static Level getLogLevel(int nLevel) {
		switch (nLevel) {
		case 0:
			return Level.OFF;
		case 1:
			return Level.SEVERE;
		case 2:
			return Level.WARNING;
		case 3:
			return Level.INFO;
		case 4:
			return Level.FINE;
		case 5:
			return Level.FINER;
		case 6:
			return Level.FINEST;

		}
		return Level.ALL;

	}

	public void setLogLevel(int logLevel) {
		m_LogLevel = logLevel;
	}

	public int getLogLevel() {
		return m_LogLevel;
	}
	public void setLogMainLevelTXT(String strLogLevel) {
		m_LogLevel = Integer.parseInt(strLogLevel);
	}

	public String getLogMainLevelTXT() {
		return ""+m_LogLevel;
	}

	public void setLogLevelConsole(int logLevelConsole) {
		m_LogLevelConsole = logLevelConsole;
	}

	public int getLogLevelConsole() {
		return m_LogLevelConsole;
	}
	public void setLogLevelConsoleTXT(String logLevelConsole) {
		m_LogLevelConsole = Integer.parseInt(logLevelConsole);
	}

	public String getLogLevelConsoleTXT() {
		return ""+m_LogLevelConsole;
	}

	public void setLogLevelFile(int logLevelFile) {
		m_LogLevelFile = logLevelFile;
	}

	public int getLogLevelFile() {
		return m_LogLevelFile;
	}
	public void setLogLevelFileTXT(String logLevelFile) {
		m_LogLevelFile = Integer.parseInt(logLevelFile);
	}

	public String getLogLevelFileTXT() {
		return ""+m_LogLevelFile;
	}

}
