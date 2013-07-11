package org.openntf.xpt.core.dss.binding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.MIMEHeader;
import lotus.domino.Session;
import lotus.domino.Stream;

import org.openntf.xpt.core.dss.binding.util.XPTObjectInputStream;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ObjectBinder implements IBinder<Object> {

	private static ObjectBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Session session = ExtLibUtil.getCurrentSession();
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
			result = restored;

			entity.recycle();

			session.setConvertMime(convertMime);

			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, (Class<?>) addValues.get("innerClass"));

			mt.invoke(objCurrent, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {

			Object body = getValue(objCurrent, strJavaField);
			Session session = ExtLibUtil.getCurrentSession();

			boolean convertMime = session.isConvertMime();
			session.setConvertMime(false);

			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(new GZIPOutputStream(byteStream));
			objectStream.writeObject(body);
			objectStream.flush();
			objectStream.close();

			MIMEEntity entity = null;
			MIMEEntity previousState = docCurrent.getMIMEEntity(strNotesField);
			if (previousState == null) {
				entity = docCurrent.createMIMEEntity(strNotesField);
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

}
