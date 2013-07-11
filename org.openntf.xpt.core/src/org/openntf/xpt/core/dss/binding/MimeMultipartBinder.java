package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.util.HashMap;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.Stream;

import com.ibm.xsp.http.MimeMultipart;

public class MimeMultipartBinder implements IBinder<MimeMultipart> {

	private static MimeMultipartBinder m_Binder;

	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			MimeMultipart strValue = null;
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					MimeMultipart.class);

			MIMEEntity entity = docCurrent.getMIMEEntity(strNotesField);
			if (entity != null) {
				strValue = MimeMultipart.fromHTML(entity.getContentAsText());
			}
			mt.invoke(objCurrent, strValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			
			MimeMultipart body = getValue(objCurrent, strJavaField);
			
			Stream stream = docCurrent.getParentDatabase().getParent().createStream();
			if(body != null)
				stream.writeText(body.getHTML());
			else
				return;
			
			MIMEEntity entity = docCurrent.getMIMEEntity(strNotesField);
			if(entity == null){
				docCurrent.removeItem(strNotesField);
				entity = docCurrent.createMIMEEntity(strNotesField);
			}
			entity.setContentFromText(stream,"text/html;charset=UTF-8", 1725);
			stream.close();
			//docCurrent.replaceItemValue(strNotesField, strValue);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static IBinder<MimeMultipart> getInstance() {
		if (m_Binder == null) {
			m_Binder = new MimeMultipartBinder();
		}
		return m_Binder;
	}

	private MimeMultipartBinder(){
		
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

}
