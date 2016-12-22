package org.openntf.xpt.test.dss.mock;

import java.util.Properties;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form = "frmTestMok", PrimaryKeyField = "ID", PrimaryFieldClass = String.class, View = "LUPByID")
public class PropertiesTestMock {
	@DominoEntity(FieldName = "id")
	private String m_ID;
	@DominoEntity(FieldName = "name")
	private String m_Name;
	@DominoEntity(FieldName = "propsMime")
	private Properties m_Props;

	public String getID() {
		return m_ID;
	}

	public void setID(String m_ID) {
		this.m_ID = m_ID;
	}

	public String getName() {
		return m_Name;
	}

	public void setName(String m_Name) {
		this.m_Name = m_Name;
	}

	public Properties getProps() {
		return m_Props;
	}

	public void setProps(Properties m_Props) {
		this.m_Props = m_Props;
	}

}
