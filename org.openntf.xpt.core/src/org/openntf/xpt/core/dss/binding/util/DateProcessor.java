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
package org.openntf.xpt.core.dss.binding.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Session;

import org.openntf.xpt.core.dss.binding.Definition;

public class DateProcessor {
	private static DateProcessor instance = null;
	private String m_DateFormat;
	private String m_DateFormatDateOnly;

	private DateProcessor() {
	}

	public static DateProcessor getInstance() {
		if (instance == null) {
			instance = new DateProcessor();
		}
		return instance;
	}

	public String getDateFormat(Definition def, Session sesCurrent) {

		try {
			if (m_DateFormat == null || m_DateFormatDateOnly == null) {
				String strDateSep = sesCurrent.getInternational().getDateSep();
				String strTimeSep = sesCurrent.getInternational().getTimeSep();
				String strHour = sesCurrent.getInternational().isTime24Hour() ? "" : " a";

				if (sesCurrent.getInternational().isDateDMY()) {
					m_DateFormatDateOnly = "dd" + strDateSep + "MM" + strDateSep + "yy";
					m_DateFormat = m_DateFormatDateOnly + " HH" + strTimeSep + "mm" + strTimeSep + "ss" + strHour;
				} else if (sesCurrent.getInternational().isDateMDY()) {
					m_DateFormatDateOnly = "MM" + strDateSep + "dd" + strDateSep + "yy";
					m_DateFormat = m_DateFormatDateOnly + " HH" + strTimeSep + "mm" + strTimeSep + "ss" + strHour;
				} else if (sesCurrent.getInternational().isDateYMD()) {
					m_DateFormatDateOnly = "yyyy" + strDateSep + "MM" + strDateSep + "dd";
					m_DateFormat = m_DateFormatDateOnly + " HH" + strTimeSep + "mm" + strTimeSep + "ss" + strHour;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (def.isDateOnly()) {
			return m_DateFormatDateOnly;
		} else {
			return m_DateFormat;
		}
	}

	@Deprecated
	public String getFormattedDateWithFormulaDDMMYYYYHHMMSS(String strNotesField, Document docCurrent, Session sesCurrent) {
		String strDate = "";
		try {
			Vector<?> strDay = sesCurrent.evaluate("@Right(\"00\" + @Text(@Day(" + strNotesField + "));2)" + "+ \".\" + @Right(\"00\" + @Text(@Month(" + strNotesField + "));2)"
					+ "+ \".\" + @Right(\"0000\" + @Text(@Year(" + strNotesField + "));4)" + "+ \" \" + @Right(\"00\" + @Text(@Hour(" + strNotesField + "));2)"
					+ "+ \":\" + @Right(\"00\" + @Text(@Minute(" + strNotesField + "));2)" + "+ \":\" + @Right(\"00\" + @Text(@Second(" + strNotesField + "));2)", docCurrent);
			strDate = (String) strDay.elementAt(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	public String getDateAsStringToEncrypt(Date dtCurrent, boolean dtOnly) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dtCurrent);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String strDay = "00" + Integer.toString(day);
		strDay = strDay.substring(strDay.length() - 2, strDay.length());
		int month = cal.get(Calendar.MONTH) + 1;
		String strMonth = "00" + Integer.toString(month);
		strMonth = strMonth.substring(strMonth.length() - 2, strMonth.length());
		int year = cal.get(Calendar.YEAR);
		String strYear = "0000" + Integer.toString(year);
		strYear = strYear.substring(strYear.length() - 4, strYear.length());
		int hour = cal.get(Calendar.HOUR);
		String strHour = "00" + Integer.toString(hour);
		strHour = strHour.substring(strHour.length() - 2, strHour.length());
		int min = cal.get(Calendar.MINUTE);
		String strMin = "00" + Integer.toString(min);
		strMin = strMin.substring(strMin.length() - 2, strMin.length());
		int sek = cal.get(Calendar.SECOND);
		String strSek = "00" + Integer.toString(sek);
		strSek = strSek.substring(strSek.length() - 2, strSek.length());

		if (dtOnly) {
			return strDay + "." + strMonth + "." + strYear;
		} else {
			return strDay + "." + strMonth + "." + strYear + " " + strSek + ":" + strMin + ":" + strHour;
		}
	}

	public String getDateFormatForEncryption(boolean dtOnly) {
		if (dtOnly) {
			return "dd.MM.yyyy";
		}
		return "dd.MM.yyyy ss:mm:HH";

	}
}
