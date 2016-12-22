/**
 * Copyright 2013, WebGate Consulting AG
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.openntf.xpt.core.dss.changeLog.ChangeLogEntry;
import org.openntf.xpt.core.dss.changeLog.ChangeLogService;
import org.openntf.xpt.core.dss.changeLog.StorageAction;
import org.openntf.xpt.core.dss.encryption.EncryptionService;

public class Java2DominoBinder {

	private ArrayList<Definition> m_Definition;

	public Java2DominoBinder() {
		m_Definition = new ArrayList<Definition>();
	}

	public void addDefinition(Definition def) {
		m_Definition.add(def);
	}

	/*
	 * public void addDefinition(String strNotesField, String strJavaField,
	 * IBinder<?> binCurrent, boolean changeLog, HashMap<String, Object>
	 * addValues, boolean encrypted, String[] encRoles) { m_Definition.add(new
	 * Definition(strNotesField, strJavaField, binCurrent, changeLog, addValues,
	 * encrypted, encRoles)); }
	 */
	public void processDocument(Document docProcess, Object objCurrent, String strPK) throws NotesException {
		StorageAction action = StorageAction.MODIFY;
		if (docProcess.isNewNote()) {
			action = StorageAction.CREATE;
		}
		for (Iterator<Definition> itDefinition = m_Definition.iterator(); itDefinition.hasNext();) {
			Definition defCurrent = itDefinition.next();
			Object[] arrResult = null;
			arrResult = defCurrent.getBinder().processJava2Domino(docProcess, objCurrent, defCurrent);
			if (arrResult != null && defCurrent.getBinder() instanceof IEncryptionBinder) {
				arrResult = ((IEncryptionBinder) defCurrent.getBinder()).getChangeLogValues(arrResult, defCurrent);

			}
			if (defCurrent.isChangeLog() && arrResult != null) {
				String strUser = docProcess.getParentDatabase().getParent().getEffectiveUserName();
				ChangeLogService.getInstance().checkChangeLog(objCurrent, strPK, arrResult[0], arrResult[1], defCurrent.getJavaField(), defCurrent.getNotesField(), action, strUser,
						docProcess.getParentDatabase().getParent(), docProcess.getParentDatabase());
			}

		}
	}

	public boolean isFieldAccessible(String strFieldName, List<String> currentRoles) {
		for (Definition def : m_Definition) {
			if (def.isEncrypted()) {
				if (def.getJavaField().equals(strFieldName)) {
					return ((IEncryptionBinder) def.getBinder()).hasAccess(def, currentRoles);
				}
			}
		}
		return true;

	}

	public boolean isFieldAccessible(String strFieldName, List<String> currentRoles, ChangeLogEntry cl) {
		for (Definition def : m_Definition) {
			if (def.isEncrypted()) {
				if (def.getJavaField().equals(strFieldName)) {
					boolean hasAccess = ((IEncryptionBinder) def.getBinder()).hasAccess(def, currentRoles);
					if (hasAccess) {
						getDecryptedChangeLogWithoutCheck(cl);
					}
					return hasAccess;
				}
			}
		}
		return true;

	}

	@SuppressWarnings("rawtypes")
	private void getDecryptedChangeLogWithoutCheck(ChangeLogEntry cl) {
		if (cl.getOldValue() instanceof Vector) {
			Vector vc = (Vector) cl.getOldValue();
			if (vc.size() > 0) {
				if (vc.get(0) instanceof String) {
					String encVal = (String) vc.get(0);
					String decVal = EncryptionService.getInstance().decrypt(encVal);
					if (decVal != null) {
						Vector<String> rc = new Vector<String>();
						rc.add(decVal);
						cl.setOldValue(rc);
					}
				}
			}
		}

		if (cl.getNewValue() instanceof Vector) {
			Vector vc = (Vector) cl.getNewValue();
			if (vc.size() > 0) {
				if (vc.get(0) instanceof String) {
					String encVal = (String) vc.get(0);
					String decVal = EncryptionService.getInstance().decrypt(encVal);
					if (decVal != null) {
						Vector<String> rc = new Vector<String>();
						rc.add(decVal);
						cl.setNewValue(rc);
					}
				}
			}
		}
	}

	public void cleanFields(Document docCurrent) throws NotesException {
		for (Definition def : m_Definition) {
			if (docCurrent.hasItem(def.getNotesField())) {
				docCurrent.removeItem(def.getNotesField());
			}
		}
	}

	public void processList2Document(Document docProcess, List<Object> lstCurrent, int nMax) throws NotesException {
		int nCount = 0;
		for (Object objCurrent : lstCurrent) {
			for (Definition def : m_Definition) {
				Definition defCurrent = Definition.cloneDefinition(def, nCount);
				defCurrent.getBinder().processJava2Domino(docProcess, objCurrent, defCurrent);
			}
			nCount++;
		}
		if (nMax > nCount) {
			for (int nDelete = nCount; nDelete < nMax; nDelete++) {
				for (Definition def : m_Definition) {
					Definition defCurrent = Definition.cloneDefinition(def, nCount);
					if (docProcess.hasItem(defCurrent.getNotesField())) {
						docProcess.removeItem(defCurrent.getNotesField());
					}
				}
			}
		}
	}

	public void cleanFields(Document docCurrent, int nSize) throws NotesException {
		for (int nCounter = 0; nCounter < nSize; nCounter++) {
			for (Definition def : m_Definition) {
				if (docCurrent.hasItem(def.getNotesField() + "_" + nCounter)) {
					docCurrent.removeItem(def.getNotesField() + "_" + nCounter);
				}
			}

		}
	}

}
