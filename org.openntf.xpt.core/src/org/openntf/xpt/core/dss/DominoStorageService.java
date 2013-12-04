/*
 * © Copyright WebGate Consulting AG, 2012
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
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.dss.binding.DateBinder;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.DoubleArrayBinder;
import org.openntf.xpt.core.dss.binding.DoubleBinder;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.IntBinder;
import org.openntf.xpt.core.dss.binding.Java2DominoBinder;
import org.openntf.xpt.core.dss.binding.LongBinder;
import org.openntf.xpt.core.dss.binding.ObjectBinder;
import org.openntf.xpt.core.dss.binding.StringArrayBinder;
import org.openntf.xpt.core.dss.binding.StringBinder;
import org.openntf.xpt.core.utils.ServiceSupport;

import com.ibm.designer.runtime.Application;

public class DominoStorageService {

	private static final String ORG_OPENNTF_XPT_CORE_DSS_DOMINO_STORE = "org.openntf.xpt.core.dss.DominoStore";
	private static final String UNID_IDENTIFIER = "@UNID";
	private Hashtable<String, Domino2JavaBinder> m_Loader = new Hashtable<String, Domino2JavaBinder>();
	private Hashtable<String, Java2DominoBinder> m_Saver = new Hashtable<String, Java2DominoBinder>();
	private Hashtable<String, DominoStore> m_StoreDefinitions = new Hashtable<String, DominoStore>();

	private DominoStorageService() {

	}

	public static DominoStorageService getInstance() {
		DominoStorageService ds = (DominoStorageService) Application.get().getObject(ORG_OPENNTF_XPT_CORE_DSS_DOMINO_STORE);
		if (ds == null) {
			ds = new DominoStorageService();
			Application.get().putObject(ORG_OPENNTF_XPT_CORE_DSS_DOMINO_STORE, ds);
		}
		return ds;
	}

	public boolean saveObject(Object objCurrent, Database ndbTarget) throws DSSException {
		DominoStore dsDefinition = checkDominoStoreDefinition(objCurrent);
		Java2DominoBinder j2d = m_Saver.get(objCurrent.getClass().getCanonicalName());
		return saveObject2Document(dsDefinition, j2d, objCurrent, ndbTarget);
	}

	public boolean saveObjectWithDocument(Object objCurrent, Document docCurrent) throws DSSException {
		DominoStore dsDefinition = checkDominoStoreDefinition(objCurrent);

		Java2DominoBinder j2d = m_Saver.get(objCurrent.getClass().getCanonicalName());
		Object objID = getObjectID(dsDefinition, objCurrent);
		try {
			j2d.processDocument(docCurrent, objCurrent, "" + objID);
			docCurrent.save(true, false, true);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean getObject(Object objCurrent, Object primaryKey, Database ndbTarget) throws DSSException {
		DominoStore dsDefinition = checkDominoStoreDefinition(objCurrent);
		Domino2JavaBinder d2j = m_Loader.get(objCurrent.getClass().getCanonicalName());
		return getObjectFromDocument(dsDefinition, d2j, objCurrent, primaryKey, ndbTarget);

	}

	public boolean getObjectWithDocument(Object objCurrent, Document docCurrent) throws DSSException {
		DominoStore dsDefinition = checkDominoStoreDefinition(objCurrent);
		try {
			Domino2JavaBinder d2j = m_Loader.get(objCurrent.getClass().getCanonicalName());
			d2j.processDocument(docCurrent, objCurrent);
			if (UNID_IDENTIFIER.equals(dsDefinition.View())) {
				setUNID2PK(dsDefinition, objCurrent, docCurrent);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean deleteObject(Object objDelete, Database ndbTarget) throws DSSException {
		DominoStore dsDefinition = checkDominoStoreDefinition(objDelete);
		Object objPK = getObjectID(dsDefinition, objDelete);
		try {
			Document docDelete = getDocument(dsDefinition, objDelete, ndbTarget, false, objPK);
			if (docDelete == null) {
				return false;
			}
			docDelete.remove(true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public Object getObjectID(Object objCurrent) throws DSSException {
		DominoStore dsDefinition = checkDominoStoreDefinition(objCurrent);
		return getObjectID(dsDefinition, objCurrent);

	}

	private boolean getObjectFromDocument(DominoStore dsDefinition, Domino2JavaBinder d2j, Object objCurrent, Object pk, Database ndbTarget) {
		try {
			if (pk != null) {
				setPK2Object(dsDefinition, objCurrent, pk);
			}
			Document docCurrent = getDocument(dsDefinition, objCurrent, ndbTarget, false, pk);
			if (docCurrent == null) {
				return false;
			}
			d2j.processDocument(docCurrent, objCurrent);
			if (UNID_IDENTIFIER.equals(dsDefinition.View())) {
				setUNID2PK(dsDefinition, objCurrent, docCurrent);
			}
			docCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean saveObject2Document(DominoStore ds, Java2DominoBinder j2d, Object obj, Database ndbTarget) {
		try {
			Object objID = getObjectID(ds, obj);
			Document docCurrent = getDocument(ds, obj, ndbTarget, true, objID);
			j2d.processDocument(docCurrent, obj, "" + objID);
			docCurrent.save(true, false, true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Document getDocument(DominoStore ds, Object obj, Database ndbTarget, boolean createOnFail, Object objID) throws NotesException {
		Document docCurrent = null;

		if (objID != null && !"".equals(objID)) {
			if (UNID_IDENTIFIER.equals(ds.View())) {
				docCurrent = ndbTarget.getDocumentByUNID("" + objID);
			} else {
				View viwCurrent = ndbTarget.getView(ds.View());
				docCurrent = viwCurrent.getDocumentByKey(objID, true);
				viwCurrent.recycle();
			}
		}
		if (docCurrent == null && createOnFail) {
			docCurrent = ndbTarget.createDocument();
			docCurrent.replaceItemValue("Form", ds.Form());
			if (UNID_IDENTIFIER.equals(ds.View())) {
				setUNID2PK(ds, obj, docCurrent);
			}
		}
		return docCurrent;
	}

	private void setUNID2PK(DominoStore ds, Object obj, Document docCurrent) {
		try {
			Method mt = obj.getClass().getMethod("set" + ds.PrimaryKeyField(), String.class);
			mt.invoke(obj, docCurrent.getUniversalID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPK2Object(DominoStore ds, Object obj, Object pk) {
		try {
			Method mt = obj.getClass().getMethod("set" + ds.PrimaryKeyField(), ds.PrimaryFieldClass());
			mt.invoke(obj, pk);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void prepareBinders(Object objCurrent) {
		DominoStore dsCurrent = objCurrent.getClass().getAnnotation(DominoStore.class);
		Domino2JavaBinder d2j = buildLoadBinder(dsCurrent, objCurrent.getClass());
		Java2DominoBinder j2d = buildSaveBinder(dsCurrent, objCurrent.getClass());
		m_Loader.put(objCurrent.getClass().getCanonicalName(), d2j);
		m_Saver.put(objCurrent.getClass().getCanonicalName(), j2d);
		m_StoreDefinitions.put(objCurrent.getClass().getCanonicalName(), dsCurrent);
	}

	private Object getObjectID(DominoStore ds, Object obj) {
		try {
			if (ds.PrimaryFieldClass().equals(String.class)) {
				return StringBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Integer.class)) {
				return IntBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Double.class)) {
				return DoubleBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Double[].class)) {
				return DoubleArrayBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Double.class)) {
				return LongBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(Date.class)) {
				return DateBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
			if (ds.PrimaryFieldClass().equals(String[].class)) {
				return StringArrayBinder.getInstance().getValue(obj, ds.PrimaryKeyField());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Java2DominoBinder buildSaveBinder(DominoStore dsStore, Class<?> currentClass) {
		Java2DominoBinder j2dRC = new Java2DominoBinder();
		Collection<Field> lstFields = ServiceSupport.getClassFields(currentClass);
		for (Field fldCurrent : lstFields) {
			if (fldCurrent.isAnnotationPresent(DominoEntity.class)) {
				DominoEntity de = fldCurrent.getAnnotation(DominoEntity.class);
				if (!de.readOnly()) {
					if (de.encrypt()) {
						IBinder<?> binder = DefinitionFactory.getEncryptionBinder(fldCurrent.getType());
						if (binder != null) {
							HashMap<String, Object> addValues = new HashMap<String, Object>();
							if (de.encRoles() != null && de.encRoles().length > 0) {
								addValues.put("encRoles", de.encRoles());
							}
							j2dRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(dsStore, fldCurrent.getName()), binder, de.changeLog(), addValues, de.encrypt(), de.encRoles());
						}
					} else {
						IBinder<?> binder = DefinitionFactory.getBinder(fldCurrent.getType(), fldCurrent.getGenericType());
						if (binder != null) {
							HashMap<String, Object> addValues = new HashMap<String, Object>();
							if (de.isNames())
								addValues.put("isNames", true);
							if (de.isAuthor())
								addValues.put("isAuthor", true);
							if (de.isReader())
								addValues.put("isReader", true);
							if (de.dateOnly())
								addValues.put("dateOnly", true);
							if (de.showNameAs() != null)
								addValues.put("showNameAs", de.showNameAs().toString());
							if (binder.getClass().equals(ObjectBinder.class)) {
								addValues.put("innerClass", fldCurrent.getType());
								addValues.put("genericType", fldCurrent.getGenericType());
							}

							j2dRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(dsStore, fldCurrent.getName()), binder, de.changeLog(), addValues, de.encrypt(), de.encRoles());

						}
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

					if (de.encrypt()) {
						IBinder<?> binder = DefinitionFactory.getEncryptionBinder(fldCurrent.getType());
						if (binder != null) {
							HashMap<String, Object> addValues = new HashMap<String, Object>();
							if (de.encRoles() != null && de.encRoles().length > 0) {
								addValues.put("encRoles", de.encRoles());
							}
							djdRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(dsStore, fldCurrent.getName()), binder, de.changeLog(), addValues, de.encrypt(), de.encRoles());
						}
					} else if (de.isFormula()) {
						IBinder<?> binder = DefinitionFactory.getFormulaBinder(fldCurrent.getType());
						if (binder != null) {
							djdRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(dsStore, fldCurrent.getName()), binder, de.changeLog(), null, de.encrypt(), de.encRoles());

						}
					} else {
						IBinder<?> binder = DefinitionFactory.getBinder(fldCurrent.getType(), fldCurrent.getGenericType());
						HashMap<String, Object> addValues = new HashMap<String, Object>();
						if (de.isNames())
							addValues.put("isNames", true);
						if (de.isAuthor())
							addValues.put("isAuthor", true);
						if (de.isReader())
							addValues.put("isReader", true);
						if (de.dateOnly())
							addValues.put("dateOnly", true);
						if (de.showNameAs() != null)
							addValues.put("showNameAs", de.showNameAs().toString());
						if (binder.getClass().equals(ObjectBinder.class)) {
							addValues.put("innerClass", fldCurrent.getType());
							addValues.put("genericType", fldCurrent.getGenericType());
						}

						if (binder != null) {
							djdRC.addDefinition(de.FieldName(), ServiceSupport.buildCleanFieldNameCC(dsStore, fldCurrent.getName()), binder, de.changeLog(), addValues, de.encrypt(), de.encRoles());

						}
					}
				}
			}
		}
		return djdRC;
	}

	private DominoStore checkDominoStoreDefinition(Object objCurrent) throws DSSException {
		if (!objCurrent.getClass().isAnnotationPresent(DominoStore.class)) {
			throw new DSSException("No @DominoStore defined for " + objCurrent.getClass().getCanonicalName());
		}
		if (!m_StoreDefinitions.containsKey(objCurrent.getClass().getCanonicalName())) {
			prepareBinders(objCurrent);
		}
		DominoStore dsDefinition = m_StoreDefinitions.get(objCurrent.getClass().getCanonicalName());
		return dsDefinition;
	}

}
