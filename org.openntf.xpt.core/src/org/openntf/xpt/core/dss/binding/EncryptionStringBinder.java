package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import org.openntf.xpt.core.dss.binding.util.NamesProcessor;
import org.openntf.xpt.core.dss.encryption.EncryptionService;

import lotus.domino.Document;
import lotus.domino.Item;

public class EncryptionStringBinder implements IBinder<String>, IEncryptionBinder{
	private static EncryptionStringBinder m_Binder;

	private EncryptionStringBinder() {

	}

	public static EncryptionStringBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new EncryptionStringBinder();
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
				
				String decryptedValue = EncryptionService.getInstance().decrypt(strCurrent);
						
				mt.invoke(objCurrent, decryptedValue);
			}
		} catch (Exception e) {
		}

	}

	public String[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		String[] arrRC = new String[2];
		try {
			boolean isNamesValue = false;
			String strOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
			String strValue = getValue(objCurrent, strJavaField);
			if (addValues != null && addValues.size() > 0) {
				docCurrent.replaceItemValue(strNotesField, "");
				Item iNotesField = docCurrent.getFirstItem(strNotesField);
				isNamesValue = NamesProcessor.getInstance().setNamesField(addValues, iNotesField);
				strValue = NamesProcessor.getInstance().setPerson(strValue, isNamesValue);
			}
			
			String encryptedOldValue = EncryptionService.getInstance().encrypt(strOldValue);
			String encryptedValue = EncryptionService.getInstance().encrypt(strValue);
			
			arrRC[0] = encryptedOldValue;
			arrRC[1] = encryptedValue;
			docCurrent.replaceItemValue(strNotesField, encryptedValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrRC;
	}


	@Override
	public String getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		// TODO Auto-generated method stub
		return null;
	}
}
