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

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Document;
import org.openntf.xpt.core.base.BaseDateBinder;
import org.openntf.xpt.core.dss.binding.util.DateProcessor;

public class DateBinder extends BaseDateBinder implements IBinder<Date> {

	private static DateBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Date.class);
			Vector<?> vecDates = null;
			try {
				vecDates = docCurrent.getItemValueDateTimeArray(strNotesField);
			} catch (Exception e) {
				// NIX machen
			}
			if (vecDates != null && vecDates.size() > 0) {
				DateTime dtCurrent = (DateTime) vecDates.elementAt(0);

				String strFormat = DateProcessor.getInstance().getDateFormat(addValues, docCurrent.getParentDatabase().getParent());

				// System.out.println("FORMAT = " + strFormat);
				DateFormat formatter = new SimpleDateFormat(strFormat);
				mt.invoke(objCurrent, (Date) formatter.parse(dtCurrent.getLocalTime()));
				// mt.invoke(objCurrent, dtCurrent.toJavaDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			Date dtCurrent = getValue(objCurrent, strJavaField);
			if (dtCurrent != null) {
				DateTime dt = docCurrent.getParentDatabase().getParent().createDateTime(dtCurrent);
				if (addValues.containsKey("dateOnly")) {
					dt = docCurrent.getParentDatabase().getParent().createDateTime(dt.getDateOnly());
				}
				docCurrent.replaceItemValue(strNotesField, dt);

			} else {
				docCurrent.removeItem(strNotesField);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static IBinder<Date> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DateBinder();
		}
		return m_Binder;
	}

	private DateBinder() {

	}

}
