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

import org.openntf.xpt.core.base.BaseBooleanBinder;

import lotus.domino.Document;

public class BooleanBinder extends BaseBooleanBinder implements IBinder<Boolean> {

	private static BooleanBinder m_Binder;

	private BooleanBinder(){
	  
	}
	
	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
		 	Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Boolean.TYPE);
			mt.invoke(objCurrent, getValueFromStore(docCurrent, strNotesField, addValues).booleanValue());
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	public Boolean[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Boolean[] blRC = new Boolean[2];
		try {
			boolean blValue = getValue(objCurrent, strJavaField).booleanValue();
			boolean blOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
			blRC[0] = blOldValue;
			blRC[1] = blValue;
			if (blValue) {
				docCurrent.replaceItemValue(strNotesField, "1");
			} else {
				docCurrent.replaceItemValue(strNotesField, "");
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return blRC;
	}

	public static IBinder<Boolean> getInstance() {
		if (m_Binder == null) {
			m_Binder = new BooleanBinder();
		}
		return m_Binder;
	}

	@Override
	public Boolean getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> addValues) {
		try {
			String strValue = docCurrent.getItemValueString(strNotesField);
			return "1".equals(strValue);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return false;
	}
}
