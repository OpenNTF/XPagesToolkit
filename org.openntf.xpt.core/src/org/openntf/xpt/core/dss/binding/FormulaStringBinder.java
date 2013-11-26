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

public class FormulaStringBinder implements IBinder<String>, IFormulaBinder {
	private static FormulaStringBinder m_Binder;

	private FormulaStringBinder() {

	}

	public static FormulaStringBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaStringBinder();
		}
		return m_Binder;
	}

	public String getValue(Object objCurrent, String strJavaField) {
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, String.class);
			Vector<?> vecString = docCurrent.getParentDatabase().getParent().evaluate(strNotesField, docCurrent);
			if (vecString.size() > 0) {
				String strCurrent = (String) vecString.elementAt(0);
				mt.invoke(objCurrent, strCurrent);
			}
		} catch (Exception e) {
		}

	}

	public String[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String JavaField, HashMap<String, Object> addValues) {
		return null;
	}

	@Override
	public String getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		// TODO Auto-generated method stub
		return null;
	}

}
