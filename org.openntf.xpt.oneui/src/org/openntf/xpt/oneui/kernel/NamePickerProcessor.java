package org.openntf.xpt.oneui.kernel;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

import org.openntf.xpt.oneui.component.UINamePicker;

public enum NamePickerProcessor {
	INSTANCE;
	
	public StringBuilder getTypeAhead(UINamePicker uiNp, String strSearch) {

		String strAddFunction = "";
		StringBuilder b = new StringBuilder();
		b.append("<ul><li><span class='informal'>Suggestions:</span></li>"); // $NON-NLS-1$
		try {
			Database db = DatabaseProvider.INSTANCE.getDatabase(uiNp.getDatabase());
			View vw = db.getView(uiNp.getView());
			DocumentCollection docCollection;
			if (db.isFTIndexed()) {
				try {

					db.updateFTIndex(true);
					vw.FTSearch(strSearch);

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
				} catch (Exception e) {
					e.printStackTrace();
					docCollection = vw.getAllDocumentsByKey(strSearch, false);
				}
			}else{
				docCollection = vw.getAllDocumentsByKey(strSearch, false);
			}

			Document curDoc = docCollection.getFirstDocument();
			while (curDoc != null) {
				Document nextDoc = docCollection.getNextDocument();
				String name = curDoc.getItemValueString("Form");
				String email = curDoc.getItemValueString("Form");

				int start = name.toLowerCase().indexOf(strSearch.toLowerCase());
				int stop = start + 3 + strSearch.length();

				StringBuffer sb = new StringBuffer(name);
				sb.insert(start, "<b>");
				sb.insert(stop, "</b>");

				b.append("<li><a onclick=\"" + strAddFunction + "();\"><p>" + sb + "<span class=\"informal\">" + email + "</span></p></a></li>");
				curDoc = nextDoc;
				nextDoc.recycle();
			}

			b.append("</ul>"); // $NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(result);
		return b;
	}
}
