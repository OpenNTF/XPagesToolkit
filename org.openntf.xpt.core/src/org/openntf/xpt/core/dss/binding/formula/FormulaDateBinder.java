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
package org.openntf.xpt.core.dss.binding.formula;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Document;

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.IFormulaBinder;

public class FormulaDateBinder implements IBinder<Date>, IFormulaBinder {

	private static FormulaDateBinder m_Binder;

	private FormulaDateBinder() {

	}

	public static FormulaDateBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaDateBinder();
		}
		return m_Binder;
	}

	public Date getValue(Object objCurrent, String strJavaField) {
		// TODO Auto-generated method stub
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		// Im notes Field ist die Formel drin (nicht so toll aber es wird tun)
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Date.class);
			Vector<?> vecDates = docCurrent.getParentDatabase().getParent().evaluate(def.getNotesField(), docCurrent);
			if (vecDates.size() > 0) {
				DateTime dtCurrent = (DateTime) vecDates.elementAt(0);
				mt.invoke(objCurrent, dtCurrent.toJavaDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Date getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		// TODO Auto-generated method stub
		return null;
	}

}
