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
package org.openntf.xpt.core.utils;

import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Session;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * Handles all calls to open and close databases and prevents to recycle the
 * session.getCurrentDatabase() by accident
 * 
 * @author Christian Guedemann
 * 
 */
public enum DatabaseProvider {
	INSTANCE;

	/**
	 * Returns the requested database. If databaseName is empty the current
	 * database is used
	 * 
	 * @param databaseName
	 * @param asSigner
	 * @return database or null
	 */
	public Database getDatabase(String databaseName, boolean asSigner) {
		Session sesCurrent = null;
		if (asSigner) {
			sesCurrent = ExtLibUtil.getCurrentSessionAsSigner();
		}
		if (sesCurrent == null) {
			sesCurrent = ExtLibUtil.getCurrentSession();
		}

		Database ndbAccess = null;
		try {
			if (StringUtil.isEmpty(databaseName)) {
				ndbAccess = sesCurrent.getCurrentDatabase();
				if (ndbAccess == null) {
					ndbAccess = ExtLibUtil.getCurrentDatabase();
				}
			} else {
				if (databaseName.contains("!!")) {
					String[] arrDB = databaseName.split("!!");
					ndbAccess = sesCurrent.getDatabase(arrDB[0], arrDB[1]);
				} else {
					ndbAccess = sesCurrent.getDatabase(sesCurrent.getCurrentDatabase().getServer(), databaseName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new XPTRuntimeException("An unexpcted error was occured during getDatabase()", e);
		}
		return ndbAccess;

	}

	/**
	 * Call to recylce the database (and prevent to recycle the currentDatabase
	 * 
	 * @param ndbRecylce
	 *            Database which should be recycled
	 */
	public void handleRecylce(Database ndbRecylce) {
		try {
			Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
			// Database accessed with sesSigner.getCurrentDB() should not be
			// recycled.
			if (ExtLibUtil.getCurrentSessionAsSigner() != null && ndbRecylce.equals(ExtLibUtil.getCurrentSessionAsSigner().getCurrentDatabase())) {
				logCurrent.info("No recycle -> sesSigner.currentDB");
				return;
			}
			// Dabases accsesd with ses.getCurrnentDB() should not be reccled.
			if (ndbRecylce.equals(ExtLibUtil.getCurrentDatabase())) {
				logCurrent.info("No recycle -> ses.currentDB");
				return;
			}

			logCurrent.info("Recycle -> DB is not currentDB");
			ndbRecylce.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
