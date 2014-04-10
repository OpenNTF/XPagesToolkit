package org.openntf.xpt.core.dss;

/**
 * Decorator for AbstractStorageServer used in DDObjectDataSource
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
		return m_Store.getById(id);
	}

	public boolean saveObject(T obj, String databaseName) {
		return m_Store.save(obj);
	}
	public T newObject() {
		return m_Store.createObject();
	}
	
	public boolean deleteObject(T obj, String databaseName) throws DSSException {
		return m_Store.softDelete(obj);
	}
}
