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
package org.openntf.xpt.core.dss.binding.util;

import lotus.domino.Item;
import lotus.domino.Name;
import lotus.domino.Session;

import org.openntf.xpt.core.dss.binding.Definition;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;

public class NamesProcessor {
	private static final ProfilerType pt = new ProfilerType("XPT.DSS.NameProcessorr");
	private static NamesProcessor instance = null;

	private NamesProcessor() {
	}

	public static NamesProcessor getInstance() {
		if (instance == null) {
			instance = new NamesProcessor();
		}
		return instance;
	}

	public String getPerson(Definition def, String strValue, Session sesCurrent) {
		String strRC = strValue;
		if (Profiler.isEnabled()) {
			ProfilerAggregator pa = Profiler.startProfileBlock(pt, "getPerson");
			long startTime = Profiler.getCurrentTime();
			try {
				strRC = _getPerson(def, strValue, sesCurrent);
			} finally {
				Profiler.endProfileBlock(pa, startTime);
			}
		} else {
			strRC = _getPerson(def, strValue, sesCurrent);

		}
		return strRC;
	}

	public String _getPerson(Definition def, String strValue, Session sesCurrent) {
		String rcValue = strValue;
		if (def.isReader() || def.isAuthor() || def.isNames() && !StringUtil.isEmpty(def.getShowNameAs())) {
			try {
				Name nonCurrent = sesCurrent.createName(strValue);

				if ("ABBREVIATE".equalsIgnoreCase(def.getShowNameAs())) {
					rcValue = nonCurrent.getAbbreviated();
				} else if ("CN".equals(def.getShowNameAs())) {
					rcValue = nonCurrent.getCommon();
				}
				nonCurrent.recycle();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rcValue;
	}

	public String setPerson(String strValue, boolean isNamesValue, Session sesCurrent) {
		String rcValue = strValue;
		try {
			if (isNamesValue) {
				Name person = sesCurrent.createName(strValue);
				if (person != null) {
					rcValue = person.getCanonical();
					person.recycle();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rcValue;
	}

	public boolean setNamesField(Definition def, Item iNotesField) {
		boolean isNamesValue = false;
		try {
			if (iNotesField != null) {
				if (def.isReader()) {
					isNamesValue = true;
					iNotesField.setReaders(true);
				} else if (def.isAuthor()) {
					isNamesValue = true;
					iNotesField.setAuthors(true);
				} else if (def.isNames()) {
					isNamesValue = true;
					iNotesField.setNames(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNamesValue;
	}
}
