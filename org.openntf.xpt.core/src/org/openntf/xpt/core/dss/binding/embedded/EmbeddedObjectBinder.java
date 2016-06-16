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
package org.openntf.xpt.core.dss.binding.embedded;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.Java2DominoBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.StringUtil;

public class EmbeddedObjectBinder implements IBinder<Object> {

	private static final  EmbeddedObjectBinder m_Binder = new EmbeddedObjectBinder();

	private EmbeddedObjectBinder() {
	}

	public static EmbeddedObjectBinder getInstance() {
		return m_Binder;
	}

	@Override
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			System.out.println("Loading ..."+ def.getNotesField());
			String strClassStore = docCurrent.getItemValueString(def.getNotesField());
			if (StringUtil.isEmpty(strClassStore)) {
				LoggerFactory.logWarning(getClass(), "No Class Defined. Using Class: " + def.getInnerClass().getCanonicalName(), null);
			} else {
				if (!strClassStore.equals(def.getInnerClass().getCanonicalName())) {
					LoggerFactory.logWarning(getClass(), strClassStore + " expected. Effective Class: " + def.getInnerClass().getCanonicalName(), null);
				}
			}
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), (Class<?>) def.getInnerClass());
			mt.invoke(objCurrent, getValueFromStore(docCurrent, null, def));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Object[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		Object[] objRC = new Object[2];
		try {
			objRC[0] = getValueFromStore(docCurrent, null, def);
			objRC[1] = getValue(objCurrent, def.getJavaField());
			Java2DominoBinder j2d = def.getContainer().getSaver(def.getInnerClass());
			if (objRC[1] == null) {
				j2d.cleanFields(docCurrent);
				docCurrent.removeItem(def.getNotesField());
			} else {
				j2d.processDocument(docCurrent, objRC[1], null);
				docCurrent.replaceItemValue(def.getNotesField(), objRC[1].getClass().getCanonicalName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objRC;
	}

	@Override
	public Object getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		Object objSet = null;
		try {
			objSet = def.getInnerClass().newInstance();
			Domino2JavaBinder d2j = def.getContainer().getLoader(objSet.getClass());
			d2j.processDocument(docCurrent, objSet);
			System.out.println(objSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objSet;
	}

}
