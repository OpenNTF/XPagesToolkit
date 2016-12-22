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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RSSEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_Title;
	private String m_Link;
	private String m_Description;
	private List<String> m_Contents;
	private List<String> m_Authors;
	private List<String> m_Categories;
	private List<String> m_Links;
	private Date m_Created;
	private Date m_Updated;

	public String getTitle() {
		return m_Title;
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public String getDescription() {
		return m_Description;
	}

	public void setDescription(String description) {
		m_Description = description;
	}

	public String getLink() {
		return m_Link;
	}

	public void setLink(String link) {
		m_Link = link;
	}

	public List<String> getContents() {
		return m_Contents;
	}

	public void setContents(List<String> contents) {
		m_Contents = contents;
	}

	public List<String> getAuthors() {
		return m_Authors;
	}

	public void setAuthors(List<String> authors) {
		m_Authors = authors;
	}

	public List<String> getCategories() {
		return m_Categories;
	}

	public void setCategories(List<String> categories) {
		m_Categories = categories;
	}

	public List<String> getLinks() {
		return m_Links;
	}

	public void setLinks(List<String> links) {
		m_Links = links;
	}

	public Date getCreated() {
		return m_Created;
	}

	public void setCreated(Date created) {
		m_Created = created;
	}

	public Date getUpdated() {
		return m_Updated;
	}

	public void setUpdated(Date updated) {
		m_Updated = updated;
	}

	public String getContentsTXT() {
		return buildStringFromList(m_Contents);
	}

	public String getCategoriesTXT() {
		return buildStringFromList(m_Categories);
	}

	public String getAuthorsTXT() {
		return buildStringFromList(m_Authors);
	}

	public String getLinksTXT() {
		return buildStringFromList(m_Links);
	}

	private String buildStringFromList(List<String> lstCurrent) {
		StringBuilder sb = new StringBuilder("");
		if (lstCurrent == null ||lstCurrent.isEmpty()) {
			return "";
		}
		Iterator<String> itCurrent = lstCurrent.iterator();
		while (itCurrent.hasNext()) {
			sb.append(itCurrent.next());
			if (itCurrent.hasNext()) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
