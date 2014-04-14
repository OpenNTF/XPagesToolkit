package org.openntf.xpt.core.mime;

import org.openntf.xpt.core.XPTRuntimeException;

import lotus.domino.Document;

import com.ibm.xsp.http.IMimeMultipart;
import com.ibm.xsp.http.IMimePart;
import com.ibm.xsp.http.MimeMultipart;

public class MimeMultiPartExtended implements IMimeMultipart {

	private final String m_Server;
	private final String m_Database;
	private final String m_DocumentUNID;
	private final String m_FieldName;

	private MimeMultipart m_Content = null;

	public static MimeMultiPartExtended buildElement(Document doc, String fieldName) {
		try {
			return new MimeMultiPartExtended(doc.getParentDatabase().getServer(), doc.getParentDatabase().getFilePath(), doc.getUniversalID(), fieldName);
		} catch (Exception ex) {
			throw new XPTRuntimeException("Error during buildElement for MimeMultiPartExtended", ex);
		}
	}

	private MimeMultiPartExtended(String server, String database, String documentUNID, String fieldName) {
		super();
		m_Server = server;
		m_Database = database;
		m_DocumentUNID = documentUNID;
		m_FieldName = fieldName;
	}

	public void checkLoad() {
		if (m_Content != null) {
			return;
		}
	}

	@Override
	public String getContentAsText() {
		checkLoad();
		return m_Content.getContentAsText();
	}

	@Override
	public long getContentLength() {
		checkLoad();
		return m_Content.getContentLength();
	}

	@Override
	public String getHTML() {
		checkLoad();
		return m_Content.getHTML();

	}

	@Override
	public String getItemName() {
		checkLoad();
		return m_Content.getItemName();
	}

	@Override
	public long[] getMimeLengths() {
		checkLoad();
		return m_Content.getMimeLengths();

	}

	@Override
	public IMimePart[] getMimeParts() {
		checkLoad();
		return m_Content.getMimeParts();
	}

	@Override
	public String[] getMimeTypes() {
		checkLoad();
		return m_Content.getMimeTypes();
	}

	@Override
	public boolean isHTMLConverted() {
		checkLoad();
		return m_Content.isHTMLConverted();
	}

	@Override
	public boolean isSameAs(IMimeMultipart arg0) {
		checkLoad();
		return m_Content.isSameAs(arg0);

	}

	@Override
	public void setHTMLConverted(boolean arg0) {
		checkLoad();
		m_Content.setHTMLConverted(arg0);

	}

	public String getServer() {
		return m_Server;
	}

	public String getDatabase() {
		return m_Database;
	}

	public String getDocumentUNID() {
		return m_DocumentUNID;
	}

	public String getFieldName() {
		return m_FieldName;
	}

}
