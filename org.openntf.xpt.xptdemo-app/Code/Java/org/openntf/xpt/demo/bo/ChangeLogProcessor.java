package org.openntf.xpt.demo.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.DocumentCollection;

import org.openntf.xpt.core.dss.changeLog.ChangeLogEntry;
import org.openntf.xpt.core.dss.changeLog.IChangeLogProcessor;
import org.openntf.xpt.core.dss.changeLog.StorageAction;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ChangeLogProcessor implements IChangeLogProcessor {

	public int doChangeLog(ChangeLogEntry cle, Session sesCurrent, Database ndbCurrent) {
		System.out.println("Calling CLF");
		try {
			Document docResult = ndbCurrent.createDocument();
			docResult.replaceItemValue("Form", "changeLog");
			docResult.replaceItemValue("ChangeAction", cle.getAction().toString());
			docResult.replaceItemValue("ChangeDate", ExtLibUtil.getCurrentSession().createDateTime(cle.getDate()));
			docResult.replaceItemValue("ObjectClass", cle.getObjectClass());
			docResult.replaceItemValue("ObjectField", cle.getObjectField());
			docResult.replaceItemValue("PrimaryKey", cle.getPrimaryKey());
			docResult.replaceItemValue("StorageField", cle.getStorageField());
			docResult.replaceItemValue("User", cle.getUser());

			processValue(docResult, cle.getNewValue(), "newValue");
			processValue(docResult, cle.getOldValue(), "oldValue");

			docResult.save(true, false, true);
			docResult.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<ChangeLogEntry> getAllChangeLogEntries(String strObjectClassName, String strPK) {
		List<ChangeLogEntry> lstRC = new ArrayList<ChangeLogEntry>();
		try {
			View viwLUP = ExtLibUtil.getCurrentDatabase().getView("LUPChangeLog");
			DocumentCollection dclCurrent = viwLUP.getAllDocumentsByKey(strObjectClassName + "@@@" + strPK, true);
			Document docNext = dclCurrent.getFirstDocument();
			while (docNext != null) {
				Document docProcess = docNext;
				docNext = dclCurrent.getNextDocument();
				ChangeLogEntry cle = processDoc2ChangeLog(docProcess);
				if (cle != null) {
					lstRC.add(cle);
				}
				docProcess.recycle();
			}
			dclCurrent.recycle();
			viwLUP.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstRC;
	}

	private ChangeLogEntry processDoc2ChangeLog(Document docProcess) {
		ChangeLogEntry cle = new ChangeLogEntry();
		try {
			cle.setDate(((DateTime) docProcess.getItemValueDateTimeArray("ChangeDate").elementAt(0)).toJavaDate());
			cle.setObjectClass(docProcess.getItemValueString("ObjectClass"));
			cle.setObjectField(docProcess.getItemValueString("ObjectField"));
			cle.setPrimaryKey(docProcess.getItemValueString("PrimaryKey"));
			cle.setStorageField(docProcess.getItemValueString("StorageField"));
			cle.setUser(docProcess.getItemValueString("User"));
			cle.setAction(StorageAction.valueOf(docProcess.getItemValueString("ChangeAction")));
			cle.setNewValue(checkValue(docProcess.getItemValue("NewValue")));
			cle.setOldValue(checkValue(docProcess.getItemValue("OldValue")));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return cle;
	}

	public List<ChangeLogEntry> getAllChangeLogEntries4Attribute(String strObjectClassName, String strPK, String strObjectMember) {
		return null;
	}

	@SuppressWarnings("unchecked")
	private void processValue(Document doc, Object objValue, String strTarget) {
		try {
			Object objSave = objValue;
			if (objValue.getClass().isArray()) {
				objSave = new Vector(Arrays.asList(objValue));
			}
			if (objValue instanceof List) {
				objSave = new Vector((List) objValue);
			}
			if (objValue instanceof Date) {
				objSave = ExtLibUtil.getCurrentSession().createDateTime((Date) objValue);
			}

			doc.replaceItemValue(strTarget, objSave);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Vector checkValue(Vector vecCurrent) {
		if (vecCurrent == null) {
			return null;
		}
		Vector vecRC = new Vector(vecCurrent.size());
		for (Iterator itVec = vecCurrent.iterator(); itVec.hasNext();) {
			Object obj = itVec.next();
			if (obj instanceof DateTime) {
				try {
					vecRC.add(((DateTime) obj).toJavaDate());
				} catch (Exception e) {
				}
			} else {
				vecRC.add(obj);
			}
		}
		return vecRC;
	}
}
