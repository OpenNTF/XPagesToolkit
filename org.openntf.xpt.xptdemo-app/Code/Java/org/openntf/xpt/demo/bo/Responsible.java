package org.openntf.xpt.demo.bo;

import java.io.Serializable;
import java.util.UUID;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
//NONE of these infos are relevant for Embedded objects!
@DominoStore(Form="frmEmbedded", View="lupByID",PrimaryFieldClass=String.class,PrimaryKeyField="ID")
public class Responsible implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName="ID")
	private String m_ID = UUID.randomUUID().toString();
	@DominoEntity(FieldName="Name")
	private String m_Name;
	@DominoEntity(FieldName="Department")
	private String m_Department;
	public void setID(String iD) {
		m_ID = iD;
	}
	public String getID() {
		return m_ID;
	}
	public void setName(String name) {
		m_Name = name;
	}
	public String getName() {
		return m_Name;
	}
	public void setDepartment(String department) {
		m_Department = department;
	}
	public String getDepartment() {
		return m_Department;
	}

}
