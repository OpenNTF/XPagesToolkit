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
package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Document;

import org.openntf.xpt.core.base.AbstractBaseBinder;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.util.DateProcessor;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public class DateBinder extends AbstractBaseBinder<Date> implements IBinder<Date> {

	private static DateBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), Date.class);
			Date dtCurrent = getValueFromStore(docCurrent, vecValues, def);
			if (dtCurrent != null) {
				mt.invoke(objCurrent, dtCurrent);
			}

		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public Date[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Date[] dtRC = new Date[2];
		try {
			Date dtCurrent = getValue(objCurrent, def.getJavaField());
			Date dtOld = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			dtRC[0] = dtOld;
			if (dtCurrent != null) {
				DateTime dt = docCurrent.getParentDatabase().getParent().createDateTime(dtCurrent);
				if (def.isDateOnly()) {
					dt = docCurrent.getParentDatabase().getParent().createDateTime(dt.getDateOnly());
				}
				docCurrent.replaceItemValue(def.getNotesField(), dt);
				Date dtCurrentNew = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
				dtRC[1] = dtCurrentNew;

			} else {
				docCurrent.removeItem(def.getNotesField());
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
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
	public Date getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) {
		if (!vecValues.isEmpty()) {
			try {
				DateTime dtCurrent = (DateTime) vecValues.elementAt(0);
				String strFormat = DateProcessor.getInstance().getDateFormat(def, docCurrent.getParentDatabase().getParent());
				DateFormat formatter = new SimpleDateFormat(strFormat);
				Date dtRC = formatter.parse(dtCurrent.getLocalTime());
				dtCurrent.recycle();
				return dtRC;
			} catch (Exception e) {
			}
		}
		return null;

	}

}
