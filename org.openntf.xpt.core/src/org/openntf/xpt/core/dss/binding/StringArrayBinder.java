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

	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					new Class[] { String[].class });
			Vector<?> vecResult = docCurrent.getItemValue(strNotesField);
			//String[] strValues = (String[]) vecResult.toArray(new String[vecResult.size()]);
			String[] strValues = new String[vecResult.size()];
				
			int i = 0;
			for(Object strValue: vecResult){
				strValues[i] = NamesProcessor.getInstance().getPerson(addValues, strValue.toString());
				i += 1;
			}
			
			mt.invoke(objCurrent, new Object[] { strValues });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			String[] strValues = getValue(objCurrent, strJavaField);
			Vector<String> vecValues = new Vector<String>(strValues.length);
			
			boolean isNamesValue = false;
			if(addValues != null && addValues.size() > 0){
				docCurrent.replaceItemValue(strNotesField,"");
				Item iNotesField = docCurrent.getFirstItem(strNotesField);
				isNamesValue = NamesProcessor.getInstance().setNamesField(addValues, iNotesField);
			}
			
			for (String strVal : strValues) {
				vecValues.addElement(NamesProcessor.getInstance().setPerson(strVal, isNamesValue));	
			}
			
			docCurrent.replaceItemValue(strNotesField, vecValues);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static IBinder<String[]> getInstance() {
		if (m_Binder == null) {
			m_Binder = new StringArrayBinder();
		}
		return m_Binder;
	}

	private StringArrayBinder(){
		
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

}
