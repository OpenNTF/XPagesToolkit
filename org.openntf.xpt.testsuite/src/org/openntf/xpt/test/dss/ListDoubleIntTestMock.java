package org.openntf.xpt.test.dss;

import java.util.List;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form="frmTestMok", PrimaryKeyField="ID", PrimaryFieldClass=String.class, View="LUPByID")
public class ListDoubleIntTestMock {

	@DominoEntity(FieldName="integerListField")
	private List<Integer> m_IntegerList;
	@DominoEntity(FieldName="doubleListField")
	private List<Double> m_DoubleList;
	public List<Double> getDoubleList() {
		return m_DoubleList;
	}
	public void setDoubleList(List<Double> doubleList) {
		m_DoubleList = doubleList;
	}
	public List<Integer> getIntegerList() {
		return m_IntegerList;
	}
	public void setIntegerList(List<Integer> integerList) {
		m_IntegerList = integerList;
	}
}
