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

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Double.TYPE);
			double nValue = docCurrent.getItemValueDouble(strNotesField);
			mt.invoke(objCurrent, nValue);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			double nValue = getValue(objCurrent, strJavaField).doubleValue();
			docCurrent.replaceItemValue(strNotesField, nValue);
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	public static IBinder<Double> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DoubleBinder();
		}
		return m_Binder;
	}

	private DoubleBinder() {

	}
}
