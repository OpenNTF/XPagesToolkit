package org.openntf.xpt.core.dss.binding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import lotus.domino.Document;

public class FormulaDoubleBinder implements IBinder<Double>, IFormulaBinder {
	private static FormulaDoubleBinder m_Binder;

	private FormulaDoubleBinder() {

	}

	public static FormulaDoubleBinder getInstance() {
		if (m_Binder == null) {
			m_Binder = new FormulaDoubleBinder();
		}
		return m_Binder;
	}

	public Double getValue(Object objCurrent, String strJavaField) {
		return null;
	}

	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField,
					Double.class);
			Vector<?> vecDouble = docCurrent.getParentDatabase().getParent()
					.evaluate(strNotesField, docCurrent);
			if (vecDouble.size() > 0) {
				Double dblCurrent = (Double) vecDouble.elementAt(0);
				mt.invoke(objCurrent, dblCurrent);
			}
		} catch (Exception e) {
		}
	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String JavaField, HashMap<String, Object> addValues) {
	}

}
