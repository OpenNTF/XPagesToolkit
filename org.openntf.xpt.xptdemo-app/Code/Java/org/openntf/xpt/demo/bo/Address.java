package org.openntf.xpt.demo.bo;

import java.io.Serializable;
import java.util.UUID;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form="frmAdrEmb", PrimaryFieldClass=String.class,PrimaryKeyField="ID",View="lupNotUsed")
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4498384696108940852L;
	@DominoEntity(FieldName="ID")
	private String m_ID = UUID.randomUUID().toString();
	@DominoEntity(FieldName="Address")
	private String m_Address;
	@DominoEntity(FieldName="ZIP")
	private String m_ZIP;
	@DominoEntity(FieldName="Location")
	private String m_Location;
	public void setID(String iD) {
		m_ID = iD;
	}
	public String getID() {
		return m_ID;
	}
	public void setAddress(String address) {
		m_Address = address;
	}
	public String getAddress() {
		return m_Address;
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
