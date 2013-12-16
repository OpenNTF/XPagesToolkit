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
package org.openntf.xpt.agents.component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.XPageAgentEntry;
import org.openntf.xpt.agents.XPageAgentRegistry;
import org.openntf.xpt.agents.beans.XPTAgentBean;
import org.openntf.xpt.agents.master.ApplicationStatus;
import org.openntf.xpt.agents.master.ExecutionUserProperties;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.application.UniqueViewIdManager;
import com.ibm.xsp.component.FacesAjaxComponent;
import com.ibm.xsp.util.StateHolderUtil;
import com.ibm.xsp.webapp.XspHttpServletResponse;

public class UIAgentList extends UIComponentBase implements FacesAjaxComponent {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.agents.component.uiagentlist"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.agents.component.uiagentlist"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.agents.component.uiagentlist"; //$NON-NLS-1$

	public static final String SORT_NAME = "NAME";
	public static final String SORT_ALIAS = "ALIAS";
	public static final String SORT_TYPE = "TYPE";
	public static final String SORT_EXECUTION = "EXECUTION";
	public static final String SORT_ACTIVE = "ACTIVE";

	private List<UIAgentEntry> m_Entries;
	private String m_Sort = SORT_NAME + "_ASC";

	public UIAgentList() {
		super();
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public List<UIAgentEntry> getAllEntries() {
		if (m_Entries == null) {
			m_Entries = new ArrayList<UIAgentEntry>();
			for (XPageAgentEntry age : XPageAgentRegistry.getInstance().getAllAgents()) {
				m_Entries.add(new UIAgentEntry(age));
			}
			doSort();
		}
		return m_Entries;
	}

	public String getSort() {
		return m_Sort;
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = StateHolderUtil.saveList(context, getAllEntries());
		values[2] = m_Sort;
		return values;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_Entries = StateHolderUtil.restoreList(context, this, values[1]);
		m_Sort = (String) values[2];
	}

	// -- AJAX Handling --//
	@Override
	public boolean handles(FacesContext arg0) {
		return false;
	}

	@Override
	public void processAjaxRequest(FacesContext context) throws IOException {

		HttpServletResponse httpResponse = (HttpServletResponse) context.getExternalContext().getResponse();
		HttpServletRequest httpRequest = (HttpServletRequest) context.getExternalContext().getRequest();

		// Disable the XPages response buffer as this will collide with the
		// engine one
		// We mark it as committed and use its delegate instead
		// Logger logCurrent =
		// LoggerFactory.getLogger(this.getClass().getCanonicalName());
		if (httpResponse instanceof XspHttpServletResponse) {
			XspHttpServletResponse r = (XspHttpServletResponse) httpResponse;
			r.setCommitted(true);
			httpResponse = r.getDelegate();
		}
		JsonJavaObject json = null;
		JsonJavaFactory factory = JsonJavaFactory.instanceEx;
		try {
			httpResponse.setContentType("text/json");
			httpResponse.setCharacterEncoding("utf-8");
			json = (JsonJavaObject) JsonParser.fromJson(factory, httpRequest.getReader());
			String strMethod = json.getString("method");
			if ("registerLogin".equals(strMethod)) {
				JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
				String strUserName = json.getString("user");
				String strPassword = json.getString("password");
				ExecutionUserProperties exp = XPTAgentBean.get(context).registerApplication2Master(strUserName, strPassword);

				jsWriter.startObject();
				jsWriter.startProperty("status");
				jsWriter.outStringLiteral(exp.isLoggedIn() ? "ok" : "failed");
				jsWriter.endProperty();
				jsWriter.startProperty("username");
				jsWriter.outStringLiteral(exp.getUserName());
				jsWriter.endProperty();
				jsWriter.endObject();
				jsWriter.close();
				return;
			}

			if ("unregister".equals(strMethod)) {
				JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
				boolean blREsp = XPTAgentBean.get(context).unregisterApplication();

				jsWriter.startObject();
				jsWriter.startProperty("status");
				jsWriter.outStringLiteral(blREsp ? "ok" : "failed");
				jsWriter.endProperty();
				jsWriter.endObject();
				jsWriter.close();
				return;
			}

			if ("sort".equals(strMethod)) {
				String strProperty = json.getString("prop");
				if ((strProperty + "_ASC").equals(m_Sort + "_ASC")) {
					m_Sort = strProperty + "_DES";
				} else {
					m_Sort = strProperty + "_ASC";
				}
				doSort();
				JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
				jsWriter.startObject();
				jsWriter.startProperty("status");
				jsWriter.outStringLiteral("ok");
				jsWriter.endProperty();
				jsWriter.endObject();
				jsWriter.close();

				return;
			}
			JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
			jsWriter.startObject();
			jsWriter.startProperty("status");
			jsWriter.outStringLiteral("error");
			jsWriter.endProperty();
			jsWriter.startProperty("error");
			jsWriter.outStringLiteral("method not found ->" + strMethod);
			jsWriter.endProperty();
			jsWriter.endObject();
			jsWriter.close();

		} catch (Exception e) {
			try {
				JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
				jsWriter.startObject();
				jsWriter.startProperty("status");
				jsWriter.outStringLiteral("error");
				jsWriter.endProperty();
				jsWriter.startProperty("error");
				StringWriter srw = new StringWriter();
				PrintWriter pw = new PrintWriter(srw);
				e.printStackTrace(pw);
				jsWriter.outStringLiteral(srw.toString());
				jsWriter.endProperty();
				jsWriter.endObject();
				jsWriter.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				httpResponse.getWriter().flush();
			}
		}
	}

	public ApplicationStatus getApplicationStatus(FacesContext context) {
		return XPTAgentBean.get(context).getApplicationStatus();
	}

	public boolean unregisterApplicaiton(FacesContext context) {
		return XPTAgentBean.get(context).unregisterApplication();
	}

	private void doSort() {
		UIAgentEntryComparator comp = new UIAgentEntryComparator(m_Sort);
		Collections.sort(m_Entries, comp);
	}

	public String getUrl(FacesContext context) {

		ExternalContext externalContext = context.getExternalContext();
		String contextPath = externalContext.getRequestContextPath();
		String servletPath = externalContext.getRequestServletPath();

		StringBuilder bURL = new StringBuilder();
		bURL.append(contextPath);
		bURL.append(servletPath);

		boolean hasQ = false;

		// Compose the query string
		String vid = UniqueViewIdManager.getUniqueViewId(context.getViewRoot());
		if (StringUtil.isNotEmpty(vid)) {
			bURL.append((hasQ ? "&" : "?") + AjaxUtil.AJAX_VIEWID + "=" + vid);
			hasQ = true;
		}
		// If not path info was specified,use the component ajax id
		String axTarget = getClientId(context);
		if (StringUtil.isNotEmpty(axTarget)) {
			bURL.append((hasQ ? "&" : "?") + AjaxUtil.AJAX_AXTARGET + "=" + axTarget);
			hasQ = true;
		}

		return bURL.toString();
	}
}
