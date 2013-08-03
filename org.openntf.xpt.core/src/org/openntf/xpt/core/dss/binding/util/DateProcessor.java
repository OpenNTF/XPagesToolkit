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
package org.openntf.xpt.core.dss.binding.util;

import java.util.HashMap;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Session;

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

	public String getDateFormat(HashMap<String, Object> addValues,
			Session sesCurrent) {

		try {
			if (m_DateFormat == null || m_DateFormatDateOnly == null) {
				String strDateSep = sesCurrent.getInternational().getDateSep();
				String strTimeSep = sesCurrent.getInternational().getTimeSep();
				String strHour = sesCurrent.getInternational().isTime24Hour() ? "":" a";
				
				if (sesCurrent.getInternational().isDateDMY()) {
					m_DateFormatDateOnly = "dd" + strDateSep + "MM"
							+ strDateSep + "yy";
					m_DateFormat = m_DateFormatDateOnly + " hh" + strTimeSep
							+ "mm" + strTimeSep + "ss" + strHour;
				} else if (sesCurrent.getInternational().isDateMDY()) {
					m_DateFormatDateOnly = "MM" + strDateSep + "dd"
							+ strDateSep + "yy";
					m_DateFormat = m_DateFormatDateOnly + " hh" + strTimeSep
							+ "mm" + strTimeSep + "ss" + strHour;
				} else if (sesCurrent.getInternational().isDateYMD()) {
					m_DateFormatDateOnly = "yyyy" + strDateSep + "MM"
							+ strDateSep + "dd";
					m_DateFormat = m_DateFormatDateOnly + " hh" + strTimeSep
							+ "mm" + strTimeSep + "ss" + strHour;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (addValues.containsKey("dateOnly")) {
			return m_DateFormatDateOnly;
		} else {
			return m_DateFormat;
		}
	}

	@Deprecated
	public String getFormattedDateWithFormulaDDMMYYYYHHMMSS(
			String strNotesField, Document docCurrent, Session sesCurrent) {
		String strDate = "";
		try {
			Vector<?> strDay = sesCurrent.evaluate(
					"@Right(\"00\" + @Text(@Day(" + strNotesField + "));2)"
							+ "+ \".\" + @Right(\"00\" + @Text(@Month("
							+ strNotesField + "));2)"
							+ "+ \".\" + @Right(\"0000\" + @Text(@Year("
							+ strNotesField + "));4)"
							+ "+ \" \" + @Right(\"00\" + @Text(@Hour("
							+ strNotesField + "));2)"
							+ "+ \":\" + @Right(\"00\" + @Text(@Minute("
							+ strNotesField + "));2)"
							+ "+ \":\" + @Right(\"00\" + @Text(@Second("
							+ strNotesField + "));2)", docCurrent);
			strDate = (String) strDay.elementAt(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
}
