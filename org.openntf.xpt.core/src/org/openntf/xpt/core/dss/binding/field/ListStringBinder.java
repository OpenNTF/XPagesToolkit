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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.util.NamesProcessor;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;

public class ListStringBinder implements IBinder<List<String>> {
	private static final ProfilerType pt = new ProfilerType("XPT.DSS.ListStringBinder");
	private static ListStringBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), List.class);
			List<String> lstValues = null;
			if (Profiler.isEnabled()) {
				ProfilerAggregator pa = Profiler.startProfileBlock(pt, "getValueFormStore");
				long startTime = Profiler.getCurrentTime();
				try {
					lstValues = getValueFromStore(docCurrent, vecCurrent, def);
				} finally {
					Profiler.endProfileBlock(pa, startTime);
				}
			} else {
				lstValues = getValueFromStore(docCurrent, vecCurrent, def);
			}

			if (lstValues != null) {
				mt.invoke(objCurrent, lstValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String>[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		@SuppressWarnings("unchecked")
		List<String>[] lstRC = new List[2];
		try {
			List<String> lstOldValues = getRawValueFromStore(docCurrent, def.getNotesField());
			List<String> lstValues = getValue(objCurrent, def.getJavaField());

			lstRC[0] = lstOldValues;
			lstRC[1] = lstValues;
			Vector<String> vValues = new Vector<String>();

			if (lstValues != null) {
				boolean isNamesValue = false;
				if (def.isAuthor() || def.isReader() || def.isNames()) {
					// Changed to a one call
					Item iNotesField = docCurrent.replaceItemValue(def.getNotesField(), "");
					// Item iNotesField =
					// docCurrent.getFirstItem(strNotesField);
					isNamesValue = NamesProcessor.getInstance().setNamesField(def, iNotesField);
				}

				for (String strValue : lstValues) {
					vValues.add(NamesProcessor.getInstance().setPerson(strValue, isNamesValue, docCurrent.getParentDatabase().getParent()));
				}
				lstRC[1] = new ArrayList<String>(vValues);

			}
			docCurrent.replaceItemValue(def.getNotesField(), vValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;
	}

	public static IBinder<List<String>> getInstance() {
		if (m_Binder == null) {
			m_Binder = new ListStringBinder();
		}
		return m_Binder;
	}

	private ListStringBinder() {

	}

	@SuppressWarnings("unchecked")
	public List<String> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<String>) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		if (!vecCurrent.isEmpty()) {
			try {
				List<String> lstValues = new ArrayList<String>(vecCurrent.size());
				for (Object strValue : vecCurrent) {
					lstValues.add(NamesProcessor.getInstance().getPerson(def, (String) strValue, docCurrent.getParentDatabase().getParent()));
				}
				return lstValues;
			} catch (Exception e) {

			}
		}
		return null;
	}

	public List<String> getRawValueFromStore(Document docCurrent, String strNotesField) {
		try {
			Vector<?> vecResult = docCurrent.getItemValue(strNotesField);
			ArrayList<String> lstValues = new ArrayList<String>(vecResult.size());
			for (Object strValue : vecResult) {
				lstValues.add(strValue.toString());
			}
			return lstValues;
		} catch (Exception e) {
		}
		return null;

	}

}
