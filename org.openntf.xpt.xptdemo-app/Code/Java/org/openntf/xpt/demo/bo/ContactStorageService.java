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
package org.openntf.xpt.demo.bo;

import java.util.ArrayList;
import java.util.List;

import org.openntf.xpt.core.dss.DominoStorageService;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;

public class ContactStorageService {

	private static ContactStorageService m_Service;

	private ContactStorageService() {

	}

	public static ContactStorageService getInstance() {
		if (m_Service == null) {
			m_Service = new ContactStorageService();
		}
		return m_Service;
	}

	public List<Contact> getAllContacts(Session sesCurrent) {
		List<Contact> lstRC = new ArrayList<Contact>();
		try {
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			View viwLUP = ndbCurrent.getView("AllContacts");
			Document docNext = viwLUP.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = viwLUP.getNextDocument(docNext);
				Contact conCurrnet = new Contact();
				DominoStorageService.getInstance().getObjectWithDocument(conCurrnet, docProcess);
				lstRC.add(conCurrnet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;
	}

	public Contact getContactByID(String strID, Session sesCurrent) {
		Contact conRC = new Contact();
		try {
			DominoStorageService.getInstance().getObject(conRC, strID, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conRC;
	}

	public boolean save(Contact conSave, Session sesCurrent) {
		try {
			return DominoStorageService.getInstance().saveObject(conSave, sesCurrent.getCurrentDatabase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
