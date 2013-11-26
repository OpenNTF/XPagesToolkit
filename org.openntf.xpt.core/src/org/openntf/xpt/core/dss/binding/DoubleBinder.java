/*
 * © Copyright WebGate Consulting AG, 2012
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

import org.openntf.xpt.core.base.BaseDoubleBinder;

import lotus.domino.Document;

public class DoubleBinder extends BaseDoubleBinder implements IBinder<Double> {

	private static DoubleBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Double.TYPE);
			Double dblVal = getValueFromStore(docCurrent, strNotesField, addValues);
			if (dblVal != null) {
				mt.invoke(objCurrent, dblVal.doubleValue());
			}
		} catch (Exception e) {
		}
	}

	public Double[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Double[] dblRC = new Double[2];
		try {
			double nOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
			double nValue = getValue(objCurrent, strJavaField).doubleValue();
			dblRC[0] = nOldValue;
			dblRC[1] = nValue;
			docCurrent.replaceItemValue(strNotesField, nValue);
		} catch (Exception e) {
		}
		return dblRC;
	}

	public static IBinder<Double> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DoubleBinder();
		}
		return m_Binder;
	}

	private DoubleBinder() {

	}

	@Override
	public Double getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			double nValue = docCurrent.getItemValueDouble(strNotesField);
			return new Double(nValue);
		} catch (Exception e) {
		}
		return null;
	}
}
