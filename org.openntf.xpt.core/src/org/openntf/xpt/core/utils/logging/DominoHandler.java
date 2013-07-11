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
package org.openntf.xpt.core.utils.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.RichTextItem;
import lotus.domino.Session;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class DominoHandler extends Handler {
	private String m_CallerClass = "";
	private Level m_Level;

	public DominoHandler(String callerClass, Level nLevel) {
		super();
		m_CallerClass = callerClass;
		m_Level = nLevel;
	}

	@Override
	public void setLevel(Level newLevel) {
		m_Level = newLevel;
	}

	@Override
	public void close() {
	}

	@Override
	public void flush() {
	}

	@Override
	public void publish(LogRecord logEvent) {
		if (m_Level.intValue() > logEvent.getLevel().intValue()) {
			return;
		}
		try {
			Session sesCurrent = ExtLibUtil.getCurrentSessionAsSigner();
			Database ndbCurrent = sesCurrent.getCurrentDatabase();
			Document docLog = ndbCurrent.createDocument();
			docLog.replaceItemValue("form", "frmLog");
			docLog.replaceItemValue("sevn", logEvent.getLevel().intValue());
			docLog.replaceItemValue("SRCNAME", m_CallerClass);
			docLog.replaceItemValue("ERRORMSG", logEvent.getMessage());
			if (logEvent.getThrown() != null) {
				Throwable trCurrent = logEvent.getThrown();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				trCurrent.printStackTrace(pw);
				RichTextItem rtCurrent = docLog.createRichTextItem("BodyRT");
				rtCurrent.appendText(sw.toString());
			}
			docLog.save(true, false, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
