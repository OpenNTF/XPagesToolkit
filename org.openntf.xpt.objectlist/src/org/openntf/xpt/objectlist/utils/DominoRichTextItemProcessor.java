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