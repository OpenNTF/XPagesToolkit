/**
 * Copyright 2014, WebGate Consulting AG
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

	@SuppressWarnings("unchecked")
	public boolean saveObject(Object obj, String databaseName) {
		if (StringUtil.isEmpty(databaseName)) {
			return m_Store.save((T)obj);
		} else {
			Database ndb = DatabaseProvider.INSTANCE.getDatabase(databaseName, false);
			boolean rc = m_Store.saveTo((T)obj, ndb);
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
