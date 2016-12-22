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

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.model.AbstractDataSource;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.model.ModelDataSource;

public class RSSDataSource extends AbstractDataSource implements
		ModelDataSource {

	private String m_FeedURL;

	@Override
	protected String composeUniqueId() {
		StringBuilder sb = new StringBuilder();
		sb.append("FEED|");
		String strPrefix = getRequestParamPrefix();
		if (!StringUtil.isEmpty(strPrefix)) {
			sb.append(strPrefix);
			sb.append("|");
		}
		sb.append(getFeedURL());
		return sb.toString();
	}

	@Override
	public Object getDataObject() {
		return ((RSSDataContainer) getDataContainer()).getData();
	}

	@Override
	public boolean isReadonly() {
		return true;
	}

	@Override
	public DataContainer load(FacesContext arg0) throws IOException {
		return new RSSDataContainer(getBeanId(), getUniqueId(), getFeedURL());
	}

	@Override
	public void readRequestParams(FacesContext arg0,
			Map<String, Object> requestMap) {
		String strFeed = (String) requestMap.get(prefixRequestParam("feed"));
		if (!StringUtil.isEmpty(strFeed)) {
			m_FeedURL = strFeed;
		}
	}

	@Override
	public boolean save(FacesContext arg0, DataContainer arg1)
			throws FacesExceptionEx {
		return false;
	}

	public String getFeedURL() {
		if (m_FeedURL != null) {
			return m_FeedURL;
		}
		ValueBinding vb = getValueBinding("feedURL");
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		}
		return null;
	}

	public void setFeedURL(String feedURL) {
		m_FeedURL = feedURL;
	}

	// SAVE and RESTOR of Datas
	@Override
	public Object saveState(FacesContext arg0) {
		Object[] state = new Object[2];
		state[0] = super.saveState(arg0);
		state[1] = m_FeedURL;
		return state;
	}

	@Override
	public void restoreState(FacesContext arg0, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(arg0, values[0]);
		m_FeedURL = (String) values[1];
	}

	@Override
	public DataModel getDataModel() {
		return (DataModel)((RSSDataContainer)getDataContainer()).getData();
	}

	
}
