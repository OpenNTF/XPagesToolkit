package org.openntf.xpt.core.dss;

import static com.ibm.xsp.extlib.util.ExtLibUtil.getCurrentDatabase;

import java.util.ArrayList;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.View;

import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.DominoStorageService;

public abstract class AbstractStorageService<T> {

	protected AbstractStorageService() {
	}

	public boolean save(T obj) {
		try {
			return DominoStorageService.getInstance().saveObject(obj, getCurrentDatabase());
		} catch (DSSException e) {
			e.printStackTrace();
		}
		return false;
	}

	public T getById(String id) {
		try {
			T ret = createObject();
			DominoStorageService.getInstance().getObject(ret, id, getCurrentDatabase());
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<T> getAll(String viewId) {
		List<T> ret = new ArrayList<T>();
		try {
			Database ndbCurrent = getCurrentDatabase();
			View viwDabases = ndbCurrent.getView(viewId);
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
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	protected abstract T createObject();

	public List<T> getObjectsByForeignId(String foreignId, String viewId) {
		List<T> ret = new ArrayList<T>();
		try {
			Database ndbCurrent = getCurrentDatabase();
			View viwDabases = ndbCurrent.getView(viewId);
			DocumentCollection documents = viwDabases.getAllDocumentsByKey(foreignId);
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
			ndbCurrent.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}
