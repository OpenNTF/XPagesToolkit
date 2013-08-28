package org.openntf.xpt.demo.api;

import java.io.Serializable;
import java.util.List;

import org.openntf.xpt.demo.bo.Contact;
import org.openntf.xpt.demo.bo.ContactStorageService;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ContactSessionFacade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Contact> getAllContacts() {
		return ContactStorageService.getInstance().getAllContacts(ExtLibUtil.getCurrentSession());
	}
}
