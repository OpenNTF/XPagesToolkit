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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;

public class ListDoubleBinder implements IBinder<List<Double>> {
	private static final ProfilerType pt = new ProfilerType("XPT.DSS.ListDoubleBinder");

	private static ListDoubleBinder m_Binder = new ListDoubleBinder();

	public static IBinder<List<Double>> getInstance() {
		return m_Binder;
	}

	@Override
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), List.class);
			List<Double> lstValues = null;
			if (Profiler.isEnabled()) {
				ProfilerAggregator pa = Profiler.startProfileBlock(pt, "getValueFormStore");
				long startTime = Profiler.getCurrentTime();
				try {
					lstValues = getValueFromStore(docCurrent, vecValues, def);
				} finally {
					Profiler.endProfileBlock(pa, startTime);
				}
			} else {
				lstValues = getValueFromStore(docCurrent, vecValues, def);
			}

			if (lstValues != null) {
				mt.invoke(objCurrent, lstValues);
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}

	}

	@Override
	public List<Double>[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		@SuppressWarnings("unchecked")
		List<Double>[] lstRC = new List[2];
		try {
			List<Double> lstOldValues = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			List<Double> lstValues = getValue(objCurrent, def.getJavaField());

			lstRC[0] = lstOldValues;
			lstRC[1] = lstValues;
			Vector<Double> vecValues = new Vector<Double>();
			vecValues.addAll(lstValues);
			Item notesItem =docCurrent.replaceItemValue(def.getNotesField(), vecValues);
			notesItem.setSummary(def.isNotesSummary());
			notesItem.recycle();

		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return lstRC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<Double>) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Double> getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		if (!vecValues.isEmpty()) {
			try {
				List<Double> lstValues = new ArrayList<Double>(vecValues.size());
				for (Object value : vecValues) {
					lstValues.add((Double) value);
				}
				return lstValues;
			} catch (Exception e) {

			}
		}
		return null;
	}

}
