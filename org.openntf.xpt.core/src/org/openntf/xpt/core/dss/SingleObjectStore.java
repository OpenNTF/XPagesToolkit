package org.openntf.xpt.core.dss;

import lotus.domino.Database;

import org.openntf.xpt.core.utils.DatabaseProvider;

import com.ibm.commons.util.StringUtil;

/**
 * Decorator for AbstractStorageService used in DDObjectDataSource
 * 
 * @author Christian Guedemann
 * 
 * @param <T>
 */
public class SingleObjectStore<T> {

	private final AbstractStorageService<T> m_Store;

	public SingleObjectStore(AbstractStorageService<T> store) {
		m_Store = store;
	}

	public T getObjectByID(String id, String databaseName) {
		if (StringUtil.isEmpty(databaseName)) {
			return m_Store.getById(id);
		} else {
			Database ndb = DatabaseProvider.INSTANCE.getDatabase(databaseName, false);
			T obj = m_Store.getByIdFrom(id, ndb);
			DatabaseProvider.INSTANCE.handleRecylce(ndb);
			return obj;
		}
	}

	public boolean saveObject(T obj, String databaseName) {
		if (StringUtil.isEmpty(databaseName)) {
			return m_Store.save(obj);
		} else {
			Database ndb = DatabaseProvider.INSTANCE.getDatabase(databaseName, false);
			boolean rc = m_Store.saveTo(obj, ndb);
			DatabaseProvider.INSTANCE.handleRecylce(ndb);
			return rc;
		}
	}

	public T newObject() {
		return m_Store.createObject();
	}

	public boolean deleteObject(T obj, String databaseName) throws DSSException {
		if (StringUtil.isEmpty(databaseName)) {
			if (m_Store.getSoftDeletionProvider() != null) {
				return m_Store.softDelete(obj);
			} else {
				return m_Store.hardDelete(obj, true);
			}
		} else {

			Database ndb = DatabaseProvider.INSTANCE.getDatabase(databaseName, false);
			boolean rc = false;
			if (m_Store.getSoftDeletionProvider() != null) {
				rc = m_Store.softDelete(obj, ndb);
			} else {
				rc = m_Store.hardDelete(obj, ndb, true);
			}
			DatabaseProvider.INSTANCE.handleRecylce(ndb);
			return rc;
		}
	}
}
