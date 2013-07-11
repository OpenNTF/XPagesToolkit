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
package org.openntf.xpt.core.properties.storage;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesNote;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.designer.domino.napi.design.FileAccess;

public class StorageService {

	private static StorageService m_Service;

	private StorageService() {

	}

	public static StorageService getInstance() {
		if (m_Service == null) {
			m_Service = new StorageService();
		}
		return m_Service;
	}

	public InputStream getFile(String strDatabase, String strFileName) {
		try {
			NotesSession nSession = new NotesSession();
			NotesDatabase nDatabase = nSession.getDatabaseByPath(strDatabase);
			nDatabase.open();
			NotesNote nFile = FileAccess.getFileByPath(nDatabase, strFileName);
			InputStream is = FileAccess.readFileContentAsInputStream(nFile);
			nFile.recycle();
			nDatabase.recycle();
			nSession.recycle();
			return is;
		} catch (NotesAPIException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Properties getPropertiesFromFile(String strDatabase, String strFileName) {
		try {
			InputStream is = getFile(strDatabase, strFileName);
			if (is == null) {
				return null;
			}
			Properties propRC = new Properties();
			propRC.load(is);
			return propRC;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public int saveProperties(String strDatabase, String strFileName, byte[] arrBytes) {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		try {
			NotesSession nSession = new NotesSession();
			NotesDatabase nDatabase = nSession.getDatabaseByPath(strDatabase);
			nDatabase.open();
			NotesNote nFile = FileAccess.getFileByPath(nDatabase, strFileName);
			if (nFile == null) {
				logCurrent.warning("Creating new Propertiesfile for: " + strFileName);
				nFile = nDatabase.createNote();
				nFile.initAsFile("345CgQ");
				nFile.setItemText("$TITLE", strFileName);
			}
			FileAccess.saveData(nFile, strFileName, arrBytes);
			nFile.recycle();
			nDatabase.recycle();
			nSession.recycle();

		} catch (Exception e) {
			e.printStackTrace();
			return -9;
		}
		return 1;
	}

	public boolean hasPropertiesFile(String strDatabase, String strFileName) {
		boolean blRC = false;
		try {
			NotesSession nSession = new NotesSession();
			NotesDatabase nDatabase = nSession.getDatabaseByPath(strDatabase);
			nDatabase.open();
			NotesNote nFile = FileAccess.getFileByPath(nDatabase, strFileName);
			if (nFile != null) {
				blRC = true;
				nFile.recycle();
			}
			nDatabase.recycle();
			nSession.recycle();

		} catch (Exception e) {
			e.printStackTrace();
			return blRC;
		}
		return blRC;
	}

}
