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
}
