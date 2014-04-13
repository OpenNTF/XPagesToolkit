package org.openntf.xpt.objectlist.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public enum ListProcessor {
	INSTANCE;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> process2List(Class<?> clListContainer, Object... objs) {
		List lstRC = null;
		if (clListContainer.isInterface() && objs.length == 1 && Arrays.asList(objs[0].getClass().getInterfaces()).contains(List.class)) {
			return (List)objs[0];
		}
		try {
			if (clListContainer.isInterface()) {
				lstRC = new ArrayList<String>();
			} else {
				Constructor<?> construcor = clListContainer.getConstructor();
				lstRC = (List) construcor.newInstance();

			}
			Collections.addAll(lstRC, objs);
		} catch (Exception ex) {
			LoggerFactory.logError(this.getClass(), "Error during conversion to List", ex);
			throw new XPTRuntimeException("Error during conversion to List", ex);
		}
		return lstRC;
	}
}
