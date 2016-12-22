/**
 * Copyright 2013, WebGate Consulting AG
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
package org.openntf.xpt.core.dss;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.embedded.EmbeddedListObjectBinder;
import org.openntf.xpt.core.dss.binding.embedded.EmbeddedObjectBinder;
import org.openntf.xpt.core.dss.binding.encryption.EncryptionDateBinder;
import org.openntf.xpt.core.dss.binding.encryption.EncryptionDoubleBinder;
import org.openntf.xpt.core.dss.binding.encryption.EncryptionStringBinder;
import org.openntf.xpt.core.dss.binding.field.BooleanBinder;
import org.openntf.xpt.core.dss.binding.field.BooleanClassBinder;
import org.openntf.xpt.core.dss.binding.field.DateBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleArrayBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleClassBinder;
import org.openntf.xpt.core.dss.binding.field.ENumBinder;
import org.openntf.xpt.core.dss.binding.field.IntBinder;
import org.openntf.xpt.core.dss.binding.field.IntClassBinder;
import org.openntf.xpt.core.dss.binding.field.ListDoubleBinder;
import org.openntf.xpt.core.dss.binding.field.ListIntegerBinder;
import org.openntf.xpt.core.dss.binding.field.ListStringBinder;
import org.openntf.xpt.core.dss.binding.field.LongBinder;
import org.openntf.xpt.core.dss.binding.field.LongClassBinder;
import org.openntf.xpt.core.dss.binding.field.MimeMultipartBinder;
import org.openntf.xpt.core.dss.binding.field.ObjectBinder;
import org.openntf.xpt.core.dss.binding.field.StringArrayBinder;
import org.openntf.xpt.core.dss.binding.field.StringBinder;
import org.openntf.xpt.core.dss.binding.files.FileDownloadBinder;
import org.openntf.xpt.core.dss.binding.files.FileUploadBinder;
import org.openntf.xpt.core.dss.binding.formula.FormulaDateBinder;
import org.openntf.xpt.core.dss.binding.formula.FormulaDoubleBinder;
import org.openntf.xpt.core.dss.binding.formula.FormulaStringBinder;
import org.openntf.xpt.core.dss.binding.util.FileHelper;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.http.MimeMultipart;

public class BinderFactory {

	private static final Map<Class<?>, IBinder<?>> BINDER_MAP = new HashMap<Class<?>, IBinder<?>>() {
		{
			put(Boolean.class, BooleanClassBinder.getInstance());
			put(Boolean.TYPE, BooleanBinder.getInstance());
			put(String.class, StringBinder.getInstance());
			put(String[].class, StringArrayBinder.getInstance());
			put(Integer.class, IntClassBinder.getInstance());
			put(Integer.TYPE, IntBinder.getInstance());
			put(Double.class, DoubleClassBinder.getInstance());
			put(Double.TYPE, DoubleBinder.getInstance());
			put(Long.class, LongClassBinder.getInstance());
			put(Long.TYPE, LongBinder.getInstance());
			put(Double[].class, DoubleArrayBinder.getInstance());
			put(Date.class, DateBinder.getInstance());
			put(MimeMultipart.class, MimeMultipartBinder.getInstance());
			put(UploadedFile.class, FileUploadBinder.getInstance());
			put(FileHelper.class, FileDownloadBinder.getInstance());

		}
	};

	public static IBinder<?> getBinder(Class<?> clCurrent, Type gtCurrent) {
		if (clCurrent.equals(List.class)) {
			return getAccurateListBinder(gtCurrent);
		}

		if (BINDER_MAP.containsKey(clCurrent)) {
			return BINDER_MAP.get(clCurrent);
		}
		if (clCurrent.isEnum()) {
			return ENumBinder.getInstance();
		}

		if (clCurrent instanceof Object) {
			return ObjectBinder.getInstance();
		}
		return null;
	}

	private static IBinder<?> getAccurateListBinder(Type gtCurrent) {
		if (gtCurrent instanceof ParameterizedType) {
			Type[] genericTypes = ((ParameterizedType) gtCurrent).getActualTypeArguments();
			for (Type genericType : genericTypes) {
				if (String.class.equals(genericType)) {
					return ListStringBinder.getInstance();
				}
				if (Double.class.equals(genericType)) {
					return ListDoubleBinder.getInstance();
				}
				if (Integer.class.equals(genericType)) {
					return ListIntegerBinder.getInstance();
				}
				if (FileHelper.class.equals(genericType)) {
					return FileDownloadBinder.getInstance();
				}
				if (UploadedFile.class.equals(genericType)) {
					return FileUploadBinder.getInstance();
				}
			}
		}
		return ObjectBinder.getInstance();
	}

	public static IBinder<?> getFormulaBinder(Class<?> clCurrent) {
		if (clCurrent.equals(String.class)) {
			return FormulaStringBinder.getInstance();
		}
		if (clCurrent.equals(Double.class) || clCurrent.equals(Double.TYPE)) {
			return FormulaDoubleBinder.getInstance();
		}
		if (clCurrent.equals(Date.class)) {
			return FormulaDateBinder.getInstance();
		}
		return null;
	}

	public static IBinder<?> getEncryptionBinder(Class<?> clCurrent) {
		if (clCurrent.equals(String.class)) {
			return EncryptionStringBinder.getInstance();
		}
		if (clCurrent.equals(Date.class)) {
			return EncryptionDateBinder.getInstance();
		}
		if (clCurrent.equals(Double.class) || clCurrent.equals(Double.TYPE)) {
			return EncryptionDoubleBinder.getInstance();
		}
		return null;
	}

	public static IBinder<?> getEmbededBinder(Class<?> clCurrent) {
		if (clCurrent.equals(List.class)) {
			return EmbeddedListObjectBinder.getInstance();
		}
		return EmbeddedObjectBinder.getInstance();
	}

	public static Definition getDefinition(DominoStore dsStore, DominoEntity de, Field fldCurrent, String prefix, boolean isRead) {
		if (de.embedded()) {
			IBinder<?> binder = BinderFactory.getEmbededBinder(fldCurrent.getType());
			if (binder != null) {
				String endPrefix = StringUtil.isEmpty(prefix) ? de.FieldName() : prefix + "_" + de.FieldName();
				return Definition.buildDefinition4EO(dsStore, de, binder, fldCurrent, endPrefix);
			}
		}
		if (de.encrypt()) {
			IBinder<?> binder = BinderFactory.getEncryptionBinder(fldCurrent.getType());
			if (binder != null) {
				if (StringUtil.isEmpty(prefix)) {
					return Definition.buildDefiniton(dsStore, de, binder, fldCurrent);
				} else {
					return Definition.buildDefinition4EO(dsStore, de, binder, fldCurrent, prefix);
				}
			}
		} else if (de.isFormula() && isRead) {
			IBinder<?> binder = BinderFactory.getFormulaBinder(fldCurrent.getType());
			if (binder != null) {
				return Definition.buildDefiniton(dsStore, de, binder, fldCurrent);
			}
		} else {
			IBinder<?> binder = BinderFactory.getBinder(fldCurrent.getType(), fldCurrent.getGenericType());
			if (binder != null) {
				if (StringUtil.isEmpty(prefix)) {
					return Definition.buildDefiniton(dsStore, de, binder, fldCurrent);
				} else {
					return Definition.buildDefinition4EO(dsStore, de, binder, fldCurrent, prefix);
				}
			}
		}
		return null;
	}

}
