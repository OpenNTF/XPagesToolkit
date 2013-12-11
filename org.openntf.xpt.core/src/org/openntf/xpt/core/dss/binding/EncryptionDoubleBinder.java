package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.util.HashMap;

import lotus.domino.Document;

import org.openntf.xpt.core.base.BaseDoubleBinder;
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

	private boolean m_EncryptionFailed = false;

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
				if (m_EncryptionFailed) {
					return null;
				}// / TODO
				double nValue = getValue(objCurrent, strJavaField).doubleValue();
				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(Double.toString(nOldValue));
				String encryptedValue = EncryptionService.getInstance().encrypt(Double.toString(nValue));
				dblRC[0] = nOldValue;
				dblRC[1] = nValue;

				docCurrent.replaceItemValue(strNotesField, encryptedValue);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dblRC;
	}

	@Override
	public Double getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		try {
			if (hasAccess(additionalValues)) {
				String strDblValue = docCurrent.getItemValueString(strNotesField);
				String strDblValueDec = EncryptionService.getInstance().decrypt(strDblValue);
				if (strDblValueDec == null) {
					m_EncryptionFailed = true;
				}
				if (strDblValueDec != null && !strDblValueDec.equals("")) {
					double nValue = new Double(strDblValueDec);
					return new Double(nValue);
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
			Double decDouble = (Double) object;
			if (decDouble != null) {
				String decString = Double.toString(decDouble);
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
