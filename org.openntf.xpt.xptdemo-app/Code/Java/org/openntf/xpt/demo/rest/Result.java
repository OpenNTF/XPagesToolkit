package org.openntf.xpt.demo.rest;

import java.io.Serializable;
import java.util.List;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;
import org.openntf.xpt.demo.bo.Contact;

@JSONObject
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONEntity(jsonproperty="status")
	private String m_Status;
	@JSONEntity(jsonproperty="contacts")
	private List<Contact> m_Contacts;
	public void setStatus(String status) {
		m_Status = status;
	}
	public String getStatus() {
		return m_Status;
	}
	public void setContacts(List<Contact> contacts) {
		m_Contacts = contacts;
	}
	public List<Contact> getContacts() {
		return m_Contacts;
	}
}
