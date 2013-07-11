package org.openntf.xpt.core.dss.binding.files;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.RichTextItem;
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
	
	private FileUploadListBinder(){
		
	}
	
	public void processDomino2Java(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {

	}

	public void processJava2Domino(Document docCurrent, Object objCurrent,
			String strNotesField, String strJavaField,
			HashMap<String, Object> addValues) {
		try {
			
			List<UploadedFile> files = getValue(objCurrent, strJavaField);
			
			for(UploadedFile file: files){
			IUploadedFile FTemp = file.getUploadedFile();
			File SrFile = FTemp.getServerFile();
			
			 File FNew = new File(SrFile.getParentFile().getAbsolutePath() +
			 File.separator + FTemp.getClientFileName()); 
			 SrFile.renameTo(FNew);
			 RichTextItem rt = null;
			 rt = (RichTextItem) docCurrent.getFirstItem(strNotesField);
			 if(rt == null)
				 rt = docCurrent.createRichTextItem(strNotesField);
			 rt.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "",FNew.getAbsolutePath(), null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

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

}
