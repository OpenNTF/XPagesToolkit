package org.openntf.xpt.core.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.RichTextItem;
import lotus.domino.Session;

import org.openntf.xpt.core.dss.binding.util.FileHelper;

import com.ibm.commons.util.io.StreamUtil;
import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.http.IUploadedFile;

public enum FileService {
	INSTANCE;
	// Get Values for Custom Control - Register Type of Files of the
	// CustomControl
	@SuppressWarnings("unchecked")
	public List<FileHelper> getValue(Object obj, String strFieldName, String strCcId, HashMap<String, FileHelper> valueFields) {
		List<FileHelper> values;
		try {

			Method mt = obj.getClass().getMethod("get" + strFieldName);
			values = (List<FileHelper>) mt.invoke(obj);

			if (values == null)
				return null;

			for (FileHelper fh : values) {
				valueFields.put(strCcId + "|" + fh.getId(), fh);
			}

			return values;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public void unregisterFiles(String strCcId, HashMap<String, Object>
	 * valueFields) { valueFields.remove(strCcId); }
	 */

	public InputStream getFileStream(FileHelper fh) {
		return getFileStream(fh, ExtLibUtil.getCurrentSession());
	}

	public InputStream getFileStream(FileHelper fh, Session ses) {
		try {
			Database ndbCurrent = DatabaseProvider.INSTANCE.getDatabaseWithSession(fh.getServer() + "!!" + fh.getPath(), ses);
			if (ndbCurrent == null)
				return null;
			Document docCurrent = ndbCurrent.getDocumentByUNID(fh.getDocID());
			if (docCurrent == null) {
				ndbCurrent.recycle();
				return null;
			}

			EmbeddedObject entity = docCurrent.getAttachment(fh.getName());
			if (entity == null) {
				ndbCurrent.recycle();
				docCurrent.recycle();
				return null;
			}
			InputStream is = entity.getInputStream();

			entity.recycle();
			docCurrent.recycle();
			DatabaseProvider.INSTANCE.handleRecylce(ndbCurrent);

			return is;// entity.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void removeFile(FileHelper fh) {
		try {
			Database ndbCurrent = ExtLibUtil.getCurrentSession().getDatabase(fh.getServer(), fh.getPath());
			if (ndbCurrent == null)
				return;
			Document docCurrent = ndbCurrent.getDocumentByUNID(fh.getDocID());
			if (docCurrent == null) {
				ndbCurrent.recycle();
				return;
			}
			// RESULTS IN NOTE ITEM NOT FOUND ERROR AFTERWARDS
			// EmbeddedObject entity = docCurrent.getAttachment(fh.getName());
			// if (entity == null)
			// return;
			// entity.remove();

			RichTextItem rti = (RichTextItem) docCurrent.getFirstItem(fh.getFieldName());
			Vector<EmbeddedObject> entitys = rti.getEmbeddedObjects();

			for (EmbeddedObject entity : entitys) {
				if (entity.getType() == EmbeddedObject.EMBED_ATTACHMENT) {
					if (entity.getName().equals(fh.getName())) {
						entity.remove();
						break;
					}
				}
			}
			docCurrent.save(true, false, true);

			rti.recycle();
			docCurrent.recycle();
			ndbCurrent.recycle();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadFile(UploadedFile file, Object obj, String strFieldName, String strCcId, String strNewId, List<FileHelper> objFiles, HashMap<String, FileHelper> fileList) {
		try {
			Method mt = null;

			FileHelper fhCurrent = null;
			for (FileHelper fh : objFiles) {
				if (fh.isNewFile()) {
					fhCurrent = fh;
					break;
				}
			}

			if (fhCurrent != null) {
				FileHelper fhNew = addFile(fhCurrent, file, strNewId);
				objFiles.add(fhNew);
				fileList.put(strCcId + "|" + fhNew.getId(), fhNew);
				mt = obj.getClass().getMethod("set" + strFieldName, List.class);
				mt.invoke(obj, objFiles);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FileHelper addFile(FileHelper fh, UploadedFile file, String strNewId) {
		try {
			Database ndbCurrent = ExtLibUtil.getCurrentSession().getDatabase(fh.getServer(), fh.getPath());
			if (ndbCurrent == null)
				return null;
			Document docCurrent = ndbCurrent.getDocumentByUNID(fh.getDocID());
			if (docCurrent == null) {
				ndbCurrent.recycle();
				return null;
			}
			IUploadedFile FTemp = file.getUploadedFile();
			File SrFile = FTemp.getServerFile();

			File FNew = new File(SrFile.getParentFile().getAbsolutePath() + File.separator + FTemp.getClientFileName());
			SrFile.renameTo(FNew);
			RichTextItem rt = null;
			rt = (RichTextItem) docCurrent.getFirstItem(fh.getFieldName());
			if (rt == null) {
				rt = docCurrent.createRichTextItem(fh.getFieldName());
			}

			EmbeddedObject em = rt.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", FNew.getAbsolutePath(), null);

			docCurrent.save(true, false, true);
			FileHelper fhNew = new FileHelper();
			fhNew.setFieldName(fh.getFieldName());
			fhNew.setServer(fh.getServer());
			fhNew.setPath(fh.getPath());
			fhNew.setFileSize(em.getFileSize());
			fhNew.setName(em.getName());
			fhNew.setDisplayName(FTemp.getClientFileName());
			fhNew.setId(strNewId);
			fhNew.setDocID(fh.getDocID());
			fhNew.setFileType(MimeTypeService.getInstance().getMimeTypes().getContentType(FNew));
			fhNew.setNewFile(false);

			rt.recycle();
			docCurrent.recycle();
			ndbCurrent.recycle();

			return fhNew;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void processToStream(RestServiceEngine engine, FileHelper fh) {// byte[]
		// byteFile)
		// {
		try {
			InputStream is = getFileStream(fh);
			OutputStream os = engine.getHttpResponse().getOutputStream();
			// os.write(byteFile);
			StreamUtil.copyStream(is, os);
			os.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
