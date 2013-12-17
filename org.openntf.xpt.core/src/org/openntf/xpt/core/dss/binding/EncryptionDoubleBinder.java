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

import lotus.domino.Document;

import org.openntf.xpt.core.base.BaseDoubleBinder;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.encryption.EncryptionService;

public class EncryptionDoubleBinder extends BaseDoubleBinder implements IBinder<Double>, IEncryptionBinder {
	private static EncryptionDoubleBinder m_Binder;

	private EncryptionDoubleBinder() {

	}

	public static EncryptionDoubleBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new EncryptionDoubleBinder();
		}
		return m_Binder;
	}


	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			if (hasAccess(addValues)) {
				Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Double.TYPE);
				Double dblVal = getValueFromStore(docCurrent, strNotesField, addValues);
				if (dblVal != null) {
					mt.invoke(objCurrent, dblVal.doubleValue());
				}
			}
		} catch (Exception e) {
		}
	}

	public Double[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Double[] dblRC = new Double[2];
		try {
			if (hasAccess(addValues)) {
				Double nOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
				double nValue = getValue(objCurrent, strJavaField).doubleValue();
				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(Double.toString(nOldValue));
				String encryptedValue = EncryptionService.getInstance().encrypt(Double.toString(nValue));
				dblRC[0] = nOldValue;
				dblRC[1] = nValue;

				docCurrent.replaceItemValue(strNotesField, encryptedValue);
			}
		}catch(DSSException e){
			System.out.println(e.getMessage());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dblRC;
	}

	@Override
	public Double getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) throws DSSException {
		try {
			if (hasAccess(additionalValues)) {
				String strDblValue = docCurrent.getItemValueString(strNotesField);
				String strDblValueDec = EncryptionService.getInstance().decrypt(strDblValue);
				if (strDblValueDec == null) {
					throw new DSSException("Decryption Failed: " + strNotesField);
				}
				if (!strDblValueDec.equals("")) {
					double nValue = new Double(strDblValueDec);
					return new Double(nValue);
				}
			}
		}catch (DSSException e) {
				throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String[] getChangeLogValues(Object[] arrObject, HashMap<String, Object> additionalValues) {
		String[] strRC = new String[arrObject.length];
		int i = 0;
		for (Object object : arrObject) {
			Double decDouble = (Double) object;
			if (decDouble != null) {
				String decString = Double.toString(decDouble);
				String encryptedValue = EncryptionService.getInstance().encrypt(decString);
				strRC[i] = encryptedValue;
			} else {
				strRC[i] = "";
			}
			i++;

		}
		return strRC;
	}

}
