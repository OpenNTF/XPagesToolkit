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
package org.openntf.xpt.core.dss.binding.formula;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.IFormulaBinder;

public class FormulaDoubleBinder implements IBinder<Double>, IFormulaBinder {
	private static FormulaDoubleBinder m_Binder;

	private FormulaDoubleBinder() {

	}

	public static FormulaDoubleBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaDoubleBinder();
		}
		return m_Binder;
	}

	public Double getValue(Object objCurrent, String strJavaField) {
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Double.class);
			Vector<?> vecDouble = docCurrent.getParentDatabase().getParent().evaluate(def.getNotesField(), docCurrent);
			if (vecDouble.size() > 0) {
				Double dblCurrent = (Double) vecDouble.elementAt(0);
				mt.invoke(objCurrent, dblCurrent);
			}
		} catch (Exception e) {
		}
	}

	public Double[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		return null;
	}

	@Override
	public Double getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		// TODO Auto-generated method stub
		return null;
	}

}
