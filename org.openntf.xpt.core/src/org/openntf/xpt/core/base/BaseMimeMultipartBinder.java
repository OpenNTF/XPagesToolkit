package org.openntf.xpt.core.base;

import java.lang.reflect.Method;

import com.ibm.xsp.http.MimeMultipart;

public class BaseMimeMultipartBinder {
	public BaseMimeMultipartBinder() {
		super();
	}

	public String getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			MimeMultipart mm = (MimeMultipart) mt.invoke(objCurrent);
			
			return mm.getHTML();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
