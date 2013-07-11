package org.openntf.xpt.core.json.binding;

import org.openntf.xpt.core.base.BaseMimeMultipartBinder;
import org.openntf.xpt.core.utils.JSONSupport;

import com.ibm.domino.services.util.JsonWriter;

public class MimeMultipartBinder extends BaseMimeMultipartBinder implements IJSONBinder<String> {
	private static MimeMultipartBinder m_Binder;

	private MimeMultipartBinder() {

	}

	public static MimeMultipartBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new MimeMultipartBinder();
		}
		return m_Binder;
	}
	
	public void process2JSON(JsonWriter jsWriter, Object objCurrent, String strJSONProperty, String strJAVAField, boolean showEmptyValue,
			Class<?> containerClass) {
		try {
			String strValue = getValue(objCurrent, strJAVAField);
			JSONSupport.writeString(jsWriter, strJSONProperty, strValue, showEmptyValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processValue2JSON(JsonWriter jsWriter, Object value) {
		try {
			jsWriter.outStringLiteral((String) value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}