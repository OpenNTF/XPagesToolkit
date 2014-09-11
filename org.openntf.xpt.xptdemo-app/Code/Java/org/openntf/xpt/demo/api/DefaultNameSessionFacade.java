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
package org.openntf.xpt.demo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lotus.domino.NotesException;

import org.openntf.xpt.oneui.component.UINamePicker;
import org.openntf.xpt.oneui.kernel.INamePickerValueService;
import org.openntf.xpt.oneui.kernel.NameEntry;

public class DefaultNameSessionFacade implements INamePickerValueService {

	public HashMap<String, String> getDislplayLabels(UINamePicker arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<NameEntry> getTypeAheadValues(UINamePicker arg0, String arg1) throws NotesException {
		List<NameEntry> nameEntry = new ArrayList<NameEntry>();
		nameEntry.add(new NameEntry("James Adams/XPT/TEST", "james.adams@xpt.test", "James Adams (james.adams@xpt.test)") );
		nameEntry.add(new NameEntry("Peter Adams/XPT/TEST", "Peter.adams@xpt.test", "Peter Adams (peter.adams@xpt.test)") );
		nameEntry.add(new NameEntry("Frank Adams/XPT/TEST", "Frank.adams@xpt.test", "Franke Adams (frank.adams@xpt.test)") );
		return nameEntry;
	}
	
}