package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;

public class ListIntegerBinder implements IBinder<List<Integer>> {
	private static final ProfilerType pt = new ProfilerType("XPT.DSS.ListIntegerBinder");

	private static ListIntegerBinder m_Binder = new ListIntegerBinder();

	public static IBinder<List<Integer>> getInstance() {
		return m_Binder;
	}

	@Override
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), List.class);
			List<Integer> lstValues = null;
			if (Profiler.isEnabled()) {
				ProfilerAggregator pa = Profiler.startProfileBlock(pt, "getValueFormStore");
				long startTime = Profiler.getCurrentTime();
				try {
					lstValues = getValueFromStore(docCurrent, vecValues, def);
				} finally {
					Profiler.endProfileBlock(pa, startTime);
				}
			} else {
				lstValues = getValueFromStore(docCurrent, vecValues, def);
			}

			if (lstValues != null) {
				mt.invoke(objCurrent, lstValues);
			}
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processDomino2Java", e);
		}

	}

	@Override
	public List<Integer>[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		@SuppressWarnings("unchecked")
		List<Integer>[] lstRC = new List[2];
		try {
			List<Integer> lstOldValues = getValueFromStore(docCurrent, docCurrent.getItemValue(def.getNotesField()), def);
			List<Integer> lstValues = getValue(objCurrent, def.getJavaField());

			lstRC[0] = lstOldValues;
			lstRC[1] = lstValues;
			Vector<Integer> vecValues = new Vector<Integer>();
			vecValues.addAll(lstValues);
			docCurrent.replaceItemValue(def.getNotesField(), vecValues);
		} catch (Exception e) {
			LoggerFactory.logWarning(getClass(), "Error during processJava2Domino", e);
		}
		return lstRC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<Integer>) mt.invoke(objCurrent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		if (!vecValues.isEmpty()) {
			try {
				List<Integer> lstValues = new ArrayList<Integer>(vecValues.size());
				for (Object value : vecValues) {
					lstValues.add((Integer) value);
				}
				return lstValues;
			} catch (Exception e) {

			}
		}
		return null;
	}

}
