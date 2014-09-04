package org.openntf.xpt.oneui.kernel;

import java.util.HashMap;
import java.util.List;

import lotus.domino.NotesException;

import org.openntf.xpt.oneui.component.UINamePicker;

public interface INamePickerValueService {

	public abstract List<NameEntry> getTypeAheadValues(UINamePicker uiNp, String strSearch) throws NotesException;

	public abstract HashMap<String, String> getDislplayLabels(UINamePicker uiNp, String[] values);

}