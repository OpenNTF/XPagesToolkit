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

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class DoubleArrayBinder implements IBinder<Double[]> {

	private static DoubleArrayBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), new Class[] { Double[].class });
			Double[] nValue = getValueFromStore(docCurrent, vecCurrent, def);
			if (nValue != null) {
				mt.invoke(objCurrent, new Object[] { nValue });
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public Double[][] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Double[][] dblRC = new Double[2][];
		try {
			Double[] nOldValues = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			Double[] nValues = getValue(objCurrent, def.getJavaField());
			dblRC[0] = nOldValues;
			dblRC[1] = nValues;
			Vector<Double> vecValues = new Vector<Double>(nValues.length);
			for (Double nVal : nValues) {
				vecValues.addElement(nVal);
			}
			docCurrent.replaceItemValue(def.getNotesField(), vecValues);
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return dblRC;
	}

	public static IBinder<Double[]> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DoubleArrayBinder();
		}
		return m_Binder;
	}

	private DoubleArrayBinder() {

	}

	public Double[] getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (Double[]) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Double[] getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		if (!vecCurrent.isEmpty()) {
			try {
				return (Double[]) vecCurrent.toArray(new Double[vecCurrent.size()]);
			} catch (Exception e) {
			}
		}
		return null;
	}

}
