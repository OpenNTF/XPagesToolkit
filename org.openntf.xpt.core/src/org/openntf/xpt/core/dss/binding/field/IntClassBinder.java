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

import org.openntf.xpt.core.base.BaseIntegerBinder;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class IntClassBinder extends BaseIntegerBinder implements IBinder<Integer> {

	private static IntClassBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Integer.class);
			Integer nValue = getValueFromStore(docCurrent, vecCurrent, def);
			if (nValue != null) {
				mt.invoke(objCurrent, nValue.intValue());
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public Integer[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Integer[] nRC = new Integer[2];
		try {
			Integer nValueOld = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			Integer nValue = getValue(objCurrent, def.getJavaField());
			nRC[0] = nValueOld;
			nRC[1] = nValue;
			docCurrent.replaceItemValue(def.getNotesField(), nValue);
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return nRC;
	}

	public static IBinder<Integer> getInstance() {
		if (m_Binder == null) {
			m_Binder = new IntClassBinder();
		}
		return m_Binder;
	}

	private IntClassBinder() {

	}

	@Override
	public Integer getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		if (!vecCurrent.isEmpty()) {
			Double dblValue = (Double) vecCurrent.get(0);
			return Integer.valueOf(dblValue.intValue());
		}
		return null;
	}
}
