package org.openntf.xpt.rss.model;

import java.io.Serializable;
import java.util.List;

public class RSSFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_URL;
	private String m_ImageURL;
	private String m_Description;
	private String m_Title;
	private String m_Author;
	private List<RSSEntry> m_Entries;

	public String getImageURL() {
		return m_ImageURL;
	}

	public void setImageURL(String imageURL) {
		m_ImageURL = imageURL;
	}

	public String getDescription() {
		return m_Description;
	}

	public void setDescription(String description) {
		m_Description = description;
	}

	public String getTitle() {
		return m_Title;
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public String getAuthor() {
		return m_Author;
	}

	public void setAuthor(String author) {
		m_Author = author;
	}

	public List<RSSEntry> getEntries() {
		return m_Entries;
	}

	public void setEntries(List<RSSEntry> entries) {
		m_Entries = entries;
	}

	public String getURL() {
		return m_URL;
	}

	public void setURL(String uRL) {
		m_URL = uRL;
	}

}
