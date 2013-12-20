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
package org.openntf.xpt.demo.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openntf.xpt.demo.bo.Contact;
import org.openntf.xpt.demo.bo.ContactStorageService;
import org.openntf.xpt.demo.rest.RestGenerator;

import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.xsp.extlib.component.rest.CustomService;
import com.ibm.xsp.extlib.component.rest.CustomServiceBean;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ContactSessionFacade extends CustomServiceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Contact> getAllContacts() {
		return ContactStorageService.getInstance().getAll("AllContacts");
	}

	public Contact getContactByID(String strID) {
		return ContactStorageService.getInstance().getById(strID);
	}

	public boolean saveContact(Contact conCurrent) {
		return ContactStorageService.getInstance().save(conCurrent);
	}

	public List<Contact> getMyContacts() {
		List<String> lstFields = new ArrayList<String>();
		lstFields.add("Observer");
		return ContactStorageService.getInstance().getAllMyObjects("AllContacts", lstFields);
	}

	public void addObserver(Contact conCurrent) {
		try {
			conCurrent.addObserver(ExtLibUtil.getCurrentSession().getEffectiveUserName());
			saveContact(conCurrent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeObserver(Contact conCurrent) {
		try {
			conCurrent.removeObserver(ExtLibUtil.getCurrentSession().getEffectiveUserName());
			saveContact(conCurrent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void renderService(CustomService cs, RestServiceEngine engine) throws ServiceException {
		RestGenerator.INSTANCE.handleRequest(engine);
	}
}
