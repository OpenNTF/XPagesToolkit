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
package org.openntf.xpt.core.dss.binding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.openntf.xpt.core.dss.binding.field.MimeMultipartBinder;
import org.openntf.xpt.core.dss.binding.field.ObjectBinder;
import org.openntf.xpt.core.dss.binding.files.FileDownloadBinder;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;

public class Domino2JavaBinder {

	private List<Definition> m_Definition;
	private static final ProfilerType PROFILERTYPE = new ProfilerType("XPT.DSS.Domino2JavaBinder");

	public Domino2JavaBinder() {
		m_Definition = new ArrayList<Definition>();
	}

	public void addDefinition(Definition def) {
		m_Definition.add(def);
	}

	public void processDocument(Document docProcess, Object objCurrent) throws NotesException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator pa = Profiler.startProfileBlock(PROFILERTYPE, "processDocument");
			long startTime = Profiler.getCurrentTime();
			try {
				_processDocument(docProcess, objCurrent);
			} finally {
				Profiler.endProfileBlock(pa, startTime);
			}
		} else {
			_processDocument(docProcess, objCurrent);
		}

	}

	private void _processDocument(Document docProcess, Object objCurrent) throws NotesException {
		for (Definition defCurrent : m_Definition) {
			if (Profiler.isEnabled()) {
				ProfilerAggregator pa = Profiler.startProfileBlock(PROFILERTYPE, "processDefinition - " + defCurrent.getBinder().getClass().getCanonicalName());
				long startTime = Profiler.getCurrentTime();
				try {
					_processDefinition(docProcess, objCurrent, defCurrent);
				} finally {
					Profiler.endProfileBlock(pa, startTime);
				}
			} else {
				_processDefinition(docProcess, objCurrent, defCurrent);
			}
		}
	}

	private void _processDefinition(Document docProcess, Object objCurrent, Definition defCurrent) throws NotesException {
		if (defCurrent.getBinder() instanceof IFormulaBinder) {
			defCurrent.getBinder().processDomino2Java(docProcess, objCurrent, null, defCurrent);
			return;
		}
		if (defCurrent.getBinder() instanceof MimeMultipartBinder) {
			defCurrent.getBinder().processDomino2Java(docProcess, objCurrent, null, defCurrent);
			return;
		}
		if (defCurrent.getBinder() instanceof FileDownloadBinder) {
			defCurrent.getBinder().processDomino2Java(docProcess, objCurrent, null, defCurrent);
			return;
		}
		if (defCurrent.getBinder() instanceof ObjectBinder) {
			defCurrent.getBinder().processDomino2Java(docProcess, objCurrent, null, defCurrent);
			return;
		}
		Vector<?> vecValues = docProcess.getItemValue(defCurrent.getNotesField());
		if (!vecValues.isEmpty()) {
			defCurrent.getBinder().processDomino2Java(docProcess, objCurrent, vecValues, defCurrent);
		}
	}

	public List<Object> processDocument2List(Document docProcess, Class<?> cl, int nSize) throws Exception {
		List<Object> lstRC = new LinkedList<Object>();
		for (int nCounter = 0; nCounter < nSize; nCounter++) {
			Object obj = cl.newInstance();
			for (Definition def : m_Definition) {
				Definition defProcess = Definition.cloneDefinition(def, nCounter);
				_processDefinition(docProcess, obj, defProcess);
			}
			lstRC.add(obj);
		}
		return lstRC;
	}

	public List<Definition> getDefinitions() {
		return m_Definition;
	}

}
