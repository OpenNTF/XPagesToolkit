package org.openntf.xpt.demo.bo;

import java.io.Serializable;
import java.util.Date;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form = "BlogEntry", View = "lupBlogById", PrimaryFieldClass = String.class, PrimaryKeyField = "ID")
public class BlogEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName = "ID")
	private String m_ID;
	@DominoEntity(FieldName = "Created")
	private Date m_Created;
	@DominoEntity(FieldName = "Creator")
	private String m_Creator;
	@DominoEntity(FieldName = "Subject")
	private String m_Subject;
	@DominoEntity(FieldName = "Content", isNotesSummary = false)
	private String m_Content;

	public String getID() {
		return m_ID;
	}

	public void setID(String id) {
		m_ID = id;
	}

	public Date getCreated() {
		return m_Created;
	}

	public void setCreated(Date created) {
		m_Created = created;
	}

	public String getCreator() {
		return m_Creator;
	}

	public void setCreator(String creator) {
		m_Creator = creator;
	}

	public String getSubject() {
		return m_Subject;
	}

	public void setSubject(String subject) {
		m_Subject = subject;
	}

	public String getContent() {
		return m_Content;
	}

	public void setContent(String content) {
		m_Content = content;
	}

}
