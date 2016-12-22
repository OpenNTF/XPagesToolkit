/**
 * Copyright 2013, WebGate Consulting AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.xpt.test.dss.mock;

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
