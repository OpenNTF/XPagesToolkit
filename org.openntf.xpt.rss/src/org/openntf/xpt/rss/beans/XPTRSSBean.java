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
package org.openntf.xpt.rss.beans;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import javax.faces.context.FacesContext;

import org.openntf.xpt.rss.model.FeedReaderService;
import org.openntf.xpt.rss.model.RSSEntry;


public class XPTRSSBean {

	public static final String BEAN_NAME = "xptRssBean"; //$NON-NLS-1$

	public static XPTRSSBean get(FacesContext context) {
		XPTRSSBean bean = (XPTRSSBean) context.getApplication().getVariableResolver()
				.resolveVariable(context, BEAN_NAME);
		return bean;
	}

	public static XPTRSSBean get() {
		return get(FacesContext.getCurrentInstance());
	}

	public List<RSSEntry> getEntries(String strURL) {
		final String finURL = strURL;
		List<RSSEntry> lstRC = AccessController
				.doPrivileged(new PrivilegedAction<List<RSSEntry>>() {

					@Override
					public List<RSSEntry> run() {
						return FeedReaderService.getInstance()
								.getAllEntriesFromURL(finURL);
					}

				});
		return lstRC;
	}
}
