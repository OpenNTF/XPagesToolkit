/*
 * © Copyright WebGate Consulting AG, 2013
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openntf.xpt.rss.XPTRSSActivator;

import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndPerson;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedReaderService {

	private static FeedReaderService m_Service;

	private FeedReaderService() {

	}

	public static FeedReaderService getInstance() {
		if (m_Service == null) {
			m_Service = new FeedReaderService();
		}
		return m_Service;
	}

	public List<RSSEntry> getAllEntriesFromURL(String strURL) {
		List<RSSEntry> lstRC = new ArrayList<RSSEntry>();
		Thread currentThread = Thread.currentThread();
		ClassLoader clCurrent = currentThread.getContextClassLoader();

		try {
			currentThread.setContextClassLoader(XPTRSSActivator.class.getClassLoader());

			URL feedUrl = new URL(strURL);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			lstRC = processFeed2List(feed);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			currentThread.setContextClassLoader(clCurrent);
		}
		return lstRC;
	}

	public RSSFeed getFeedFromURL(String strURL) {
		RSSFeed rssFeed = new RSSFeed();
		rssFeed.setURL(strURL);
		Thread currentThread = Thread.currentThread();
		ClassLoader clCurrent = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(XPTRSSActivator.class.getClassLoader());
			URL feedUrl = new URL(strURL);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			rssFeed.setAuthor(feed.getAuthor());
			rssFeed.setDescription(feed.getDescription());
			rssFeed.setTitle(feed.getTitle());
			if (feed.getImage() != null) {
				rssFeed.setImageURL(feed.getImage().getUrl());
			}
			rssFeed.setEntries(processFeed2List(feed));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			currentThread.setContextClassLoader(clCurrent);
		}
		return rssFeed;
	}

	private List<RSSEntry> processFeed2List(SyndFeed feed) {
		List<RSSEntry> lstRC = new ArrayList<RSSEntry>();
		for (Object synEntryObject : feed.getEntries()) {
			SyndEntry sEntry = (SyndEntry) synEntryObject;
			RSSEntry rssEntry = new RSSEntry();
			rssEntry.setTitle(sEntry.getTitle());
			rssEntry.setLink(sEntry.getLink());
			if (sEntry.getDescription() != null) {
				rssEntry.setDescription(sEntry.getDescription().getValue());
			} else {
				rssEntry.setDescription("");
			}
			rssEntry.setAuthors(buildStringList(sEntry.getAuthors()));
			rssEntry.setCategories(buildStringList(sEntry.getCategories()));
			rssEntry.setContents(buildStringList(sEntry.getContents()));
			rssEntry.setLinks(buildStringList(sEntry.getLinks()));
			rssEntry.setCreated(sEntry.getPublishedDate());
			rssEntry.setUpdated(sEntry.getUpdatedDate());
			lstRC.add(rssEntry);
		}
		return lstRC;
	}

	private List<String> buildStringList(List<?> lstConvert) {
		List<String> lstRC = new ArrayList<String>();
		if (lstConvert == null || lstConvert.size() == 0) {
			return lstRC;
		}
		for (Object objX : lstConvert) {
			if (objX instanceof SyndPerson) {
				lstRC.add(((SyndPerson) objX).getName());
			} else if (objX instanceof SyndContentImpl) {
				lstRC.add(((SyndContentImpl) objX).getValue());
			} else if (objX instanceof SyndCategoryImpl) {
				lstRC.add(((SyndCategoryImpl) objX).getName());
			} else {
				lstRC.add(objX.toString());
			}
		}
		return lstRC;
	}
}
