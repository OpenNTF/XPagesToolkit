package org.openntf.xpt.core.mime;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.RichTextItem;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.http.IMimeMultipart;
import com.ibm.xsp.http.IMimePart;
import com.ibm.xsp.http.MimeMultipart;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.model.domino.wrapped.DominoRichTextItem;

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
		try {
			Database ndbCurrent = NotesContext.getCurrentUnchecked().getCurrentSession().getDatabase(m_Server, m_Database);
			if (ndbCurrent == null) {
				throw new XPTRuntimeException("Database "+ m_Server +"!!"+m_Database +" not accessable");
			}
			Document docCurrent = ndbCurrent.getDocumentByUNID(m_DocumentUNID);
			if (docCurrent == null) {
				throw new XPTRuntimeException("Document "+ m_Server +"!!"+m_Database +"/"+ m_DocumentUNID+" not accessable");
				
			}
			MIMEEntity mimeCurrent = docCurrent.getMIMEEntity(m_FieldName);
			if (mimeCurrent == null && !docCurrent.hasItem(m_FieldName)) {
				m_Content = MimeMultipart.fromHTML("");
				return;
			}
			if (mimeCurrent == null && docCurrent.hasItem(m_FieldName)) {
				if (docCurrent.getFirstItem(m_FieldName) != null) {
					Item itField = docCurrent.getFirstItem(m_FieldName);
					if (itField.getType() != 1) {
						m_Content = MimeMultipart.fromHTML(docCurrent.getItemValueString(m_FieldName));
					} else {
						RichTextItem rti = (RichTextItem) itField;
						if (rti != null) {
							DominoDocument dd = new DominoDocument();
							dd.setDocument(docCurrent);
							DominoRichTextItem drtCurrent = new DominoRichTextItem(dd, rti);
							m_Content = MimeMultipart.fromHTML(drtCurrent.getHTML());
						}
					}
					itField.recycle();
				}
				docCurrent.recycle();
				ndbCurrent.recycle();
			}
			processMime(mimeCurrent);
		} catch (Exception ex) {
			LoggerFactory.logError(this.getClass(), "Error druing checkLoad()", ex);
			throw new XPTRuntimeException("General Error with: Database "+ m_Server +"!!"+m_Database +" / Field: "+ m_FieldName +" / "+m_DocumentUNID);
		}
	}

	private void processMime(MIMEEntity mimeCurrent) {
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
