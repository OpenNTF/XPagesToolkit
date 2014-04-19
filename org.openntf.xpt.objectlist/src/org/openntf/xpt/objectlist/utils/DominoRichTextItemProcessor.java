package org.openntf.xpt.objectlist.utils;

import com.ibm.xsp.component.UIFileuploadEx;
import com.ibm.xsp.component.UIInputRichText;
import com.ibm.xsp.http.IMimeMultipart;
import com.ibm.xsp.model.domino.wrapped.DominoRichTextItem;

public enum DominoRichTextItemProcessor {
	INSTANCE;

	public void addEmbeddedImage(final UIInputRichText.EmbeddedImage embImage, final DominoRichTextItem dtr) throws Exception {
		dtr.getParent().restoreWrappedDocument();
		dtr.persistEmbeddedImage(embImage);
		dtr.closeMIMEEntities(false);
	}
	
	
	public void addMimeMultiPart(final IMimeMultipart mm, final DominoRichTextItem dtr) throws Exception {
		dtr.getParent().restoreWrappedDocument();
		dtr.replaceNewValue(mm);
		//dtr.closeMIMEEntities(false);
	}
	public void addMFileUpload(final UIFileuploadEx.UploadedFile fup, final DominoRichTextItem dtr) throws Exception {
		dtr.getParent().restoreWrappedDocument();
		dtr.persistFile(fup);;
		//dtr.closeMIMEEntities(false);
	}

}
