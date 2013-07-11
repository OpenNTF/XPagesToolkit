package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import lotus.domino.Document;

public class FormulaStringBinder implements IBinder<String>, IFormulaBinder {
	private static FormulaStringBinder m_Binder;

	private FormulaStringBinder() {

	}

	public static FormulaStringBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaStringBinder();
		}
		return m_Binder;
	}

	public String getValue(Object objCurrent, String strJavaField) {
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					String.class);
			Vector<?> vecString = docCurrent.getParentDatabase().getParent()
					.evaluate(strNotesField, docCurrent);
			if (vecString.size() > 0) {
				String strCurrent = (String) vecString.elementAt(0);
				mt.invoke(objCurrent, strCurrent);
			}
		} catch (Exception e) {
		}

	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String JavaField, HashMap<String, Object> addValues) {
	}

}
