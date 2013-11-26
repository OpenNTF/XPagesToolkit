/*
 * © Copyright WebGate Consulting AG, 2013
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
package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import lotus.domino.Document;

public class FormulaDoubleBinder implements IBinder<Double>, IFormulaBinder {
	private static FormulaDoubleBinder m_Binder;

	private FormulaDoubleBinder() {

	}

	public static FormulaDoubleBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaDoubleBinder();
		}
		return m_Binder;
	}

	public Double getValue(Object objCurrent, String strJavaField) {
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					Double.class);
			Vector<?> vecDouble = docCurrent.getParentDatabase().getParent()
					.evaluate(strNotesField, docCurrent);
			if (vecDouble.size() > 0) {
				Double dblCurrent = (Double) vecDouble.elementAt(0);
				mt.invoke(objCurrent, dblCurrent);
			}
		} catch (Exception e) {
		}
	}

	public Double[] processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String JavaField, HashMap<String, Object> addValues) {
		return null;
	}

	@Override
	public Double getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		// TODO Auto-generated method stub
		return null;
	}

}
