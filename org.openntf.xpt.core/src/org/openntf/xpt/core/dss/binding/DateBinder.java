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

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, Date.class);
			Date dtCurrent = getValueFromStore(docCurrent, strNotesField, addValues);
			if (dtCurrent != null) {
				mt.invoke(objCurrent, dtCurrent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date[] processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		Date[] dtRC = new Date[2];
		try {
			Date dtCurrent = getValue(objCurrent, strJavaField);
			Date dtOld = getValueFromStore(docCurrent, strNotesField, addValues);
			dtRC[0] = dtOld;
			if (dtCurrent != null) {
				DateTime dt = docCurrent.getParentDatabase().getParent().createDateTime(dtCurrent);
				if (addValues.containsKey("dateOnly")) {
					dt = docCurrent.getParentDatabase().getParent().createDateTime(dt.getDateOnly());
				}
				docCurrent.replaceItemValue(strNotesField, dt);
				Date dtCurrentNew =getValueFromStore(docCurrent, strNotesField, addValues);
				dtRC[1] = dtCurrentNew;

			} else {
				docCurrent.removeItem(strNotesField);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtRC;

	}

	public static IBinder<Date> getInstance() {
		if (m_Binder == null) {
			m_Binder = new DateBinder();
		}
		return m_Binder;
	}

	private DateBinder() {

	}

	@Override
	public Date getValueFromStore(Document docCurrent, String strNotesField, HashMap<String, Object> additionalValues) {
		Vector<?> vecDates = null;
		try {
			try {
				vecDates = docCurrent.getItemValueDateTimeArray(strNotesField);
			} catch (Exception e) {
			  e.printStackTrace();
			}
			if (vecDates != null && vecDates.size() > 0) {
				DateTime dtCurrent = (DateTime) vecDates.elementAt(0);

				String strFormat = DateProcessor.getInstance().getDateFormat(additionalValues, docCurrent.getParentDatabase().getParent());
				DateFormat formatter = new SimpleDateFormat(strFormat);
				return (Date) formatter.parse(dtCurrent.getLocalTime());
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return null;
	}

}
