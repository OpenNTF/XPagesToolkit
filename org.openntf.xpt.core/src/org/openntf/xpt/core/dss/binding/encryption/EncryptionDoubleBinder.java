/**
 * Copyright 2013, WebGate Consulting AG
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
package org.openntf.xpt.core.dss.binding.encryption;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.IEncryptionBinder;
import org.openntf.xpt.core.dss.binding.util.BaseEncryptionBinderSupport;
import org.openntf.xpt.core.dss.encryption.EncryptionService;

public class EncryptionDoubleBinder extends AbstractBaseBinder<Double> implements IBinder<Double>, IEncryptionBinder {
	private static EncryptionDoubleBinder m_Binder;

	private EncryptionDoubleBinder() {

	}

	public static EncryptionDoubleBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new EncryptionDoubleBinder();
		}
		return m_Binder;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			if (BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, docCurrent.getParentDatabase())) {
				Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Double.TYPE);
				Double dblVal = getValueFromStore(docCurrent, vecCurrent, def);
				if (dblVal != null) {
					mt.invoke(objCurrent, dblVal.doubleValue());
				}
			}
		} catch (Exception e) {
		}
	}

	public Double[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Double[] dblRC = new Double[2];
		try {
			if (BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, docCurrent.getParentDatabase())) {
				Double nOldValue = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
				double nValue = getValue(objCurrent, def.getJavaField()).doubleValue();
				// String encryptedOldValue =
				// EncryptionService.getInstance().encrypt(Double.toString(nOldValue));
				String encryptedValue = EncryptionService.getInstance().encrypt(Double.toString(nValue));
				dblRC[0] = nOldValue;
				dblRC[1] = nValue;

				docCurrent.replaceItemValue(def.getNotesField(), encryptedValue);
			}
		} catch (DSSException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dblRC;
	}

	@Override
	public Double getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) throws DSSException {
		try {
			if (BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, docCurrent.getParentDatabase()) && !vecCurrent.isEmpty()) {
				String strDblValue = (String) vecCurrent.get(0);
				String strDblValueDec = EncryptionService.getInstance().decrypt(strDblValue);
				if (strDblValueDec == null) {
					throw new DSSException("Decryption failed: " + def.getJavaField() + " / " + def.getNotesField());
				}
				if (!strDblValueDec.equals("")) {
					return Double.valueOf(strDblValueDec);
				}
			}
		} catch (DSSException e) {
			throw e;
		} catch (Exception e) {
			throw new XPTRuntimeException("General Error", e);
		}
		return null;
	}

	@Override
	public String[] getChangeLogValues(Object[] arrObject, Definition def) {
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

	@Override
	public boolean hasAccess(Definition def, List<String> arrRoles) {
		return BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, arrRoles);
	}

}
