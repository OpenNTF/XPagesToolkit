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
package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class ENumBinder implements IBinder<Enum<?>> {

	private static ENumBinder m_Binder = new ENumBinder();

	public static ENumBinder getInstance() {
		return m_Binder;
	}

	private ENumBinder() {
	}

	@Override
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), (Class<?>) def.getInnerClass());
			mt.invoke(objCurrent, getValueFromStore(docCurrent, vecValues, def));
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
			throw new XPTRuntimeException("Error during processDomino2Java", e);
		}
	}

	@Override
	public Enum<?>[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Enum<?>[] enRC = new Enum<?>[2];
		try {
			enRC[0] = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			enRC[1] = getValue(objCurrent, def.getJavaField());
			docCurrent.replaceItemValue(def.getNotesField(), enRC[1].name());
		} catch (Exception ex) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", ex);
			throw new XPTRuntimeException("Error during processJava2Domino", ex);
		}

		return enRC;
	}

	@Override
	public Enum<?> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (Enum<?>) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Enum<?> getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		Enum<?> rc = null;
		if (vecValues.isEmpty()) {
			return rc;
		}
		try {
			String strValue = (String) vecValues.elementAt(0);
			Class cl = def.getInnerClass();
			rc = Enum.valueOf(cl, strValue);
		} catch (Exception e) {
			LoggerFactory.logWarning(this.getClass(), "getValueFromStore", e);
			throw new XPTRuntimeException("Error during getValueFrom Store", e);
		}
		return rc;
	}

}
