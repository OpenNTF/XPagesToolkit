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
				double nOldValue = getValueFromStore(docCurrent, strNotesField, addValues);
				double nValue = getValue(objCurrent, strJavaField).doubleValue();
				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(Double.toString(nOldValue));
				String encryptedValue = EncryptionService.getInstance().encrypt(Double.toString(nValue));
				dblRC[0] = nOldValue; /// Do I have to return Enc Values? Ret Type!
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
				if(strDblValueDec != null && !strDblValueDec.equals("")){
					double nValue = new Double(strDblValueDec);
					return new Double(nValue);
				}else{
					return new Double(0); ///DefaultValue??
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
