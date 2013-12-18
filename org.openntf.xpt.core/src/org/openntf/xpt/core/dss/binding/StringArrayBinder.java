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
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;

import org.openntf.xpt.core.dss.binding.util.NamesProcessor;

public class StringArrayBinder implements IBinder<String[]> {

	private static StringArrayBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, new Class[] { String[].class });
			String[] strValues = getValueFromStore(docCurrent, strNotesField, addValues);
			if (strValues != null) {
				mt.invoke(objCurrent, new Object[] { strValues });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[][] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		String[][] strRC = new String[2][];
		try {
			String[] strOldValues = getRawValueFromStore(docCurrent, strNotesField);
			String[] strValues = getValue(objCurrent, strJavaField);
			strRC[0] = strOldValues;
			strRC[1] = strValues;
			Vector<String> vecValues = new Vector<String>(strValues.length);

			boolean isNamesValue = false;
			if (addValues != null && addValues.size() > 0) {
				docCurrent.replaceItemValue(strNotesField, "");
				Item iNotesField = docCurrent.getFirstItem(strNotesField);
				isNamesValue = NamesProcessor.getInstance().setNamesField(addValues, iNotesField);
			}

			for (String strVal : strValues) {
				vecValues.addElement(NamesProcessor.getInstance().setPerson(strVal, isNamesValue, docCurrent.getParentDatabase().getParent()));
			}
			strRC[1] = vecValues.toArray(new String[vecValues.size()]);
			docCurrent.replaceItemValue(strNotesField, vecValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRC;

	}

	public static IBinder<String[]> getInstance() {
		if (m_Binder == null) {
			m_Binder = new StringArrayBinder();
		}
		return m_Binder;
	}

	private StringArrayBinder() {

	}

	public String[] getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (String[]) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String[] getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			Vector<?> vecResult = docCurrent.getItemValue(strNotesField);
			String[] strValues = new String[vecResult.size()];

			int i = 0;
			for (Object strValue : vecResult) {
				strValues[i] = NamesProcessor.getInstance().getPerson(additionalValues, strValue.toString(), docCurrent.getParentDatabase().getParent());
				i += 1;
			}
			return strValues;

		} catch (Exception e) {
		}
		return null;
	}

	public String[] getRawValueFromStore(Document docCurrent, String strNotesField) {
		try {
			Vector<?> vecResult = docCurrent.getItemValue(strNotesField);
			String[] strValues = new String[vecResult.size()];

			int i = 0;
			for (Object strValue : vecResult) {
				strValues[i] = strValue.toString();
				i += 1;
			}
			return strValues;

		} catch (Exception e) {
		}
		return null;
	}

}
