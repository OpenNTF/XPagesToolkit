/*
 * © Copyright WebGate Consulting AG, 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.xpt.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.json.annotations.JSONObject;

/**
 * @author Christian Guedemann
 *
 */
/**
 * @author Christian Guedemann
 *
 */
/**
 * @author Christian Guedemann
 * 
 */
public class ServiceSupport {

	public static String buildCleanFieldNameCC(DominoStore dsCurrent, String strFieldName) {
		return makeCamelCase(buildCleanFieldName(dsCurrent, strFieldName));
	}

	public static String buildCleanFieldName(DominoStore dsCurrent, String strFieldName) {
		return internalBuildCleanFieldName(dsCurrent.JavaFieldPrefix(), dsCurrent.JavaFieldPostFix(), strFieldName);
	}

	public static String buildCleanFieldNameCC(JSONObject jsCurrent, String strFieldName) {
		return makeCamelCase(buildCleanFieldName(jsCurrent, strFieldName));
	}

	public static String buildCleanFieldName(JSONObject jsCurrent, String strFieldName) {
		return internalBuildCleanFieldName(jsCurrent.JavaFieldPrefix(), jsCurrent.JavaFieldPostFix(), strFieldName);
	}

	private static String internalBuildCleanFieldName(String strPrefix, String strPostFix, String fldName) {
		String fldNameClean = fldName;
		if (strPrefix.length() > 0) {
			fldNameClean = fldNameClean.substring(strPrefix.length());
		}
		if (strPostFix.length() > 0) {
			fldNameClean = fldNameClean.substring(0, (fldNameClean.length() - strPostFix.length()));
		}
		return fldNameClean;
	}

	public static String makeGetter(String strFieldClean) {
		return "get" + makeCamelCase(strFieldClean);
	}

	public static String makeBooleanGetter(String strFieldClean) {
		return "is" + makeCamelCase(strFieldClean);
	}

	public static String makeSetter(String strFieldClean) {
		return "set" + makeCamelCase(strFieldClean);
	}

	public static String makeGetter(DominoStore dsCurrent, String strField) {
		String strFieldClean = buildCleanFieldName(dsCurrent, strField);
		return makeGetter(strFieldClean);
	}

	public static String makeGetter(JSONObject dsCurrent, String strField) {
		String strFieldClean = buildCleanFieldName(dsCurrent, strField);
		return makeGetter(strFieldClean);
	}

	public static String makeSetter(DominoStore dsCurrent, String strField) {
		String strFieldClean = buildCleanFieldName(dsCurrent, strField);
		return makeSetter(strFieldClean);
	}

	public static String makeSetter(JSONObject dsCurrent, String strField) {
		String strFieldClean = buildCleanFieldName(dsCurrent, strField);
		return makeSetter(strFieldClean);
	}

	public static String makeCamelCase(String strCurrent) {
		return strCurrent.substring(0, 1).toUpperCase() + strCurrent.substring(1);
	}

	/**
	 * Returns the getter Method form objectClass. Uses "get" + strFieldName and
	 * if it fails uses is +...
	 * 
	 * @param objectClass
	 * @param strFieldClean
	 * @return GetterMethod or throws a {@link NoSuchMethodException}
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getGetterMethod(Class<?> objectClass, String strFieldClean) throws SecurityException, NoSuchMethodException {
		String strMethod = makeGetter(strFieldClean);
		try {
			return objectClass.getMethod(strMethod);
		} catch (NoSuchMethodException nomp) {
			strMethod = makeBooleanGetter(strFieldClean);
			return objectClass.getMethod(strMethod);
		}
	}

	public static Collection<Field> getClassFields(final Class<?> currentClass) {
		Collection<Field> lstFields = AccessController.doPrivileged(new PrivilegedAction<Collection<Field>>() {

			@Override
			public Collection<Field> run() {
				Set<Field> setFields = new TreeSet<Field>(new Comparator<Field>() {
					@Override
					public int compare(Field o1, Field o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				getAllFieldsRec(currentClass, setFields);
				return setFields;
			}

		});
		return lstFields;
	}

	private static Set<Field> getAllFieldsRec(Class<?> clazz, Set<Field> setFields) {
		Class<?> superClazz = clazz.getSuperclass();
		List<Field> decFields = Arrays.asList(clazz.getDeclaredFields());
		for (Field fldCheck : decFields) {
			if (!fldCheck.isSynthetic()) {
				setFields.add(fldCheck);
			}
		}
		if (superClazz != null) {
			getAllFieldsRec(superClazz, setFields);
		}
		return setFields;
	}

}
