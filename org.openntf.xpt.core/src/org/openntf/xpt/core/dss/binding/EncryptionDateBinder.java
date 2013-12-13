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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lotus.domino.Document;

import org.openntf.xpt.core.base.BaseDateBinder;
import org.openntf.xpt.core.dss.binding.util.DateProcessor;
import org.openntf.xpt.core.dss.encryption.EncryptionService;

public class EncryptionDateBinder extends BaseDateBinder implements IBinder<Date>, IEncryptionBinder {
	private static EncryptionDateBinder m_Binder;

	private EncryptionDateBinder() {

	}

	public static EncryptionDateBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new EncryptionDateBinder();
		}
		return m_Binder;
	}

	private boolean m_EncryptionFailed = false;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			if (hasAccess(addValues)) {
				Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Date.class);
				Date dtCurrent = getValueFromStore(docCurrent, strNotesField, addValues);
				if (dtCurrent != null) {
					mt.invoke(objCurrent, dtCurrent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Date[] dtRC = new Date[2];
		try {
			if (hasAccess(addValues)) {
				Date dtCurrent = getValue(objCurrent, strJavaField);
				Date dtOld = getValueFromStore(docCurrent, strNotesField, addValues);
				if (m_EncryptionFailed) {
					return null;
				}
				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(dtOld.toString());

				dtRC[0] = dtOld; // /EncValue for Logger? Prob with return
									// type
				dtRC[1] = dtCurrent;
				if (dtCurrent != null) {
					//DateTime dt = docCurrent.getParentDatabase().getParent().createDateTime(dtCurrent);
					//if (addValues.containsKey("dateOnly")) {
					//	dt = docCurrent.getParentDatabase().getParent().createDateTime(dt.getDateOnly());
					//}
					String dtString = DateProcessor.getInstance().getDateAsStringToEncrypt(dtCurrent, addValues.containsKey("dateOnly"));

					String encryptedValue = EncryptionService.getInstance().encrypt(dtString);

					docCurrent.replaceItemValue(strNotesField, encryptedValue);
				} else {
					docCurrent.removeItem(strNotesField);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dtRC;

	}

	@Override
	public Date getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			if (hasAccess(additionalValues)) {
				String encDate;
				String decDate;

				encDate = docCurrent.getItemValueString(strNotesField);
				decDate = EncryptionService.getInstance().decrypt(encDate);
				if (decDate == null) {
					m_EncryptionFailed = true;
				}
				if (decDate != null && !decDate.equals("")) {
					if(additionalValues.containsKey("dateOnly")){
						decDate = decDate.substring(0,10);
					}
					DateFormat formatter = new SimpleDateFormat(DateProcessor.getInstance().getDateFormatForEncryption(additionalValues.containsKey("dateOnly")));
					return (Date) formatter.parse(decDate);
				}
			}
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
			Date decDate = (Date) object;
			if (decDate != null) {
				String decString = DateProcessor.getInstance().getDateAsStringToEncrypt(decDate, additionalValues.containsKey("dateOnly"));
				String encryptedValue = EncryptionService.getInstance().encrypt(decString);
				strRC[i] = encryptedValue;
			} else {
				strRC[i] = null;
			}
			i++;
		}
		return strRC;
	}

}
