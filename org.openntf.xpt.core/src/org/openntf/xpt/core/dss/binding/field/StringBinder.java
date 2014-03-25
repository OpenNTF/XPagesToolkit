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
package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;

import org.openntf.xpt.core.base.BaseStringBinder;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.util.NamesProcessor;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class StringBinder extends BaseStringBinder implements IBinder<String> {

	private static StringBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), String.class);

			String strValue = getValueFromStore(docCurrent, vecCurrent, def);
			if (strValue != null) {
				mt.invoke(objCurrent, strValue);
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public String[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		String[] arrRC = new String[2];
		try {
			boolean isNamesValue = false;
			String strOldValue = docCurrent.getItemValueString(def.getNotesField());
			String strValue = getValue(objCurrent, def.getJavaField());
			if (def.isAuthor() || def.isReader() || def.isNames()) {
				// Changed to a oneLine Call
				Item iNotesField = docCurrent.replaceItemValue(def.getNotesField(), "");
				// Item iNotesField = docCurrent.getFirstItem(strNotesField);
				isNamesValue = NamesProcessor.getInstance().setNamesField(def, iNotesField);
				strValue = NamesProcessor.getInstance().setPerson(strValue, isNamesValue, docCurrent.getParentDatabase().getParent());
			}
			arrRC[0] = strOldValue;
			arrRC[1] = strValue;
			docCurrent.replaceItemValue(def.getNotesField(), strValue);

		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
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
	public String getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			String strValue = (String) vecCurrent.get(0);
			strValue = NamesProcessor.getInstance().getPerson(def, strValue, docCurrent.getParentDatabase().getParent());
			return strValue;

		} catch (Exception e) {
		}
		return null;
	}
}
