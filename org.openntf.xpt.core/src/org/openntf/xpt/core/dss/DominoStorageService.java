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

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.dss.annotations.XPTPresentationControl;
import org.openntf.xpt.core.dss.binding.BinderContainer;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.Java2DominoBinder;
import org.openntf.xpt.core.dss.binding.field.DateBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleArrayBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleBinder;
import org.openntf.xpt.core.dss.binding.field.IntBinder;
import org.openntf.xpt.core.dss.binding.field.LongBinder;
import org.openntf.xpt.core.dss.binding.field.StringArrayBinder;
import org.openntf.xpt.core.dss.binding.field.StringBinder;
import org.openntf.xpt.core.dss.changeLog.ChangeLogEntry;
import org.openntf.xpt.core.utils.ServiceSupport;

import com.ibm.designer.runtime.Application;

public class DominoStorageService {

	private static final String ORG_OPENNTF_XPT_CORE_DSS_DOMINO_STORE = "org.openntf.xpt.core.dss.DominoStore";
	private static final String UNID_IDENTIFIER = "@UNID";
	private final BinderContainer m_BinderContainer = new BinderContainer("");

	private DominoStorageService() {

	}

	public static synchronized DominoStorageService getInstance() {
		DominoStorageService ds = (DominoStorageService) Application.get().getObject(ORG_OPENNTF_XPT_CORE_DSS_DOMINO_STORE);
		if (ds == null) {
			ds = new DominoStorageService();
			Application.get().putObject(ORG_OPENNTF_XPT_CORE_DSS_DOMINO_STORE, ds);
		}
		return ds;
	}

	public boolean saveObject(Object objCurrent, Database ndbTarget) throws DSSException {
		DominoStore dsDefinition = m_BinderContainer.getStoreDefinitions(objCurrent.getClass());
		Java2DominoBinder j2d = m_BinderContainer.getSaver(objCurrent.getClass());
		return saveObject2Document(dsDefinition, j2d, objCurrent, ndbTarget);
	}

	public boolean saveObjectWithDocument(Object objCurrent, Document docCurrent) throws DSSException {
		DominoStore dsDefinition = m_BinderContainer.getStoreDefinitions(objCurrent.getClass());

		Java2DominoBinder j2d = m_BinderContainer.getSaver(objCurrent.getClass());
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
		Domino2JavaBinder d2j = m_BinderContainer.getLoader(objCurrent.getClass());
		return getObjectFromDocument(m_BinderContainer.getStoreDefinitions(objCurrent.getClass()), d2j, objCurrent, primaryKey, ndbTarget);

	}

	public boolean getObjectWithDocument(Object objCurrent, Document docCurrent) throws DSSException {
		DominoStore dsDefinition = m_BinderContainer.getStoreDefinitions(objCurrent.getClass());
		try {
			Domino2JavaBinder d2j = m_BinderContainer.getLoader(objCurrent.getClass());
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
		DominoStore dsDefinition = m_BinderContainer.getStoreDefinitions(objDelete.getClass());
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
		DominoStore dsDefinition = m_BinderContainer.getStoreDefinitions(objCurrent.getClass());
		return getObjectID(dsDefinition, objCurrent);

	}

	public boolean isFieldAccessable(Object objCurrent, String strFieldName, List<String> currentRoles) throws DSSException {
		Java2DominoBinder d2j = m_BinderContainer.getSaver(objCurrent.getClass());
		return d2j.isFieldAccessable(strFieldName, currentRoles);

	}

	public boolean isFieldAccessable(Object objCurrent, ChangeLogEntry cl, List<String> currentRoles) throws DSSException {
		Java2DominoBinder d2j = m_BinderContainer.getSaver(objCurrent.getClass());
		return d2j.isFieldAccessable(cl.getObjectField(), currentRoles, cl);

	}

	public XPTPresentationControl getXPTPresentationControl(Object obj, String elField) throws DSSException {
		return m_BinderContainer.getXPTPresentationControl(obj.getClass(), elField);
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

	private Object getObjectID(DominoStore ds, Object obj) {
		try {
			String strPK = ServiceSupport.makeCamelCase(ds.PrimaryKeyField());
			if (ds.PrimaryFieldClass().equals(String.class)) {
				return StringBinder.getInstance().getValue(obj, strPK);
			}
			if (ds.PrimaryFieldClass().equals(Integer.class)) {
				return IntBinder.getInstance().getValue(obj, strPK);
			}
			if (ds.PrimaryFieldClass().equals(Double.class)) {
				return DoubleBinder.getInstance().getValue(obj, strPK);
			}
			if (ds.PrimaryFieldClass().equals(Double[].class)) {
				return DoubleArrayBinder.getInstance().getValue(obj, strPK);
			}
			if (ds.PrimaryFieldClass().equals(Double.class)) {
				return LongBinder.getInstance().getValue(obj, strPK);
			}
			if (ds.PrimaryFieldClass().equals(Date.class)) {
				return DateBinder.getInstance().getValue(obj, strPK);
			}
			if (ds.PrimaryFieldClass().equals(String[].class)) {
				return StringArrayBinder.getInstance().getValue(obj, strPK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
