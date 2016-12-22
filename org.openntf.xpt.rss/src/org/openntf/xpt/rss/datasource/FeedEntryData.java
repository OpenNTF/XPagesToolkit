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
package org.openntf.xpt.rss.datasource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.openntf.xpt.rss.model.RSSEntry;

import com.ibm.xsp.model.ViewRowData;

public class FeedEntryData implements ViewRowData, Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> m_Properties;

	public static FeedEntryData buildFromRSSEntry(RSSEntry rssEntry) {
		FeedEntryData fed = new FeedEntryData();
		Map<String,Object> props = new HashMap<String, Object>();
		props.put("title", rssEntry.getTitle());
		props.put("link", rssEntry.getLink());
		props.put("description", rssEntry.getDescription());
		props.put("author", rssEntry.getAuthors().isEmpty() ? "" : rssEntry.getAuthors().get(0));
		props.put("authors", rssEntry.getAuthorsTXT());
		props.put("categories", rssEntry.getCategoriesTXT());
		props.put("contents", rssEntry.getContentsTXT());
		props.put("links", rssEntry.getLinksTXT());
		props.put("date", rssEntry.getCreated());
		props.put("update", rssEntry.getUpdated());
		fed.m_Properties = props;
		return fed;
		
	}
	
	@Override
	public ColumnInfo getColumnInfo(String arg0) {
		return null;
	}

	@Override
	public Object getColumnValue(String arg0) {
		return getValue(arg0);
	}

	@Override
	public String getOpenPageURL(String arg0, boolean arg1) {
		return (String)m_Properties.get("link");
	}

	@Override
	public Object getValue(String arg0) {
		if (m_Properties != null) {
			return m_Properties.get(""+arg0);
		}
		return null;
	}

	@Override
	public boolean isReadOnly(String arg0) {
		return true;
	}

	@Override
	public void setColumnValue(String arg0, Object arg1) {
		if (m_Properties == null) {
			m_Properties = new HashMap<String, Object>();
		}
		m_Properties.put(""+arg0, arg1);
		
	}

}
