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
package org.openntf.xpt.core.dss.binding.files;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.RichTextItem;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.dss.binding.util.FileHelper;
import org.openntf.xpt.core.utils.MimeTypeService;

public class FileDownloadBinder implements IBinder<List<FileHelper>> {

	private static FileDownloadBinder m_Binder;

	@SuppressWarnings("unchecked")
	public void processDomino2Java(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + strJavaField, List.class);

			String server = docCurrent.getParentDatabase().getServer();
			String path = docCurrent.getParentDatabase().getFilePath();
			List<FileHelper> myFiles = new ArrayList<FileHelper>();

			RichTextItem rti = (RichTextItem) docCurrent.getFirstItem(strNotesField);
			if (rti != null) {
				Vector<EmbeddedObject> entitys = rti.getEmbeddedObjects();

				for (EmbeddedObject entity : entitys) {
					FileHelper fh = new FileHelper();
					fh.setId(UUID.randomUUID().toString());
					fh.setFileSize(entity.getFileSize());
					fh.setName(entity.getName());
					fh.setDisplayName(entity.getSource());
					fh.setDocID(docCurrent.getUniversalID());
					fh.setCreated(docCurrent.getCreated().toJavaDate());
					fh.setServer(server);
					fh.setPath(path);
					fh.setFieldName(strNotesField);
					/*
					 * InputStream is = entity.getInputStream();
					 * ByteArrayOutputStream byteStream = new
					 * ByteArrayOutputStream();
					 * 
					 * int nextValue = is.read(); while (-1 != nextValue) {
					 * byteStream.write(nextValue); nextValue = is.read(); }
					 * 
					 * byte[] byteSource = byteStream.toByteArray();
					 * 
					 * fh.setByteFile(byteSource);
					 */

					File file = new File(entity.getSource());

					fh.setFileType(MimeTypeService.getInstance().getMimeTypes().getContentType(file));
					file.delete();

					myFiles.add(fh);

				}
			}
			FileHelper fh = new FileHelper();
			fh.setId(UUID.randomUUID().toString());
			fh.setDocID(docCurrent.getUniversalID());
			fh.setServer(server);
			fh.setPath(path);
			fh.setFieldName(strNotesField);
			fh.setNewFile(true);

			myFiles.add(fh);
			mt.invoke(objCurrent, myFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void processJava2Domino(Document docCurrent, Object objCurrent, String strNotesField, String strJavaField, HashMap<String, Object> addValues) {

		if (docCurrent != null && getValue(objCurrent, strJavaField) == null) {
			try {
				String server = docCurrent.getParentDatabase().getServer();
				String path = docCurrent.getParentDatabase().getFilePath();
				List<FileHelper> myFiles = new ArrayList<FileHelper>();

				FileHelper fh = new FileHelper();
				fh.setId(UUID.randomUUID().toString());
				fh.setDocID(docCurrent.getUniversalID());
				fh.setServer(server);
				fh.setPath(path);
				fh.setFieldName(strNotesField);
				fh.setNewFile(true);

				myFiles.add(fh);

				Method mt = objCurrent.getClass().getMethod("set" + strJavaField, List.class);
				mt.invoke(objCurrent, myFiles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static IBinder<List<FileHelper>> getInstance() {
		if (m_Binder == null) {
			m_Binder = new FileDownloadBinder();
		}
		return m_Binder;
	}

	private FileDownloadBinder() {

	}

	@SuppressWarnings("unchecked")
	public List<FileHelper> getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (List<FileHelper>) mt.invoke(objCurrent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
