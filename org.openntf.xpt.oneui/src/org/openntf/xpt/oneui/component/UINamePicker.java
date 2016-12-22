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
package org.openntf.xpt.oneui.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;

import org.openntf.xpt.core.json.JSONService;
import org.openntf.xpt.core.utils.ErrorJSONBuilder;
import org.openntf.xpt.core.utils.ValueBindingSupport;
import org.openntf.xpt.core.utils.logging.LoggerFactory;
import org.openntf.xpt.oneui.kernel.INamePickerValueService;
import org.openntf.xpt.oneui.kernel.JsonResult;
import org.openntf.xpt.oneui.kernel.NameEntry;
import org.openntf.xpt.oneui.kernel.NamePickerProcessor;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.binding.MethodBindingEx;
import com.ibm.xsp.component.FacesAjaxComponent;
import com.ibm.xsp.component.UIInputEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.util.ManagedBeanUtil;
import com.ibm.xsp.util.StateHolderUtil;
import com.ibm.xsp.util.TypedUtil;
import com.ibm.xsp.webapp.XspHttpServletResponse;

public class UINamePicker extends UIInputEx implements FacesAjaxComponent {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$

	private static final String[] VAR_METHOD = { "docName" };

	private String m_Database;
	private String m_View;
	private String m_LookupView;
	private String m_SearchQuery;
	private String m_RefreshId;
	private boolean m_DisplayLabel;

	private MethodBinding m_BuildLabel;
	private MethodBinding m_BuildValue;
	private MethodBinding m_BuildLine;

	private Boolean m_ReadOnly;

	private String m_NameValueBean;

	public UINamePicker() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getDatabase() {

		return ValueBindingSupport.getValue(m_Database, "database", this, null, getFacesContext());
	}

	public void setDatabase(String database) {
		m_Database = database;
	}

	public String getView() {
		return ValueBindingSupport.getValue(m_View, "view", this, null, getFacesContext());
	}

	public void setView(String view) {
		m_View = view;
	}

	public String getLookupView() {
		return m_LookupView;
	}

	public void setLookupView(String lookupView) {
		m_LookupView = lookupView;
	}

	public String getSearchQuery() {
		return ValueBindingSupport.getValue(m_SearchQuery, "searchQuery", this, null, getFacesContext());
	}

	public void setSearchQuery(String searchQuery) {
		m_SearchQuery = searchQuery;
	}

	public String getNameValueBean() {
		return ValueBindingSupport.getValue(m_NameValueBean, "nameValueBean", this, null, getFacesContext());
	}

	public void setNameValueBean(String nameValueBean) {
		m_NameValueBean = nameValueBean;
	}

	public boolean isDisplayLabel() {
		return m_DisplayLabel;
	}

	public void setDisplayLabel(boolean displayLabel) {
		m_DisplayLabel = displayLabel;
	}

	public String getRefreshId() {
		return ValueBindingSupport.getValue(m_RefreshId, "refreshId", this, null, getFacesContext());
	}

	public void setRefreshId(String refreshId) {
		m_RefreshId = refreshId;
	}

	public MethodBinding getBuildLabel() {
		return m_BuildLabel;
	}

	public void setBuildLabel(MethodBinding buildLabel) {
		m_BuildLabel = buildLabel;
	}

	public MethodBinding getBuildValue() {
		return m_BuildValue;
	}

	public void setBuildValue(MethodBinding buildValue) {
		m_BuildValue = buildValue;
	}

	public MethodBinding getBuildLine() {
		return m_BuildLine;
	}

	public void setBuildLine(MethodBinding buildLine) {
		m_BuildLine = buildLine;
	}

	public boolean isReadOnly() {
		return ValueBindingSupport.getValue(m_ReadOnly, "readOnly", this, Boolean.FALSE, FacesContext.getCurrentInstance());
	}

	public void setReadOnly(boolean readOnly) {
		m_ReadOnly = readOnly;
	}

	// This is to maintain the compatibility with JSF
	// We add this pseudo property as it can be used by the readonly renderkit
	public boolean isReadonly() {
		return isReadOnly();
	}

	public void setReadonly(boolean readOnly) {
		setReadOnly(readOnly);
	}

	// SAVE & RESTORE of the Values
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_Database = (String) values[1];
		m_View = (String) values[2];
		m_RefreshId = (String) values[3];
		m_SearchQuery = (String) values[4];
		m_BuildLabel = StateHolderUtil.restoreMethodBinding(context, this, values[5]);
		m_BuildLine = StateHolderUtil.restoreMethodBinding(context, this, values[6]);
		m_BuildValue = StateHolderUtil.restoreMethodBinding(context, this, values[7]);
		m_LookupView = (String) values[8];
		m_NameValueBean = (String) values[9];
		m_DisplayLabel = (Boolean) values[10];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[11];
		values[0] = super.saveState(context);
		values[1] = m_Database;
		values[2] = m_View;
		values[3] = m_RefreshId;
		values[4] = m_SearchQuery;
		values[5] = StateHolderUtil.saveMethodBinding(context, m_BuildLabel);
		values[6] = StateHolderUtil.saveMethodBinding(context, m_BuildLine);
		values[7] = StateHolderUtil.saveMethodBinding(context, m_BuildValue);
		values[8] = m_LookupView;
		values[9] = m_NameValueBean;
		values[10] = m_DisplayLabel;
		return values;
	}

	@Override
	public boolean handles(FacesContext arg0) {
		return true;
	}

	@Override
	public void processAjaxRequest(FacesContext context) throws IOException {

		HttpServletResponse httpResponse = (HttpServletResponse) context.getExternalContext().getResponse();

		if (httpResponse instanceof XspHttpServletResponse) {
			XspHttpServletResponse r = (XspHttpServletResponse) httpResponse;
			r.setCommitted(true);
			httpResponse = r.getDelegate();
		}
		try {
			httpResponse.setContentType("text/json");
			httpResponse.setCharacterEncoding("utf-8");

			JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
			Map<String, String> localMap = TypedUtil.getRequestParameterMap(context.getExternalContext());
			String strSearch = localMap.get("$$value");
			List<NameEntry> lstEntries = new ArrayList<NameEntry>();
			if (strSearch != null) {
				lstEntries = searcheNameValues(context, strSearch);
			}
			JsonResult jsResult = JsonResult.generateOKResult(lstEntries);
			JSONService.getInstance().process2JSON(jsWriter, jsResult);
			jsWriter.close();
		} catch (Exception e) {
			ErrorJSONBuilder.getInstance().processError2JSON(httpResponse, 9999, "Error during parsing!", e);
		}
	}

	private List<NameEntry> searcheNameValues(FacesContext context, String strSearch) throws NotesException {
		List<NameEntry> lstEntries;
		INamePickerValueService service = getNamePickerValueService(context);
		lstEntries = service.getTypeAheadValues(this, strSearch);
		return lstEntries;
	}

	public INamePickerValueService getNamePickerValueService(FacesContext context) {
		String nameValueBeanName = getNameValueBean();
		if (nameValueBeanName != null) {
			Object nvBean = ManagedBeanUtil.getBean(context, getNameValueBean());
			if (!(nvBean instanceof INamePickerValueService)) {
				throw new FacesExceptionEx(null, "Bean {0} is not a INamePickerValueService", nameValueBeanName);
			}
			return (INamePickerValueService) nvBean;
		} else {
			return NamePickerProcessor.INSTANCE;
		}

	}

	public String getDisplayLableValue(Document docSearch) {
		String strLabel = "";

		try {
			String strDbPath = docSearch.getParentDatabase().getServer() + "!!" + docSearch.getParentDatabase().getFilePath();
			DominoDocument dDoc = DominoDocument.wrap(strDbPath, docSearch, "", "", false, "", "");

			Object[] objExec = { dDoc };
			strLabel = computeValueMB(m_BuildLabel, objExec);
			if (strLabel == null) {
				strLabel = getField(docSearch, "InternetAddress");
			}
		} catch (Exception ex) {
			LoggerFactory.logError(getClass(), "getDisplayLabelValue", ex);
		}
		return strLabel;
	}

	public NameEntry getDocumentEntryRepresentation(Document docSearch) {
		try {

			String strDbPath = docSearch.getParentDatabase().getServer() + "!!" + docSearch.getParentDatabase().getFilePath();
			DominoDocument dDoc = DominoDocument.wrap(strDbPath, docSearch, "", "", false, "", "");

			Object[] objExec = { dDoc };
			String strLine = computeValueMB(m_BuildLine, objExec);
			if (strLine == null) {
				strLine = getField(docSearch, "InternetAddress");
			}
			String strValue = computeValueMB(m_BuildValue, objExec);
			if (strValue == null) {
				strValue = getField(docSearch, "FullName");
				Name non = docSearch.getParentDatabase().getParent().createName(strValue);
				strValue = non.getAbbreviated();
				non.recycle();
			}

			String strLabel = computeValueMB(m_BuildLabel, objExec);
			if (strLabel == null) {
				strLabel = getField(docSearch, "InternetAddress");
			}
			if (StringUtil.isEmpty(strValue)) {
				return null;
			}
			if (StringUtil.isEmpty(strLine)) {
				if (StringUtil.isEmpty(strLabel)) {
					strLine = strValue;
					strLabel = strValue;
				} else {
					strLine = strLabel;
				}
			}
			return new NameEntry(strValue, strLabel, strLine);

		} catch (Exception ex) {
			LoggerFactory.logError(getClass(), "General Error", ex);

		}
		return null;
	}

	private String getField(Document docSearch, String strFieldName) {
		try {
			return docSearch.getItemValueString(strFieldName);
		} catch (Exception ex) {
			LoggerFactory.logError(getClass(), "getField", ex);
			return "";
		}
	}

	private String computeValueMB(MethodBinding mb, Object[] objExec) {
		String strRC = null;
		if (mb != null) {
			if (mb instanceof MethodBindingEx) {
				((MethodBindingEx) mb).setComponent(this);
				((MethodBindingEx) mb).setParamNames(VAR_METHOD);
			}
			Object objRC = mb.invoke(getFacesContext(), objExec);
			strRC = "" + objRC;
		}
		return strRC;
	}

	public String buildJSFunctionCall(NameEntry nam) {
		StringBuilder sb = new StringBuilder(buildJSFunctionName());
		sb.append("('");
		sb.append(nam.getLabel());
		sb.append("','");
		sb.append(nam.getValue());
		sb.append("');");
		return sb.toString();
	}

	public String buildJSFunctionName() {
		String strID = getClientId(getFacesContext());
		return "addName_" + ExtLibUtil.encodeJSFunctionName(strID);
	}

	public String buildFTSearch(String strSearch) {
		String strPattern = getSearchQuery();
		if (StringUtil.isEmpty(strPattern)) {
			return null;
		}
		return strPattern.replace("###VALUE###", strSearch);
	}
}
