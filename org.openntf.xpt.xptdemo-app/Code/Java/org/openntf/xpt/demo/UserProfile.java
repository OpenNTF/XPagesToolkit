package org.openntf.xpt.demo;

import java.io.Serializable;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form="User", PrimaryFieldClass=String.class,PrimaryKeyField="UserName", View="UserByName")
public class UserProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName="UserName")
	private String m_UserName;
	@DominoEntity(FieldName="Street")
	private String m_Street;
	@DominoEntity(FieldName="Location")
	private String m_Location;
	@DominoEntity(FieldName="ZIP")
	private String m_ZIP;
	
	public void setUserName(String userName) {
		m_UserName = userName;
	}
	public String getUserName() {
		return m_UserName;
	}
	public void setStreet(String street) {
		m_Street = street;
	}
	public String getStreet() {
		return m_Street;
	}
	public void setZIP(String zIP) {
		m_ZIP = zIP;
	}
	public String getZIP() {
		return m_ZIP;
	}
	public void setLocation(String location) {
		m_Location = location;
	}
	public String getLocation() {
		return m_Location;
	}
	
}
