/*
 * © Copyright WebGate Consulting AG, 2014
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
package org.openntf.xpt.core.dss.binding;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Hashtable;

import org.openntf.xpt.core.dss.BinderFactory;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.utils.ServiceSupport;

public class BinderContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Hashtable<String, Domino2JavaBinder> m_Loader = new Hashtable<String, Domino2JavaBinder>();
	private final Hashtable<String, Java2DominoBinder> m_Saver = new Hashtable<String, Java2DominoBinder>();
	private final Hashtable<String, DominoStore> m_StoreDefinitions = new Hashtable<String, DominoStore>();

	private final String m_Prefix;

	public BinderContainer(String prefix) {
		super();
		m_Prefix = prefix;
	}

	public Domino2JavaBinder getLoader(Class<?> cl) throws DSSException {
		checkDominoStoreDefinition(cl);
		return m_Loader.get(cl.getCanonicalName());
	}

	public Java2DominoBinder getSaver(Class<?> cl) throws DSSException {
		checkDominoStoreDefinition(cl);
		return m_Saver.get(cl.getCanonicalName());
	}

	public DominoStore getStoreDefinitions(Class<?> cl) throws DSSException {
		checkDominoStoreDefinition(cl);
		return m_StoreDefinitions.get(cl.getCanonicalName());
	}

	private void prepareBinders(Class<?> cl) {
		DominoStore dsCurrent = cl.getAnnotation(DominoStore.class);
		Domino2JavaBinder d2j = buildLoadBinder(dsCurrent, cl);
		Java2DominoBinder j2d = buildSaveBinder(dsCurrent, cl);
		m_Loader.put(cl.getCanonicalName(), d2j);
		m_Saver.put(cl.getCanonicalName(), j2d);
		m_StoreDefinitions.put(cl.getCanonicalName(), dsCurrent);
	}

	private Java2DominoBinder buildSaveBinder(DominoStore dsStore, Class<?> currentClass) {
		Java2DominoBinder j2dRC = new Java2DominoBinder();
		Collection<Field> lstFields = ServiceSupport.getClassFields(currentClass);
		for (Field fldCurrent : lstFields) {
			if (fldCurrent.isAnnotationPresent(DominoEntity.class)) {
				DominoEntity de = fldCurrent.getAnnotation(DominoEntity.class);
				if (!de.readOnly()) {
					Definition def = BinderFactory.getDefinition(dsStore, de, fldCurrent, m_Prefix, false);
					if (def != null) {
						j2dRC.addDefinition(def);
					}
				}
			}
		}
		return j2dRC;
	}

	private Domino2JavaBinder buildLoadBinder(DominoStore dsStore, Class<?> currentClass) {
		Domino2JavaBinder djdRC = new Domino2JavaBinder();
		Collection<Field> lstFields = ServiceSupport.getClassFields(currentClass);
		for (Field fldCurrent : lstFields) {
			if (fldCurrent.isAnnotationPresent(DominoEntity.class)) {
				DominoEntity de = fldCurrent.getAnnotation(DominoEntity.class);
				if (!de.writeOnly()) {
					Definition def = BinderFactory.getDefinition(dsStore, de, fldCurrent, m_Prefix, true);
					if (def != null) {
						djdRC.addDefinition(def);
					}
				}
			}
		}
		return djdRC;
	}

	private DominoStore checkDominoStoreDefinition(Class<?> cl) throws DSSException {
		if (!cl.isAnnotationPresent(DominoStore.class)) {
			throw new DSSException("No @DominoStore defined for " + cl.getCanonicalName());
		}
		if (!m_StoreDefinitions.containsKey(cl.getCanonicalName())) {
			prepareBinders(cl);
		}
		DominoStore dsDefinition = m_StoreDefinitions.get(cl.getCanonicalName());
		return dsDefinition;
	}
}
