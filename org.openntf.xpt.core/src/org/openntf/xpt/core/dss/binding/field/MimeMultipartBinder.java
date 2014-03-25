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
package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.RichTextItem;
import lotus.domino.Stream;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.http.MimeMultipart;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.model.domino.wrapped.DominoRichTextItem;

public class MimeMultipartBinder implements IBinder<MimeMultipart> {

	private static MimeMultipartBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), MimeMultipart.class);
			mt.invoke(objCurrent, getRawValueFromStore(docCurrent, def.getNotesField()));
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}
	}

	public MimeMultipart[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		MimeMultipart[] mpRC = new MimeMultipart[2];
		try {

			MimeMultipart oldBody = getRawValueFromStore(docCurrent, def.getNotesField());
			MimeMultipart body = getValue(objCurrent, def.getJavaField());
			mpRC[0] = oldBody;
			mpRC[1] = body;
			Stream stream = docCurrent.getParentDatabase().getParent().createStream();
			if (body != null)
				stream.writeText(body.getHTML());
			else
				return null;

			MIMEEntity entity = docCurrent.getMIMEEntity(def.getNotesField());
			if (entity == null) {
				docCurrent.removeItem(def.getNotesField());
				entity = docCurrent.createMIMEEntity(def.getNotesField());
			}
			entity.setContentFromText(stream, "text/html;charset=UTF-8", 1725);
			stream.close();

		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return mpRC;

	}

	public static IBinder<MimeMultipart> getInstance() {
		if (m_Binder == null) {
			m_Binder = new MimeMultipartBinder();
		}
		return m_Binder;
	}

	private MimeMultipartBinder() {

	}

	public MimeMultipart getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (MimeMultipart) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public MimeMultipart getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		return null;
	}

	public MimeMultipart getRawValueFromStore(Document docCurrent, String strNotesField) {
		try {
			MimeMultipart mimeValue = null;
			MIMEEntity entity = docCurrent.getMIMEEntity(strNotesField);
			if (entity != null) {
				mimeValue = MimeMultipart.fromHTML(entity.getContentAsText());

			} else if (docCurrent.hasItem(strNotesField)) {

				if (docCurrent.getFirstItem(strNotesField) != null) {
					if (docCurrent.getFirstItem(strNotesField).getType() != 1) {
						mimeValue = MimeMultipart.fromHTML(docCurrent.getItemValueString(strNotesField));
					} else {
						RichTextItem rti = (RichTextItem) docCurrent.getFirstItem(strNotesField);
						if (rti != null) {
							DominoDocument dd = new DominoDocument();
							dd.setDocument(docCurrent);
							DominoRichTextItem drtCurrent = new DominoRichTextItem(dd, rti);
							mimeValue = MimeMultipart.fromHTML(drtCurrent.getHTML());
						}
					}
				}
			}

			return mimeValue;
		} catch (Exception e) {

		}
		return null;
	}

}
