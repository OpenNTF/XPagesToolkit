package org.openntf.xpt.core.dss.binding.embedded;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.Java2DominoBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.StringUtil;

public class EmbeddedListObjectBinder implements IBinder<List<Object>> {

	private final static EmbeddedListObjectBinder m_Binder = new EmbeddedListObjectBinder();

	public static EmbeddedListObjectBinder getInstance() {
		return m_Binder;
	}

	private EmbeddedListObjectBinder() {
	}

	@Override
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			String strClassStore = docCurrent.getItemValueString(def.getNotesField());
			if (StringUtil.isEmpty(strClassStore)) {
				return;
			}
			if (!strClassStore.equals(def.getInnerClass().getCanonicalName())) {
				LoggerFactory.logWarning(getClass(), strClassStore + " expected. Effective Class: " + def.getInnerClass().getCanonicalName(), null);
			}
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), (Class<?>) def.getInnerClass());
			mt.invoke(objCurrent, getValueFromStore(docCurrent, null, def));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object>[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		List<Object>[] objRC = new List[2];
		try {
			objRC[0] = getValueFromStore(docCurrent, null, def);
			objRC[1] = getValue(objCurrent, def.getJavaField());
			int nSize = docCurrent.getItemValueInteger(def.getNotesField() + "_SIZE");
			Java2DominoBinder j2d = def.getContainer().getSaver(getInnerClass(def.getGenericType()));
			if (objRC[1] == null) {
				j2d.cleanFields(docCurrent, nSize);
				docCurrent.removeItem(def.getNotesField());
			} else {
				j2d.processList2Document(docCurrent, objRC[1], nSize);
				docCurrent.replaceItemValue(def.getNotesField(), getInnerClass(def.getGenericType()).getCanonicalName());
				docCurrent.replaceItemValue(def.getNotesField()+"_SIZE", objRC[1].size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objRC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<Object>) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		List<Object> lstObjects = null;
		try {
			int nSize = docCurrent.getItemValueInteger(def.getNotesField() + "_SIZE");
			Domino2JavaBinder d2j = def.getContainer().getLoader(getInnerClass(def.getGenericType()));
			lstObjects = d2j.processDocument2List(docCurrent, getInnerClass(def.getGenericType()), nSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstObjects;
	}
	
	private static Class<?> getInnerClass(Type gen) {
		Class<?> clInner = null;
		if (gen instanceof ParameterizedType) {
			Type[] genericTypes = ((ParameterizedType) gen).getActualTypeArguments();
			if (genericTypes.length > 0) {
				clInner = (Class<?>) genericTypes[0];
			}
		}
		return clInner;
	}


}
