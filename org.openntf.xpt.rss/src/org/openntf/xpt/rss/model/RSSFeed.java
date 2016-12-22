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
