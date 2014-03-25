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

import org.openntf.xpt.core.base.BaseDoubleBinder;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class DoubleClassBinder extends BaseDoubleBinder implements IBinder<Double> {

	private static DoubleClassBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Double.class);
			Double dblVal = getValueFromStore(docCurrent, vecCurrent, def);
			if (dblVal != null) {
				mt.invoke(objCurrent, dblVal.doubleValue());
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public Double[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Double[] dblRC = new Double[2];
		try {
			Double nOldValue = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			Double nValue = getValue(objCurrent, def.getJavaField());
			dblRC[0] = nOldValue;
			dblRC[1] = nValue;
			docCurrent.replaceItemValue(def.getNotesField(), nValue);
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return dblRC;
	}

	public static IBinder<Double> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DoubleClassBinder();
		}
		return m_Binder;
	}

	private DoubleClassBinder() {

	}

	@Override
	public Double getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		if (!vecCurrent.isEmpty()) {
			return (Double) vecCurrent.get(0);
		}
		return null;
	}
}
