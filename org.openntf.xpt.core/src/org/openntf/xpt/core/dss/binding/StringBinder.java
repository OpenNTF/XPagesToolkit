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
import lotus.domino.Item;
import org.openntf.xpt.core.base.BaseStringBinder;
import org.openntf.xpt.core.dss.binding.util.NamesProcessor;

public class StringBinder extends BaseStringBinder implements IBinder<String> {

	private static StringBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, String.class);

			String strValue = getValueFromStore(docCurrent, strNotesField, addValues);
			if (strValue != null) {
				mt.invoke(objCurrent, strValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		String[] arrRC = new String[2];
		try {
			boolean isNamesValue = false;
			String strOldValue = docCurrent.getItemValueString(strNotesField);
			String strValue = getValue(objCurrent, strJavaField);
			if (addValues != null && addValues.size() > 0) {
				docCurrent.replaceItemValue(strNotesField, "");
				Item iNotesField = docCurrent.getFirstItem(strNotesField);
				isNamesValue = NamesProcessor.getInstance().setNamesField(addValues, iNotesField);
				strValue = NamesProcessor.getInstance().setPerson(strValue, isNamesValue, docCurrent.getParentDatabase().getParent());
			}
			arrRC[0] = strOldValue;
			arrRC[1] = strValue;
			docCurrent.replaceItemValue(strNotesField, strValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrRC;
	}

	public static IBinder<String> getInstance() {
		if (m_Binder == null) {
			m_Binder = new StringBinder();
		}
		return m_Binder;
	}

	private StringBinder() {

	}

	@Override
	public String getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			String strValue = docCurrent.getItemValueString(strNotesField);
			strValue = NamesProcessor.getInstance().getPerson(additionalValues, strValue,docCurrent.getParentDatabase().getParent());
			return strValue;

		} catch (Exception e) {
		}
		return null;
	}
}
