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

import org.openntf.xpt.core.base.BaseStringBinder;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.util.NamesProcessor;
import org.openntf.xpt.core.dss.encryption.EncryptionService;

public class EncryptionStringBinder extends BaseStringBinder implements IBinder<String>, IEncryptionBinder {
	private static EncryptionStringBinder m_Binder;

	private EncryptionStringBinder() {

	}

	public static EncryptionStringBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new EncryptionStringBinder();
		}
		return m_Binder;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {

			if (hasAccess(addValues, docCurrent.getParentDatabase())) {

				Method mt = objCurrent.getClass().getMethod("set" + strJavaField, String.class);
				Vector<?> vecString = docCurrent.getParentDatabase().getParent().evaluate(strNotesField, docCurrent);
				if (vecString.size() > 0) {
					String strCurrent = (String) vecString.elementAt(0);

					String decryptedValue = EncryptionService.getInstance().decrypt(strCurrent);
					mt.invoke(objCurrent, decryptedValue);
				}
			}
		} catch (Exception e) {
		}

	}

	public String[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		String[] arrRC = new String[2];
		try {
			if (hasAccess(addValues, docCurrent.getParentDatabase())) {
				// boolean isNamesValue = false;
				String strOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
				if (strOldValue == null) {
					return null;
				}
				String strValue = getValue(objCurrent, strJavaField);

				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(strOldValue);
				if ((addValues.containsKey("isReader") || addValues.containsKey("isAuthor") || addValues.containsKey("isNames"))) {
					strValue = NamesProcessor.getInstance().setPerson(strValue, true, docCurrent.getParentDatabase().getParent());
				}
				String encryptedValue = EncryptionService.getInstance().encrypt(strValue);

				arrRC[0] = strOldValue;
				arrRC[1] = strValue;
				docCurrent.replaceItemValue(strNotesField, encryptedValue);
			}
		} catch (DSSException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrRC;
	}

	@Override
	public String getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) throws DSSException {
		try {
			if (hasAccess(additionalValues, docCurrent.getParentDatabase())) {
				String strValue = docCurrent.getItemValueString(strNotesField);
				String decryptedValue = EncryptionService.getInstance().decrypt(strValue);
				if (decryptedValue == null) {
					throw new DSSException("Decryption Failed: " + strNotesField);
				}
				decryptedValue = NamesProcessor.getInstance().getPerson(additionalValues, decryptedValue, docCurrent.getParentDatabase().getParent());
				return decryptedValue;
			}
		} catch (DSSException e) {
			throw e;
		} catch (Exception e) {
		}
		return "";
	}

	@Override
	public String[] getChangeLogValues(Object[] arrObject, HashMap<String, Object> additionalValues) {
		String[] strRC = new String[arrObject.length];
		int i = 0;
		for (Object object : arrObject) {
			String decString = (String) object;
			if (decString != null) {
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
