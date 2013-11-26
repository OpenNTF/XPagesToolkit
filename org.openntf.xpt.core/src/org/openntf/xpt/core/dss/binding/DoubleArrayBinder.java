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

public class DoubleArrayBinder implements IBinder<Double[]> {

	private static DoubleArrayBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, new Class[] { Double[].class });
			Double[] nValue = getValueFromStore(docCurrent, strNotesField, addValues);
			if (nValue != null) {
				mt.invoke(objCurrent, new Object[] { nValue });
			}
		} catch (Exception e) {
		}
	}

	public Double[][] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Double[][] dblRC = new Double[2][];
		try {
			Double[] nOldValues = getValueFromStore(docCurrent, strNotesField, addValues);
			Double[] nValues = getValue(objCurrent, strJavaField);
			dblRC[0] = nOldValues;
			dblRC[1] = nValues;
			Vector<Double> vecValues = new Vector<Double>(nValues.length);
			for (Double nVal : nValues) {
				vecValues.addElement(nVal);
			}
			docCurrent.replaceItemValue(strNotesField, vecValues);
		} catch (Exception e) {
		}
		return dblRC;
	}

	public static IBinder<Double[]> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DoubleArrayBinder();
		}
		return m_Binder;
	}

	private DoubleArrayBinder() {

	}

	public Double[] getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (Double[]) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double[] getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			Vector<Double[]> vecResult = docCurrent.getItemValue(strNotesField);
			return (Double[]) vecResult.toArray(new Double[vecResult.size()]);
		} catch (Exception e) {
		}
		return null;
	}

}
