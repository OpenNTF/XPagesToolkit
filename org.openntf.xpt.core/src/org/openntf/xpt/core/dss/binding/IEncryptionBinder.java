package org.openntf.xpt.core.dss.binding;

import java.util.HashMap;
import java.util.List;

public interface IEncryptionBinder {

	public String[] getChangeLogValues(Object[] arrObject, HashMap<String, Object> additionalValues);
	boolean hasAccess(HashMap<String, Object> addValues, List<String> arrRoles);
}
