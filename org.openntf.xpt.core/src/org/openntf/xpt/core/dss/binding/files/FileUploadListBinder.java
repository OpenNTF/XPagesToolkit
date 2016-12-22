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
package org.openntf.xpt.core.dss.binding.files;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.RichTextItem;

import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;

import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.http.IUploadedFile;

//NOT IN USE//NOT IN USE//NOT IN USE//NOT IN USE//NOT IN USE//NOT IN USE//NOT IN USE//NOT IN USE
public class FileUploadListBinder implements IBinder<List<UploadedFile>> {

	private static FileUploadListBinder m_Binder;

	public static IBinder<List<UploadedFile>> getInstance() {
		if (m_Binder == null) {
			m_Binder = new FileUploadListBinder();
		}
		return m_Binder;
	}

	private FileUploadListBinder() {

	}

	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecCurrent, Definition def) {

	}

	public List<UploadedFile>[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		try {

			List<UploadedFile> files = getValue(objCurrent, def.getJavaField());

			for (UploadedFile file : files) {
				IUploadedFile FTemp = file.getUploadedFile();
				File SrFile = FTemp.getServerFile();

				File FNew = new File(SrFile.getParentFile().getAbsolutePath() + File.separator + FTemp.getClientFileName());
				SrFile.renameTo(FNew);
				RichTextItem rt = null;
				rt = (RichTextItem) docCurrent.getFirstItem(def.getNotesField());
				if (rt == null)
					rt = docCurrent.createRichTextItem(def.getNotesField());
				rt.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", FNew.getAbsolutePath(), null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<UploadedFile> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<UploadedFile>) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<UploadedFile> getValueFromStore(Document docCurrent, Vector<?> vecCurrent, Definition def) {
		return null;
	}

}
