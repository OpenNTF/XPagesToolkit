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

/*
 * Special Thank to Matthias Cullmann and Sven Haufe for the inspiration of this class
 */

package org.openntf.xpt.core.dss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.View;

import org.apache.commons.collections.CollectionUtils;
import org.openntf.xpt.core.dss.changeLog.ChangeLogEntry;
import org.openntf.xpt.core.dss.changeLog.ChangeLogService;
import org.openntf.xpt.core.utils.RoleAndGroupProvider;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public abstract class AbstractStorageService<T> {

	protected AbstractStorageService() {
	}

	public boolean save(T obj) {
		return saveTo(obj, ExtLibUtil.getCurrentDatabase());
	}

	public boolean saveTo(T obj, Database ndbTarget) {
		try {
			return DominoStorageService.getInstance().saveObject(obj, ndbTarget);
		} catch (DSSException e) {
			e.printStackTrace();
		}
		return false;
	}

	public T getById(String strID) {
		return getByIdFrom(strID, ExtLibUtil.getCurrentDatabase());
	}

	public T getByIdFrom(String id, Database ndbSource) {
		try {
			T ret = createObject();
			if (!DominoStorageService.getInstance().getObject(ret, id, ndbSource)) {
				return null;
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<T> getAll(String viewID) {
		return getAllFrom(viewID, ExtLibUtil.getCurrentDatabase());
	}

	public List<T> getAllFrom(String viewId, Database ndbSource) {
		List<T> ret = new ArrayList<T>();
		try {
			View viwDabases = ndbSource.getView(viewId);
			Document docNext = viwDabases.getFirstDocument();
			while (docNext != null) {
				Document docCurrent = docNext;
				docNext = viwDabases.getNextDocument(docNext);
				T obj = createObject();
				if (DominoStorageService.getInstance().getObjectWithDocument(obj, docCurrent)) {
					ret.add(obj);
				}
				docCurrent.recycle();

			}
			viwDabases.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<T> getObjectsByForeignId(String foreignId, String viewID) {
		return getObjectsByForeignIdFrom(foreignId, viewID, ExtLibUtil.getCurrentDatabase());
	}

	public List<T> getObjectsByForeignIdFrom(String foreignId, String viewId, Database ndbSource) {
		List<T> ret = new ArrayList<T>();
		try {
			View viwDabases = ndbSource.getView(viewId);
			DocumentCollection documents = viwDabases.getAllDocumentsByKey(foreignId, true);
			Document docNext = documents.getFirstDocument();
			while (docNext != null) {
				Document docCurrent = docNext;
				docNext = documents.getNextDocument(docNext);

				T obj = createObject();
				if (DominoStorageService.getInstance().getObjectWithDocument(obj, docCurrent)) {
					ret.add(obj);
				}
				docCurrent.recycle();

			}
			viwDabases.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<T> getAllMyObjects(String strViewID, List<String> lstFieldsToCheck) {
		return getAllMyObjectsFrom(strViewID, lstFieldsToCheck, ExtLibUtil.getCurrentDatabase());
	}

	public List<T> getAllMyObjectsFrom(String strViewID, List<String> lstFieldsToCheck, Database ndbSource) {
		List<T> ret = new ArrayList<T>();
		List<String> lstRolesGroups = RoleAndGroupProvider.getInstance().getMyGroupsAndRoles();
		try {
			View viwDabases = ndbSource.getView(strViewID);
			Document docNext = viwDabases.getFirstDocument();
			while (docNext != null) {
				Document docCurrent = docNext;
				docNext = viwDabases.getNextDocument(docNext);
				if (isDocumentOfInterest(docCurrent, lstRolesGroups, lstFieldsToCheck)) {
					T obj = createObject();
					if (DominoStorageService.getInstance().getObjectWithDocument(obj, docCurrent)) {
						ret.add(obj);
					}
				}
				docCurrent.recycle();

			}
			viwDabases.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}

	public List<T> getAllObjectFor(String strUser, String strViewID, List<String> lstFieldsToCheck) {
		return getAllObjectsForFrom(strUser, strViewID, lstFieldsToCheck, ExtLibUtil.getCurrentDatabase());
	}

	public List<T> getAllObjectsForFrom(String strUser, String strViewID, List<String> lstFieldsToCheck, Database ndbSource) {
		List<T> ret = new ArrayList<T>();
		List<String> lstRolesGroups = RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(strUser, ndbSource);
		try {
			View viwDabases = ndbSource.getView(strViewID);
			Document docNext = viwDabases.getFirstDocument();
			while (docNext != null) {
				Document docCurrent = docNext;
				docNext = viwDabases.getNextDocument(docNext);
				if (isDocumentOfInterest(docCurrent, lstRolesGroups, lstFieldsToCheck)) {
					T obj = createObject();
					if (DominoStorageService.getInstance().getObjectWithDocument(obj, docCurrent)) {
						ret.add(obj);
					}
				}
				docCurrent.recycle();

			}
			viwDabases.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}

	public ISoftDeletionProvider<T> getSoftDeletionProvider() {
		return null;
	}

	public boolean softDelete(T objDelete) throws DSSException {
		return softDelete(objDelete, ExtLibUtil.getCurrentDatabase());
	}

	public boolean softDelete(T objDelete, Database ndbTarget) throws DSSException {

		ISoftDeletionProvider<T> sdProv = getSoftDeletionProvider();
		if (sdProv == null) {
			throw new DSSException("No softdeletion provider defined");
		}
		return sdProv.softDelete(objDelete, ndbTarget, this);
	}

	public boolean hardDelete(T objDelete, boolean direct) throws DSSException {
		return hardDelete(objDelete, ExtLibUtil.getCurrentDatabase(), direct);
	}

	public boolean hardDelete(T objDelete, Database ndbTarget, boolean direct) throws DSSException {
		if (direct) {
			return DominoStorageService.getInstance().deleteObject(objDelete, ndbTarget);
		}
		ISoftDeletionProvider<T> sdProv = getSoftDeletionProvider();
		if (sdProv == null) {
			throw new DSSException("No softdeletion provider defined");
		}
		return sdProv.hardDelete(objDelete, ndbTarget, this);

	}

	public boolean undelete(T objDelete) throws DSSException {
		return undelete(objDelete, ExtLibUtil.getCurrentDatabase());
	}

	public boolean undelete(T objDelete, Database ndbTarget) throws DSSException {

		ISoftDeletionProvider<T> sdProv = getSoftDeletionProvider();
		if (sdProv == null) {
			throw new DSSException("No softdeletion provider defined");
		}
		return sdProv.undelete(objDelete, ndbTarget, this);
	}

	private boolean isDocumentOfInterest(Document docCurrent, List<String> lstRolesGroups, List<String> lstFieldsToCheck) {
		try {
			for (String strField : lstFieldsToCheck) {
				if (docCurrent.hasItem(strField)) {
					List<String> lstValues = getStringListFromDocument(docCurrent, strField);
					if (CollectionUtils.containsAny(lstRolesGroups, lstValues)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private List<String> getStringListFromDocument(Document docCurrent, String strField) {
		List<String> lstRC = new ArrayList<String>();
		try {
			Vector<?> vecRes = docCurrent.getItemValue(strField);
			for (Iterator<?> itValue = vecRes.iterator(); itValue.hasNext();) {
				lstRC.add("" + itValue.next());
			}
		} catch (Exception e) {
		}
		return lstRC;
	}

	protected abstract T createObject();

	public List<ChangeLogEntry> getChangeLog(T objCurrent) {
		return getChangeLog(objCurrent, RoleAndGroupProvider.getInstance().getMyGroupsAndRoles());
	}

	public List<ChangeLogEntry> getChangeLog(T objCurrent, List<String> arrMyRoles) {
		List<ChangeLogEntry> lstRC = null;
		try {
			String strObjectClass = objCurrent.getClass().getCanonicalName();
			String strPK = DominoStorageService.getInstance().getObjectID(objCurrent).toString();
			lstRC = ChangeLogService.getInstance().getChangeLog(strObjectClass, strPK);
			for (Iterator<ChangeLogEntry> itCL = lstRC.iterator(); itCL.hasNext();) {
				ChangeLogEntry cl = itCL.next();
				if (!DominoStorageService.getInstance().isFieldAccessable(objCurrent, cl, arrMyRoles)) {
					itCL.remove();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;
	}
}
