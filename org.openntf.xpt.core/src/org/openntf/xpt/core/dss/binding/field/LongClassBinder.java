/*
 * © Copyright WebGate Consulting AG, 2012
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

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class LongClassBinder implements IBinder<Long> {

	private static LongClassBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Long.class);
			Long nValue = getValueFromStore(docCurrent, vecCurrent, def);
			if (nValue != null) {
				mt.invoke(objCurrent, nValue.longValue());
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public Long[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Long[] lngRC = new Long[2];
		try {
			Long nOldValue = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			Long nValue = getValue(objCurrent, def.getJavaField());
			lngRC[0] = nOldValue;
			lngRC[1] = nValue;
			docCurrent.replaceItemValue(def.getNotesField(), nValue);
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return lngRC;

	}

	public static IBinder<Long> getInstance() {
		if (m_Binder == null) {
			m_Binder = new LongClassBinder();
		}
		return m_Binder;
	}

	private LongClassBinder() {

	}

	public Long getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (Long) mt.invoke(objCurrent);
		} catch (Exception ex) {

		}
		return null;
	}

	@Override
	public Long getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		if (!vecCurrent.isEmpty()) {
			Double dblCurrent = (Double) vecCurrent.get(0);
			return Long.valueOf(dblCurrent.longValue());
		}
		return null;
	}
}
