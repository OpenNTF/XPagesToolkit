package org.openntf.xpt.demo.bo;

import java.io.Serializable;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form="frmAdrEmb", PrimaryFieldClass=String.class,PrimaryKeyField="ID",View="lupNotUsed")
public class Address implements Serializable {

	@DominoEntity(FieldName="ID")
	private String m_ID;
	@DominoEntity(FieldName="Address")
	private String m_Address;
	@DominoEntity(FieldName="ZIP")
	private String m_ZIP;
	@DominoEntity(FieldName="Location")
	private String m_Location;
}
