package org.openntf.xpt.demo.bo;

import java.io.Serializable;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form = "Contact", PrimaryFieldClass = String.class, PrimaryKeyField = "ID", View = "LUPContactByID")
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName = "ID")
	private String m_ID;
	@DominoEntity(FieldName = "FirstName")
	private String m_FirstName;
	@DominoEntity(FieldName = "LastName")
	private String m_LastName;
	@DominoEntity(FieldName = "City")
	private String m_City;
	@DominoEntity(FieldName = "Email")
	private String m_Email;
	@DominoEntity(FieldName = "State")
	private String m_State;

	public String getID() {
		return m_ID;
	}

	public void setID(String id) {
		m_ID = id;
	}

	public String getFirstName() {
		return m_FirstName;
	}

	public void setFirstName(String firstName) {
		m_FirstName = firstName;
	}

	public String getLastName() {
		return m_LastName;
	}

	public void setLastName(String lastName) {
		m_LastName = lastName;
	}

	public String getCity() {
		return m_City;
	}

	public void setCity(String city) {
		m_City = city;
	}

	public String getEmail() {
		return m_Email;
	}

	public void setEmail(String email) {
		m_Email = email;
	}

	public String getState() {
		return m_State;
	}

	public void setState(String state) {
		m_State = state;
	}

}
