package org.openntf.xpt.objectlist.utils;

import java.io.File;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.component.UIInputRichText;
import com.ibm.xsp.http.MimeMultipart;

public enum MIMEMultipartProcessor {
	INSTANCE;

	public MimeMultipart addEmbeddedImage(final UIInputRichText.EmbeddedImage embImage, final MimeMultipart mmCurrent) {
		StringBuilder sb = new StringBuilder("");
		if (mmCurrent != null) {
			sb.append(mmCurrent.getHTML());
		}
		LoggerFactory.logInfo(this.getClass(), embImage.getFilename() + " / " + embImage.getMimeType(), null);
		sb.append("<img src=\"data:" + embImage.getMimeType() + ";base64,");
		try {
			File flCurrent = new File(embImage.getUploadedFile().getServerFileName());
			sb.append(Base64.encodeBase64String(FileUtils.readFileToByteArray(flCurrent)));
			sb.append("\">");
		} catch (Exception ex) {
			LoggerFactory.logError(this.getClass(), "Parse Error", ex);
			throw new XPTRuntimeException("Error during addEmbeddedImage", ex);
		}
		return MimeMultipart.fromHTML(sb.toString());
	}
}
