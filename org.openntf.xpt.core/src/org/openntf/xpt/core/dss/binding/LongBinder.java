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

import lotus.domino.Document;

public class LongBinder implements IBinder<Long> {

	private static LongBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Long.TYPE);
			Long nValue = getValueFromStore(docCurrent, strNotesField, addValues);
			if (nValue != null) {
				mt.invoke(objCurrent, nValue.longValue());
			}
		} catch (Exception e) {
		}
	}

	public Long[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Long[] lngRC = new Long[2];
		try {
			long nOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
			long nValue = getValue(objCurrent, strJavaField).longValue();
			lngRC[0] = nOldValue;
			lngRC[1] = nValue;
			docCurrent.replaceItemValue(strNotesField, nValue);
		} catch (Exception e) {
		}
		return lngRC;

	}

	public static IBinder<Long> getInstance() {
		if (m_Binder == null) {
			m_Binder = new LongBinder();
		}
		return m_Binder;
	}

	private LongBinder() {

	}

	public Long getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (Long) mt.invoke(objCurrent);
		} catch (Exception ex) {

		}
		return null;
	}

	@Override
	public Long getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			long nValue = (long) docCurrent.getItemValueDouble(strNotesField);
			return new Long(nValue);
		} catch (Exception e) {
		}
		return null;
	}
}
