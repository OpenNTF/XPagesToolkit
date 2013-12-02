package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lotus.domino.DateTime;
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

				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(dtOld.toString());
				
				System.out.println("dtCurrent = " + dtCurrent);
				dtRC[0] = dtOld; // /EncValue for Logger? Prob with return type
				dtRC[1] = dtCurrent;
				if (dtCurrent != null) {
					DateTime dt = docCurrent.getParentDatabase().getParent().createDateTime(dtCurrent);
					if (addValues.containsKey("dateOnly")) {
						dt = docCurrent.getParentDatabase().getParent().createDateTime(dt.getDateOnly());
					}
				
				
					String dtString = DateProcessor.getInstance().getDateAsStringToEncrypt(dtCurrent, addValues.containsKey("dateOnly"));
					System.out.println("dt = " + dt.toString());
					System.out.println("DtString = " + dtString);
					String encryptedValue = EncryptionService.getInstance().encrypt(dtString);
					System.out.println("encVal = " + encryptedValue);
					
					docCurrent.replaceItemValue(strNotesField, encryptedValue);
				} else {
					docCurrent.removeItem(strNotesField);
				}
			}
			System.out.println("Done");
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
				System.out.println("EncDate = " + encDate);
				decDate = EncryptionService.getInstance().decrypt(encDate);
System.out.println("DECDATE=" + decDate);
				
				if (decDate != null && decDate != "") {
					
					DateFormat formatter = new SimpleDateFormat(DateProcessor.getInstance().getDateFormatForEncryption(additionalValues.containsKey("dateOnly")));
					return (Date) formatter.parse(decDate);
	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
