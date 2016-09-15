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
package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.util.NamesProcessor;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class StringArrayBinder implements IBinder<String[]> {

	private static StringArrayBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), new Class[] { String[].class });
			String[] strValues = getValueFromStore(docCurrent, vecCurrent, def);
			if (strValues != null) {
				mt.invoke(objCurrent, new Object[] { strValues });
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public String[][] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		String[][] strRC = new String[2][];
		try {
			String[] strOldValues = getRawValueFromStore(docCurrent, def.getNotesField());
			String[] strValues = getValue(objCurrent, def.getJavaField());
			strRC[0] = strOldValues;
			strRC[1] = strValues;
			Vector<String> vecValues = new Vector<String>(strValues.length);

			boolean isNamesValue = false;
			if (def.isAuthor() || def.isReader() || def.isNames()) {
				Item iNotesField = docCurrent.replaceItemValue(def.getNotesField(), "");
				isNamesValue = NamesProcessor.getInstance().setNamesField(def, iNotesField);
				iNotesField.recycle();
			}

			for (String strVal : strValues) {
				vecValues.addElement(NamesProcessor.getInstance().setPerson(strVal, isNamesValue, docCurrent.getParentDatabase().getParent()));
			}
			strRC[1] = vecValues.toArray(new String[vecValues.size()]);
			Item notesItem =docCurrent.replaceItemValue(def.getNotesField(), vecValues);
			notesItem.setSummary(def.isNotesSummary());
			notesItem.recycle();

		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
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
	public String[] getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			String[] strValues = new String[vecCurrent.size()];
			int i = 0;
			for (Object strValue : vecCurrent) {
				strValues[i] = NamesProcessor.getInstance().getPerson(def, strValue.toString(), docCurrent.getParentDatabase().getParent());
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
