package org.openntf.xpt.test.dss.mock;

import java.util.Date;
import java.util.List;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form = "frmTestMok", PrimaryKeyField = "ID", PrimaryFieldClass = String.class, View = "LUPByID")
public class FieldBinderTestMock {

	@DominoEntity(FieldName = "boolTypeField")
	private boolean m_BoolType;
	@DominoEntity(FieldName = "boolClassField")
	private Boolean m_BoolClass;
	@DominoEntity(FieldName = "intTypeField")
	private int m_IntType;
	@DominoEntity(FieldName = "intClassField")
	private Integer m_IntClass;
	@DominoEntity(FieldName = "doubleTypeField")
	private double m_DoubleType;
	@DominoEntity(FieldName = "doubleClassField")
	private Double m_DoubleClass;
	@DominoEntity(FieldName = "dateField")
	private Date m_Date;
	@DominoEntity(FieldName = "longClassField")
	private Long m_LongClass;
	@DominoEntity(FieldName = "longTypeField")
	private long m_LongType;
	@DominoEntity(FieldName = "stringField")
	private String m_String;
	@DominoEntity(FieldName = "enumField")
	private TestEnum m_Enum;
	@DominoEntity(FieldName = "stringListField")
	private List<String> m_StringList;
	@DominoEntity(FieldName = "stringArrayField")
	private String[] m_StringArray;
	public boolean isBoolType() {
		return m_BoolType;
	}
	public void setBoolType(boolean boolType) {
		m_BoolType = boolType;
	}
	public Boolean getBoolClass() {
		return m_BoolClass;
	}
	public void setBoolClass(Boolean boolClass) {
		m_BoolClass = boolClass;
	}
	public int getIntType() {
		return m_IntType;
	}
	public void setIntType(int intType) {
		m_IntType = intType;
	}
	public Integer getIntClass() {
		return m_IntClass;
	}
	public void setIntClass(Integer intClass) {
		m_IntClass = intClass;
	}
	public double getDoubleType() {
		return m_DoubleType;
	}
	public void setDoubleType(double doubleType) {
		m_DoubleType = doubleType;
	}
	public Double getDoubleClass() {
		return m_DoubleClass;
	}
	public void setDoubleClass(Double doubleClass) {
		m_DoubleClass = doubleClass;
	}
	public Date getDate() {
		return m_Date;
	}
	public void setDate(Date date) {
		m_Date = date;
	}
	public Long getLongClass() {
		return m_LongClass;
	}
	public void setLongClass(Long longClass) {
		m_LongClass = longClass;
	}
	public long getLongType() {
		return m_LongType;
	}
	public void setLongType(long longType) {
		m_LongType = longType;
	}
	public String getString() {
		return m_String;
	}
	public void setString(String string) {
		m_String = string;
	}
	public TestEnum getEnum() {
		return m_Enum;
	}
	public void setEnum(TestEnum enum1) {
		m_Enum = enum1;
	}
	public List<String> getStringList() {
		return m_StringList;
	}
	public void setStringList(List<String> stringList) {
		m_StringList = stringList;
	}
	public String[] getStringArray() {
		return m_StringArray;
	}
	public void setStringArray(String[] stringArray) {
		m_StringArray = stringArray;
	}
	
	

}
