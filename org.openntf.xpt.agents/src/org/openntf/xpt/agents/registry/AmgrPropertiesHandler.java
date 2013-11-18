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
package org.openntf.xpt.agents.registry;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class AmgrPropertiesHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Properties m_Props = new Properties();

	public Properties getProps() {
		return m_Props;
	}

	public void setProps(Properties props) {
		m_Props = props;
	}

	public AmgrPropertiesHandler() {
		loadProperties();
	}

	private void loadProperties() {
		try {
			Document docProfile = getProfileDocument();
			m_Props = new Properties();
			if (docProfile.getItems() != null) {
				for (Iterator<?> itItems = docProfile.getItems().iterator(); itItems.hasNext();) {
					Item itmCurrent = (Item) itItems.next();
					if (!itmCurrent.getName().startsWith("$")) {
						m_Props.setProperty(itmCurrent.getName(), itmCurrent.getValueString());
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document getProfileDocument() {
		Document docProfile = null;
		try {
			Database ndbCurrent = NotesContext.getCurrentUnchecked().getCurrentDatabase();
			docProfile = ndbCurrent.getProfileDocument("XPTAmgrProps", ndbCurrent.getServer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docProfile;
	}

	public void saveProperties() {
		try {
			Document docProfile = getProfileDocument();
			for (Enumeration<?> enKeys = m_Props.propertyNames(); enKeys.hasMoreElements();) {
				String strName = (String) enKeys.nextElement();
				docProfile.replaceItemValue(strName, m_Props.getProperty(strName));
			}
			docProfile.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String strKey) {
		return m_Props.getProperty(strKey);
	}

	public void setProperty(String strKey, String strValue) {
		m_Props.setProperty(strKey, strValue);
	}
}
