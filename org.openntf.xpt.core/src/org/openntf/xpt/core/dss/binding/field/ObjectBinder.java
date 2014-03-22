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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.MIMEHeader;
import lotus.domino.Session;
import lotus.domino.Stream;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.util.XPTObjectInputStream;

public class ObjectBinder implements IBinder<Object> {

	private static ObjectBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {
		try {

			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), (Class<?>) def.getInnerClass());
			mt.invoke(objCurrent, getRawValueFromStore(docCurrent, def.getNotesField()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Object[] objRC = new Object[2];
		try {
			Object oldBody = getRawValueFromStore(docCurrent, def.getNotesField());
			Object body = getValue(objCurrent, def.getJavaField());
			objRC[0] = oldBody;
			objRC[1] = body;
			Session session = docCurrent.getParentDatabase().getParent();

			boolean convertMime = session.isConvertMime();
			session.setConvertMime(false);

			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(new GZIPOutputStream(byteStream));
			objectStream.writeObject(body);
			objectStream.flush();
			objectStream.close();

			MIMEEntity entity = null;
			MIMEEntity previousState = docCurrent.getMIMEEntity(def.getNotesField());
			if (previousState == null) {
				entity = docCurrent.createMIMEEntity(def.getNotesField());
			} else {
				entity = previousState;
			}
			Stream mimeStream = session.createStream();
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteStream.toByteArray());
			mimeStream.setContents(byteIn);
			entity.setContentFromBytes(mimeStream, "application/x-java-serialized-object", MIMEEntity.ENC_NONE);

			MIMEHeader header = entity.getNthHeader("Content-Encoding");
			if (header == null) {
				header = entity.createHeader("Content-Encoding");
			}
			header.setHeaderVal("gzip");

			header.recycle();
			entity.recycle();
			mimeStream.recycle();

			session.setConvertMime(convertMime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objRC;

	}

	public static IBinder<Object> getInstance() {
		if (m_Binder == null) {
			m_Binder = new ObjectBinder();
		}
		return m_Binder;
	}

	private ObjectBinder() {

	}

	public Object getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Object getRawValueFromStore(Document docCurrent, String strNotesField) {
		try {
			Session session = docCurrent.getParentDatabase().getParent();
			boolean convertMime = session.isConvertMime();
			session.setConvertMime(false);

			Serializable result = null;
			Stream mimeStream = session.createStream();
			MIMEEntity entity = docCurrent.getMIMEEntity(strNotesField);
			entity.getContentAsBytes(mimeStream);

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			mimeStream.getContents(streamOut);
			mimeStream.recycle();

			byte[] stateBytes = streamOut.toByteArray();
			ByteArrayInputStream byteStream = new ByteArrayInputStream(stateBytes);
			XPTObjectInputStream objectStream;
			if (entity.getHeaders().toLowerCase().contains("content-encoding: gzip")) {
				GZIPInputStream zipStream = new GZIPInputStream(byteStream);
				objectStream = new XPTObjectInputStream(zipStream);
			} else {
				objectStream = new XPTObjectInputStream(byteStream);
			}
			final XPTObjectInputStream finOIS = objectStream;
			Serializable restored = AccessController.doPrivileged(new PrivilegedAction<Serializable>() {

				@Override
				public Serializable run() {
					try {

						return (Serializable) finOIS.readObject();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

			});
			finOIS.close();
			result = restored;

			entity.recycle();

			session.setConvertMime(convertMime);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Object getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		return null;
	}

}
