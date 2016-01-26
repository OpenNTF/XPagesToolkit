/**
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
package org.openntf.xpt.properties.datasource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openntf.xpt.core.properties.storage.StorageService;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.model.AbstractDataContainer;

public class PropertiesDataContainer extends AbstractDataContainer {

	private PropertiesDataObject m_PDO;
	private String m_Database;
	private String m_FileName;

	public PropertiesDataContainer() {
		super();
	}

	public PropertiesDataContainer(String arg0, String arg1) {
		super(arg0, arg1);
	}

	public PropertiesDataContainer(String beanID, String unid, String database,
			String fileName) {
		super(beanID, unid);
		m_Database = database;
		m_FileName = fileName;
	}

	@Override
	public void deserialize(ObjectInput in) throws IOException {
		m_Database = readUTF(in);
		m_FileName = readUTF(in);
		try {
			m_PDO = (PropertiesDataObject) in.readObject();
		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "Errror during deserialize", e);
		}
	}

	@Override
	public void serialize(ObjectOutput out) throws IOException {
		writeUTF(out, m_Database);
		writeUTF(out, m_FileName);
		out.writeObject(m_PDO);

	}

	public String getDatabase() {
		return m_Database;
	}

	public void setDatabase(String database) {
		m_Database = database;
	}

	public String getFileName() {
		return m_FileName;
	}

	public void setFileName(String fileName) {
		m_FileName = fileName;
	}

	public PropertiesDataObject getPDO() {
		if (m_PDO == null) {
			executeLoad();
		}
		return m_PDO;
	}

	public boolean executeSave() {
		if (m_PDO == null) {
			return false;
		}
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			m_PDO.getProperties().store(bos, "CGU-SAVE");
			if (StorageService.getInstance().saveProperties(getDatabase(),
					getFileName(), bos.toByteArray()) < 1) {
				return false;
			}

		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "save failed!", e);
			return false;
		}
		return true;
	}

	public void executeLoad() {
		InputStream isCurrent = StorageService.getInstance().getFile(
				m_Database, m_FileName);
		m_PDO = new PropertiesDataObject();
		m_PDO.initProperties(isCurrent);
	}
}
