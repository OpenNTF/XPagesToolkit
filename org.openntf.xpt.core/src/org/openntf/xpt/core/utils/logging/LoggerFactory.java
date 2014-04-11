/*
 * © Copyright WebGate Consulting AG, 2013
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.xpt.core.properties.storage.StorageService;

import com.ibm.designer.runtime.Application;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class LoggerFactory {
	private static final String XPT_CORE_LOGGER = "-core-";

	private static final String XPT_LOG_PROPERTIES = "xpt-log.properties";

	private static final String APPLICATION_LOGPROP_KEY = "xpt.logger.properties";

	private static HashMap<String, Logger> m_RegistredLoggers = new HashMap<String, Logger>();

	private static int m_logLevel = -1;

	public static synchronized Logger getLogger(String strName) {
		try {
			String strDB = XPT_CORE_LOGGER;
			try {
				if (Application.get() != null && NotesContext.getCurrent() != null) {
					strDB = NotesContext.getCurrent().getCurrentDatabase().getFilePath();
				}
			} catch (Exception e) {
				// System.out.println("Context has no Database... :"+e.getMessage());
			}
			Logger logRC = null;
			if (m_RegistredLoggers.containsKey(strDB + strName)) {
				logRC = m_RegistredLoggers.get(strDB + strName);
				if (!strDB.equals(XPT_CORE_LOGGER)) {
					return logRC;
				}
			} else {
				logRC = java.util.logging.Logger.getAnonymousLogger();
			}
			
			if (strDB.equals(XPT_CORE_LOGGER)) {
				if (m_logLevel == -1) {
					checkLogLevel();
				}
				logRC.setLevel(getLogLevel(m_logLevel));
				ConsoleHandler ch = new ConsoleHandler(strDB, strName, getLogLevel(m_logLevel));
				logRC.addHandler(ch);
			} else {
				int nLevel = getAppLogLevel(strDB, strName);
				logRC.setLevel(getLogLevel(nLevel));
				ConsoleHandler ch = new ConsoleHandler(strDB, strName, getLogLevel(nLevel));
				logRC.addHandler(ch);

			}
			m_RegistredLoggers.put(strDB + strName, logRC);
			return logRC;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	private static void checkLogLevel() {
		try {
			NotesContext nc = NotesContext.getCurrentUnchecked();
			if (nc == null) {
				return;
			}
			String strLogLevel = nc.getEnvironmentString("DEBUG_XPT");
			if (strLogLevel == null || "".equals(strLogLevel)) {
				m_logLevel = 1;
			}
			try {
				if (strLogLevel != null && !"".equals(strLogLevel)) {
					m_logLevel = Integer.parseInt(strLogLevel);
				}
			} catch (Exception e) {
				m_logLevel = 0;
			}
			System.out.println("XPT     LOG-LEVEL is set to: " + strLogLevel + " / " + m_logLevel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int getAppLogLevel(String strDBPath, String strClassName) {
		System.out.println("check: " + strDBPath +" / "+ strClassName);
		if (Application.get() == null) {
			return -1;
		}
		int nRC = 1;
		try {
			Properties propLog = null;
			if (Application.get().getObject(APPLICATION_LOGPROP_KEY) == null) {

				if (!StorageService.getInstance().hasPropertiesFile(strDBPath, XPT_LOG_PROPERTIES)) {
					propLog = new Properties();
					propLog.setProperty("level", "1");
				} else {
					propLog = StorageService.getInstance().getPropertiesFromFile(strDBPath, XPT_LOG_PROPERTIES);
				}
				Application.get().putObject(APPLICATION_LOGPROP_KEY, propLog);
			} else {
				propLog = (Properties) Application.get().getObject(APPLICATION_LOGPROP_KEY);
			}
			List<String> arrList = new ArrayList<String>();
			for (Enumeration<?> en = propLog.propertyNames(); en.hasMoreElements();) {
				arrList.add((String) en.nextElement());
			}
			Collections.sort(arrList);
			Collections.reverse(arrList);
			String strLevel = "";
			String strKeyCaller = "";
			for (String strKey : arrList) {
				if (strClassName.startsWith(strKey)) {
					strLevel = propLog.getProperty(strKey);
					strKeyCaller = strKey;
					break;
				}
			}
			if ("".equals(strLevel)) {
				strLevel = propLog.getProperty("level", "1");
			}
			try {
				nRC = Integer.parseInt(strLevel);
			} catch (Exception e) {
				System.out.println("XPT.LoggerFactory.getLogLevel with " + strDBPath + " / " + strClassName + " /" + strKeyCaller + ": strLevel =" + strLevel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nRC;
	}

	public static void logFinest(Class<?> clCurrent, String strMessage, Throwable thr) {
		log(Level.FINEST, clCurrent, strMessage, thr);
	}

	public static void logFiner(Class<?> clCurrent, String strMessage, Throwable thr) {
		log(Level.FINER, clCurrent, strMessage, thr);
	}

	public static void logFine(Class<?> clCurrent, String strMessage, Throwable thr) {
		log(Level.FINE, clCurrent, strMessage, thr);
	}

	public static void logInfo(Class<?> clCurrent, String strMessage, Throwable thr) {
		log(Level.INFO, clCurrent, strMessage, thr);
	}

	public static void logWarning(Class<?> clCurrent, String strMessage, Throwable thr) {
		log(Level.WARNING, clCurrent, strMessage, thr);
	}

	public static void logError(Class<?> clCurrent, String strMessage, Throwable thr) {
		log(Level.SEVERE, clCurrent, strMessage, thr);

	}

	public static void log(Level level, Class<?> clCurrent, String strMessage, Throwable thr) {
		Logger log = getLogger(clCurrent.getCanonicalName());
		if (thr == null) {
			log.log(level, strMessage);
		} else {
			log.log(level, strMessage, thr);
		}
	}

}
