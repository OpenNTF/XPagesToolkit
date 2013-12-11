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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;

import org.openntf.xpt.core.dss.binding.util.NamesProcessor;

public class ListStringBinder implements IBinder<List<String>> {

	private static ListStringBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, List.class);
			List<String> lstValues = getValueFromStore(docCurrent, strNotesField, addValues);
			if (lstValues != null) {
				mt.invoke(objCurrent, lstValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String>[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		@SuppressWarnings("unchecked")
		List<String>[] lstRC = new ArrayList[2];
		try {
			List<String> lstOldValues = getValueFromStore(docCurrent, strNotesField, addValues);
			List<String> lstValues = getValue(objCurrent, strJavaField);

			lstRC[0] = lstOldValues;
			lstRC[1] = lstValues;
			Vector<String> vValues = new Vector<String>();

			if (lstValues != null) {
				boolean isNamesValue = false;
				if (addValues != null && addValues.size() > 0) {
					docCurrent.replaceItemValue(strNotesField, "");
					Item iNotesField = docCurrent.getFirstItem(strNotesField);
					isNamesValue = NamesProcessor.getInstance().setNamesField(addValues, iNotesField);
				}

				for (String strValue : lstValues) {
					vValues.add(NamesProcessor.getInstance().setPerson(strValue, isNamesValue));
				}
				lstRC[1] = new ArrayList<String>(vValues);

			}
			docCurrent.replaceItemValue(strNotesField, vValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;
	}

	public static IBinder<List<String>> getInstance() {
		if (m_Binder == null) {
			m_Binder = new ListStringBinder();
		}
		return m_Binder;
	}

	private ListStringBinder() {

	}

	@SuppressWarnings("unchecked")
	public List<String> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<String>) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			Vector<?> vecResult = docCurrent.getItemValue(strNotesField);
			ArrayList<String> lstValues = new ArrayList<String>();
			for (Object strValue : vecResult) {
				lstValues.add(NamesProcessor.getInstance().getPerson(additionalValues, strValue.toString()));
			}
			return lstValues;
		} catch (Exception e) {
		}
		return null;
	}

}
