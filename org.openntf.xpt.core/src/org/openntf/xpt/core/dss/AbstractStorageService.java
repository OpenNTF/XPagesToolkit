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
import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.dss.changeLog.ChangeLogEntry;
import org.openntf.xpt.core.dss.changeLog.ChangeLogService;
import org.openntf.xpt.core.utils.RoleAndGroupProvider;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.extlib.util.ExtLibUtil;


/**
 * Abstract class with all methode's for a storage service.
 * 
 * You need to define the createObject() to build the object for T
 * 
 * @author Christian Guedemann
 *
 * @param <T> The Type of the Object you want to load/store
 */
public abstract class AbstractStorageService<T> {

	protected AbstractStorageService() {
	}

	/**
	 * Stores the object in the current database.
	 * @param obj The object to store
	 * @return true if the save operation was successful
	 */
	
	public boolean save(T obj) {
		return saveTo(obj, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Stores the objec in the target database.
	 * @param obj the object to store
	 * @param targetDatabase the target database
	 * @return true if the save operation was successful
	 */
	public boolean saveTo(T obj, Database targetDatabase) {
		try {
			return DominoStorageService.getInstance().saveObject(obj, targetDatabase);
		} catch (DSSException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Loads an object from the current database
	 * @param id
	 * @return the object
	 */
	public T getById(String id) {
		return getByIdFrom(id, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Load an object form the source database
	 * @param id
	 * @param sourceDatabase
	 * @return the object
	 */
	public T getByIdFrom(String id, Database sourceDatabase) {
		try {
			T ret = createObject();
			if (!DominoStorageService.getInstance().getObject(ret, id, sourceDatabase)) {
				return null;
			}
			return ret;
		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
	}

	
	/**
	 * Loads all objects from a view
	 * @param viewName
	 * @return List of objects
	 */
	public List<T> getAll(String viewName) {
		return getAllFrom(viewName, ExtLibUtil.getCurrentDatabase());
	}

	
	/**
	 * Loads all objects form a view from the source database
	 * @param viewName
	 * @param sourceDatabase
	 * @return
	 */
	public List<T> getAllFrom(String viewName, Database sourceDatabase) {
		List<T> ret = new ArrayList<T>();
		try {
			View viwDabases = sourceDatabase.getView(viewName);
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
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
		return ret;
	}

	
	/**
	 * Loads all object by a foreign id from a view
	 * @param foreignId
	 * @param viewName
	 * @return List with object
	 */
	public List<T> getObjectsByForeignId(String foreignId, String viewName) {
		return getObjectsByForeignIdFrom(foreignId, viewName, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Load all objects by a foreign id form a view from the source database
	 * @param foreignId
	 * @param viewName
	 * @param sourceDatabase
	 * @return
	 */
	public List<T> getObjectsByForeignIdFrom(String foreignId, String viewName, Database sourceDatabase) {
		List<T> ret = new ArrayList<T>();
		try {
			View view = sourceDatabase.getView(viewName);
			DocumentCollection documents = view.getAllDocumentsByKey(foreignId, true);
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
			view.recycle();
		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
		return ret;
	}

	/**
	 * Loads all object where my user, group or role occurs in one of the fields
	 * @param viewName
	 * @param fieldsToCheck
	 * @return
	 */
	public List<T> getAllMyObjects(String viewName, List<String> fieldsToCheck) {
		return getAllMyObjectsFrom(viewName, fieldsToCheck, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Loads all object where my user, group or role occurs in one of the fields form the source database
	 * @param viewName
	 * @param fieldsToCheck
	 * @param sourceDatabase
	 * @return
	 */
	public List<T> getAllMyObjectsFrom(String viewName, List<String> fieldsToCheck, Database sourceDatabase) {
		List<T> ret = new ArrayList<T>();
		List<String> lstRolesGroups = RoleAndGroupProvider.getInstance().getMyGroupsAndRoles();
		try {
			View viwDabases = sourceDatabase.getView(viewName);
			Document docNext = viwDabases.getFirstDocument();
			while (docNext != null) {
				Document docCurrent = docNext;
				docNext = viwDabases.getNextDocument(docNext);
				if (isDocumentOfInterest(docCurrent, lstRolesGroups, fieldsToCheck)) {
					T obj = createObject();
					if (DominoStorageService.getInstance().getObjectWithDocument(obj, docCurrent)) {
						ret.add(obj);
					}
				}
				docCurrent.recycle();

			}
			viwDabases.recycle();
		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
		return ret;

	}

	/**
	 * Loads all object where the user, group or role occurs in one of the fields 
	 * @param userName Name of the user
	 * @param viewName
	 * @param fieldsToCheck 
	 * @return
	 */
	public List<T> getAllObjectFor(String userName, String viewName, List<String> fieldsToCheck) {
		return getAllObjectsForFrom(userName, viewName, fieldsToCheck, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Loads all object where the user, group or role occurs in one of the fields form the source database
	 * @param userName
	 * @param viewName
	 * @param fieldsToCheck
	 * @param sourceDatabase
	 * @return
	 */
	public List<T> getAllObjectsForFrom(String userName, String viewName, List<String> fieldsToCheck, Database sourceDatabase) {
		List<T> ret = new ArrayList<T>();
		List<String> lstRolesGroups = RoleAndGroupProvider.getInstance().getGroupsAndRolesOf(userName, sourceDatabase);
		try {
			View view = sourceDatabase.getView(viewName);
			Document docNext = view.getFirstDocument();
			while (docNext != null) {
				Document docCurrent = docNext;
				docNext = view.getNextDocument(docNext);
				if (isDocumentOfInterest(docCurrent, lstRolesGroups, fieldsToCheck)) {
					T obj = createObject();
					if (DominoStorageService.getInstance().getObjectWithDocument(obj, docCurrent)) {
						ret.add(obj);
					}
				}
				docCurrent.recycle();

			}
			view.recycle();
		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
		return ret;

	}

	/**
	 * Override this method with your implementation of the SoftDeleteProvider
	 * @return
	 */
	public ISoftDeletionProvider<T> getSoftDeletionProvider() {
		return null;
	}

	/**
	 * Try's to softDelete the object from the current database. It throws a DSSException, if no softdeleteprovider is defined.
	 * @param objDelete
	 * @return true if the operation was successful
	 * @throws DSSException
	 */
	public boolean softDelete(T objDelete) throws DSSException {
		return softDelete(objDelete, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Try's to softDelete the object from the current database. It throws a DSSException, if no softdeleteprovider is defined.
	 * @param objDelete
	 * @param targetDatabase
	 * @return true if the operation was successful
	 * @throws DSSException
	 */
	public boolean softDelete(T objDelete, Database targetDatabase) throws DSSException {

		ISoftDeletionProvider<T> sdProv = getSoftDeletionProvider();
		if (sdProv == null) {
			throw new DSSException("No softdeletion provider defined");
		}
		return sdProv.softDelete(objDelete, targetDatabase, this);
	}
	
	

	/**
	 * Deletes an object from the current database. If direct is true, the SoftDeleprovider is not used
	 * @param objDelete
	 * @param direct
	 * @return
	 * @throws DSSException
	 */
	public boolean hardDelete(T objDelete, boolean direct) throws DSSException {
		return hardDelete(objDelete, ExtLibUtil.getCurrentDatabase(), direct);
	}

	/**
	 * Deletes an object from the target database. If direct is true, the SoftDeleprovider is not used
	 * @param objDelete
	 * @param targetDatabase
	 * @param direct
	 * @return
	 * @throws DSSException
	 */
	public boolean hardDelete(T objDelete, Database targetDatabase, boolean direct) throws DSSException {
		if (direct) {
			return DominoStorageService.getInstance().deleteObject(objDelete, targetDatabase);
		}
		ISoftDeletionProvider<T> sdProv = getSoftDeletionProvider();
		if (sdProv == null) {
			throw new DSSException("No softdeletion provider defined");
		}
		return sdProv.hardDelete(objDelete, targetDatabase, this);

	}

	/**
	 * Undelet's an object from the current database using the SoftDeleteProvider
	 * @param objDelete
	 * @return
	 * @throws DSSException
	 */
	public boolean undelete(T objDelete) throws DSSException {
		return undelete(objDelete, ExtLibUtil.getCurrentDatabase());
	}

	/**
	 * Undelet's an object from the target database using the SoftDeleteProvider
	 * @param objDelete
	 * @param targetDatabase
	 * @return
	 * @throws DSSException
	 */
	public boolean undelete(T objDelete, Database targetDatabase) throws DSSException {

		ISoftDeletionProvider<T> sdProv = getSoftDeletionProvider();
		if (sdProv == null) {
			throw new DSSException("No softdeletion provider defined");
		}
		return sdProv.undelete(objDelete, targetDatabase, this);
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
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
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
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
		return lstRC;
	}

	
	/**
	 * Override this function to create a blank object
	 * @return
	 */
	protected abstract T createObject();

	public List<ChangeLogEntry> getChangeLog(T objCurrent) {
		return getChangeLog(objCurrent, RoleAndGroupProvider.getInstance().getMyGroupsAndRoles());
	}

	
	/**
	 * Get the ChangeLog entries for an object.
	 * @param objCurrent
	 * @param myRoles
	 * @return
	 */
	public List<ChangeLogEntry> getChangeLog(T objCurrent, List<String> myRoles) {
		List<ChangeLogEntry> lstRC = null;
		try {
			String strObjectClass = objCurrent.getClass().getCanonicalName();
			String strPK = DominoStorageService.getInstance().getObjectID(objCurrent).toString();
			lstRC = ChangeLogService.getInstance().getChangeLog(strObjectClass, strPK);
			for (Iterator<ChangeLogEntry> itCL = lstRC.iterator(); itCL.hasNext();) {
				ChangeLogEntry cl = itCL.next();
				if (!DominoStorageService.getInstance().isFieldAccessable(objCurrent, cl, myRoles)) {
					itCL.remove();
				}

			}

		} catch (Exception e) {
			LoggerFactory.logError(getClass(), "General Error",e);
			throw new XPTRuntimeException("General Error", e);
		}
		return lstRC;
	}
}
