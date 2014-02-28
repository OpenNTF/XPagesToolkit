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

import org.openntf.xpt.core.base.BaseBooleanBinder;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;

public class BooleanBinder extends BaseBooleanBinder implements IBinder<Boolean> {

	private static BooleanBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Boolean.TYPE);
			mt.invoke(objCurrent, getValueFromStore(docCurrent, vecValues, def));
		} catch (Exception e) {
		}
	}

	public Boolean[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Boolean[] blRC = new Boolean[2];
		try {
			boolean blValue = getValue(objCurrent, def.getJavaField()).booleanValue();
			boolean blOldValue = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			blRC[0] = blOldValue;
			blRC[1] = blValue;
			if (blValue) {
				docCurrent.replaceItemValue(def.getNotesField(), "1");
			} else {
				docCurrent.replaceItemValue(def.getNotesField(), "");
			}
		} catch (Exception e) {
		}
		return blRC;

	}

	public static IBinder<Boolean> getInstance() {
		if (m_Binder == null) {
			m_Binder = new BooleanBinder();
		}
		return m_Binder;
	}

	private BooleanBinder() {

	}

	@Override
	public Boolean getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) {
		if (!vecValues.isEmpty()) {
			String strValue = (String) vecValues.get(0);
			return "1".equals(strValue) ? Boolean.TRUE : Boolean.FALSE;
		}
		return false;
	}

}
