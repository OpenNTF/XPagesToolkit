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

import org.openntf.xpt.core.base.BaseIntegerBinder;

import lotus.domino.Document;

public class IntBinder extends BaseIntegerBinder implements IBinder<Integer> {

	private static IntBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Integer.TYPE);
			Integer nValue = getValueFromStore(docCurrent, strNotesField, addValues);
			if (nValue != null) {
				mt.invoke(objCurrent, nValue.intValue());
			}
		} catch (Exception e) {
		}
	}

	public Integer[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Integer[] nRC = new Integer[2];
		try {
			int nValueOld = getValueFromStore(docCurrent, strNotesField, addValues);
			int nValue = getValue(objCurrent, strJavaField).intValue();
			nRC[0] = nValueOld;
			nRC[1] = nValue;
			docCurrent.replaceItemValue(strNotesField, nValue);
		} catch (Exception e) {
		}
		return nRC;
	}

	public static IBinder<Integer> getInstance() {
		if (m_Binder == null) {
			m_Binder = new IntBinder();
		}
		return m_Binder;
	}

	private IntBinder() {

	}

	@Override
	public Integer getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			int nValue = docCurrent.getItemValueInteger(strNotesField);
			return new Integer(nValue);
		} catch (Exception e) {
		}
		return null;
	}
}
