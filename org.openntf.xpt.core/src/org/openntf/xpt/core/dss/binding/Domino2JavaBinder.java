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
package org.openntf.xpt.core.dss.binding;

import java.util.ArrayList;
import java.util.Vector;

import org.openntf.xpt.core.dss.binding.field.DominoRichTextItemBinder;
import org.openntf.xpt.core.dss.binding.field.MimeMultipartBinder;
import org.openntf.xpt.core.dss.binding.field.ObjectBinder;
import org.openntf.xpt.core.dss.binding.files.FileDownloadBinder;

import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;

public class Domino2JavaBinder {

	private ArrayList<Definition> m_Definition;
	private static final ProfilerType pt = new ProfilerType("XPT.DSS.Domino2JavaBinder");

	public Domino2JavaBinder() {
		m_Definition = new ArrayList<Definition>();
	}

	public void addDefinition(Definition def) {
		m_Definition.add(def);
	}

	public void processDocument(Document docProcess, Object objCurrent) throws NotesException {
		if (Profiler.isEnabled()) {
			ProfilerAggregator pa = Profiler.startProfileBlock(pt, "processDocument");
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
				ProfilerAggregator pa = Profiler.startProfileBlock(pt, "processDefinition - " + defCurrent.getBinder().getClass().getCanonicalName());
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
		if (defCurrent.getBinder() instanceof DominoRichTextItemBinder) {
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
		if (!vecValues.isEmpty() ) {
			defCurrent.getBinder().processDomino2Java(docProcess, objCurrent, vecValues, defCurrent);
		}
	}
}
