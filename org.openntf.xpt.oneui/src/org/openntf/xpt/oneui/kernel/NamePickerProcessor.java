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
package org.openntf.xpt.oneui.kernel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.utils.DatabaseProvider;
import org.openntf.xpt.core.utils.logging.LoggerFactory;
import org.openntf.xpt.oneui.component.UINamePicker;

import com.ibm.commons.util.StringUtil;

public enum NamePickerProcessor implements INamePickerValueService {
	INSTANCE;

	/* (non-Javadoc)
	 * @see org.openntf.xpt.oneui.kernel.INamePickerValueService#getTypeAheaderNE(org.openntf.xpt.oneui.component.UINamePicker, java.lang.String)
	 */
	@Override
	public List<NameEntry> getTypeAheadValues(UINamePicker uiNp, String strSearch) throws NotesException {
		Database db = DatabaseProvider.INSTANCE.getDatabase(uiNp.getDatabase(), false);
		View vw = db.getView(uiNp.getView());
		DocumentCollection docCollection;
		String strFTSearch = uiNp.buildFTSearch(strSearch);
		if (db.isFTIndexed() && !StringUtil.isEmpty(strFTSearch)) {
			try {

				db.updateFTIndex(true);
				vw.FTSearch(strFTSearch);

				ViewEntryCollection vecEntries = vw.getAllEntries();
				ViewEntry entryNext = vecEntries.getFirstEntry();
				docCollection = vw.getAllDocumentsByKey("EMPTY_COLLECTION"); // Initalize
																				// empty
																				// Collection

				while (entryNext != null) {
					ViewEntry entry = entryNext;
					entryNext = vecEntries.getNextEntry(entry);
					docCollection.addDocument(entry.getDocument());
					entry.recycle();
				}
				vecEntries.recycle();
			} catch (Exception e) {
				LoggerFactory.logError(getClass(), "Error during ftSearch access", e);
				docCollection = vw.getAllDocumentsByKey(strSearch, false);
			}
		} else {
			docCollection = vw.getAllDocumentsByKey(strSearch, false);
		}
		List<NameEntry> lstNameEntries = new ArrayList<NameEntry>();
		Document docNext = docCollection.getFirstDocument();
		while (docNext != null) {
			Document docProcess = docNext;
			docNext = docCollection.getNextDocument();
			NameEntry nam = uiNp.getDocumentEntryRepresentation(docProcess);
			if (nam != null) {
				lstNameEntries.add(nam);
				nam.buildResultLineHL(strSearch);
			}
			docProcess.recycle();
		}
		Collections.sort(lstNameEntries, new Comparator<NameEntry>() {

			@Override
			public int compare(NameEntry o1, NameEntry o2) {
				return o1.getLabel().compareTo(o2.getLabel());
			}

		});
		return lstNameEntries;
	}

	/* (non-Javadoc)
	 * @see org.openntf.xpt.oneui.kernel.INamePickerValueService#getDislplayLabels(org.openntf.xpt.oneui.component.UINamePicker, java.lang.String[])
	 */
	@Override
	public Map<String, String> getDislplayLabels(UINamePicker uiNp, String[] values) {
		Map<String, String> hsRC = new HashMap<String, String>();
		try {
			Database db = DatabaseProvider.INSTANCE.getDatabase(uiNp.getDatabase(), false);
			buildDisplayLabelsFromDatabase(uiNp, values, hsRC, db);
			DatabaseProvider.INSTANCE.handleRecylce(db);
		} catch (Exception ex) {
			throw new XPTRuntimeException("getDisplayLables", ex);
		}
		return hsRC;
	}

	public void buildDisplayLabelsFromDatabase(UINamePicker uiNp, String[] values, Map<String, String> hsRC, Database db) throws NotesException {
		View vw = null;
		if (StringUtil.isEmpty(uiNp.getLookupView())) {
			vw = db.getView(uiNp.getView());
		} else {
			vw = db.getView(uiNp.getLookupView());

		}
		for (String strValue : values) {
			String strLabel = getLabel(vw, strValue, uiNp);
			hsRC.put(strValue, strLabel);
		}
		if (vw != null) {
			vw.recycle();
		}
	}
	
	private String getLabel(View vw, String strValue, UINamePicker uiNp) throws NotesException {
		String rc = strValue;
		if (vw != null) {
			Document docRC = vw.getDocumentByKey(strValue, true);
			if (docRC != null) {
				rc = uiNp.getDisplayLableValue(docRC);
				docRC.recycle();
			}
		}
		return rc;
	}
}
