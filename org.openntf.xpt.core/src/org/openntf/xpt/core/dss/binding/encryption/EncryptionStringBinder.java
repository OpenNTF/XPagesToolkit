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
package org.openntf.xpt.core.dss.binding.encryption;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.Document;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.IEncryptionBinder;
import org.openntf.xpt.core.dss.binding.util.BaseEncryptionBinderSupport;
import org.openntf.xpt.core.dss.binding.util.NamesProcessor;
import org.openntf.xpt.core.dss.encryption.EncryptionService;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class EncryptionStringBinder extends AbstractBaseBinder<String> implements IBinder<String>, IEncryptionBinder {
	private static EncryptionStringBinder m_Binder;

	private EncryptionStringBinder() {

	}

	public static EncryptionStringBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new EncryptionStringBinder();
		}
		return m_Binder;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {

			if (BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, docCurrent.getParentDatabase())) {

				Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), String.class);
				mt.invoke(objCurrent, getValueFromStore(docCurrent, vecCurrent, def));
			}
		} catch (DSSException dsse) {
			Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());
			log.log(Level.SEVERE, dsse.getMessage(), dsse);
		} catch (XPTRuntimeException rte) {
			throw rte;

		} catch (Exception e) {
			throw new XPTRuntimeException("General Error: ", e);
		}

	}

	public String[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		String[] arrRC = new String[2];
		try {
			if (BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, docCurrent.getParentDatabase())) {
				String strOldValue = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
				if (strOldValue == null) {
					return null;
				}
				String strValue = getValue(objCurrent, def.getJavaField());
				if (strValue == null) {
					strValue = "";
				}
				if (def.isReader() || def.isAuthor() || def.isAuthor()) {
					strValue = NamesProcessor.getInstance().setPerson(strValue, true, docCurrent.getParentDatabase().getParent());
				}
				String encryptedValue = EncryptionService.getInstance().encrypt(strValue);

				arrRC[0] = strOldValue;
				arrRC[1] = strValue;
				docCurrent.replaceItemValue(def.getNotesField(), encryptedValue);
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
	public String getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) throws DSSException {
		try {
			if (BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, docCurrent.getParentDatabase()) && !vecCurrent.isEmpty()) {
				String strValue = (String) vecCurrent.get(0);
				String decryptedValue = EncryptionService.getInstance().decrypt(strValue);
				if (decryptedValue == null) {
					throw new DSSException("Decryption Failed: " + def.getJavaField() + " / " + def.getNotesField());
				}
				decryptedValue = NamesProcessor.getInstance().getPerson(def, decryptedValue, docCurrent.getParentDatabase().getParent());
				return decryptedValue;
			}
		} catch (DSSException e) {
			throw e;
		} catch (Exception e) {
			throw new XPTRuntimeException("General Error", e);
		}
		return "";
	}

	@Override
	public String[] getChangeLogValues(Object[] arrObject, Definition def) {
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

	@Override
	public boolean hasAccess(Definition def, List<String> arrRoles) {
		return BaseEncryptionBinderSupport.INSTANCE.hasAccess(def, arrRoles);
	}

}
