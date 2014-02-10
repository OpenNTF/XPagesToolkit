package org.openntf.xpt.oneui.component;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Document;
import lotus.domino.Name;

import org.openntf.xpt.core.utils.ValueBindingSupport;
import org.openntf.xpt.oneui.kernel.NameEntry;
import org.openntf.xpt.oneui.kernel.NamePickerProcessor;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.binding.MethodBindingEx;
import com.ibm.xsp.component.FacesAjaxComponent;
import com.ibm.xsp.component.UIInputEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.util.HtmlUtil;
import com.ibm.xsp.util.StateHolderUtil;
import com.ibm.xsp.util.TypedUtil;

public class UINamePicker extends UIInputEx implements FacesAjaxComponent {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$

	private static final String[] VAR_METHOD = { "docName" };

	private String m_Database;
	private String m_View;
	private String m_SearchQuery;
	// private Boolean m_MultiValue;
	// private String m_MultiValueSeparator;
	private String m_RefreshId;
	private boolean m_DisplayLabel;

	private MethodBinding m_BuildLabel;
	private MethodBinding m_BuildValue;
	private MethodBinding m_BuildLine;

	// private IValuePickerData dataProvider;
	// private String m_ReturnFieldValue;
	// private String m_ReturnFieldAlias;
	// private String m_SearchQueryAll;
	// private String[] m_DisplayFields;
	// @Formulas?

	public UINamePicker() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getDatabase() {

		/*
		 * if (m_Database != null) { return m_Database; } ValueBinding vb =
		 * getValueBinding("database"); //$NON-NLS-1$ if (vb != null) { return
		 * (String) vb.getValue(getFacesContext()); } else { return null; }
		 */
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

	public String getSearchQuery() {
		return ValueBindingSupport.getValue(m_SearchQuery, "searchQuery", this, null, getFacesContext());
	}

	public void setSearchQuery(String searchQuery) {
		m_SearchQuery = searchQuery;
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
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[8];
		values[0] = super.saveState(context);
		values[1] = m_Database;
		values[2] = m_View;
		values[3] = m_RefreshId;
		values[4] = m_SearchQuery;
		values[5] = StateHolderUtil.saveMethodBinding(context, m_BuildLabel);
		values[6] = StateHolderUtil.saveMethodBinding(context, m_BuildLine);
		values[7] = StateHolderUtil.saveMethodBinding(context, m_BuildValue);

		return values;
	}

	@Override
	public boolean handles(FacesContext arg0) {
		return true;
	}

	@Override
	public void processAjaxRequest(FacesContext context) throws IOException {

		try {
			HttpServletResponse localHttpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
			localHttpServletResponse.setContentType("text/xml; charset=UTF-8");
			localHttpServletResponse.setHeader("Cache-Control", "no-cache");
			localHttpServletResponse.setStatus(200);

			boolean bool = AjaxUtil.isRendering(context);
			try {
				AjaxUtil.setRendering(context, true);

				// ViewHandlerEx localViewHandlerEx =
				// (ViewHandlerEx)context.getApplication().getViewHandler();
				// localViewHandlerEx.doInitRender(context);

				ResponseWriter localResponseWriter = context.getResponseWriter();
				try {
					/*
					 * StringBuilder b = new StringBuilder(); b.append("<ul>");
					 * // $NON-NLS-1$ for (int i = 0; i < 5; i++) {
					 * 
					 * b.append("<li>"); // $NON-NLS-1$
					 * 
					 * // Note, use double-quotes instead of single-quotes for
					 * // attributes, so as to be XHTML-compliant.
					 * b.append("<span class=\"informal\">"); // $NON-NLS-1$
					 * b.append(TextUtil.toXMLString("Label" + i));
					 * b.append("</span>"); // $NON-NLS-1$
					 * b.append(TextUtil.toXMLString("Value" + i));
					 * b.append("</li>"); // $NON-NLS-1$
					 * 
					 * } b.append("</ul>"); // $NON-NLS-1$
					 */
					Map<String, String> localMap = TypedUtil.getRequestParameterMap(context.getExternalContext());
					String strSearch = localMap.get("$$value");
					if (strSearch != null) {
						strSearch = strSearch.trim();
						String b = NamePickerProcessor.INSTANCE.getTypeAhead(this, strSearch);
						localResponseWriter.write(b);
					}
				} finally {
					localResponseWriter.endDocument();
					context.responseComplete();
				}
			} finally {
				AjaxUtil.setRendering(context, bool);
			}
		} catch (IOException localIOException) {
			throw new FacesExceptionEx(localIOException);
		}

	}

	/*
	 * public IValuePickerData getDataProvider() { return this.dataProvider; }
	 * 
	 * public void setDataProvider(IValuePickerData dataProvider) {
	 * this.dataProvider = dataProvider; }
	 */

	public void encodeBegin(FacesContext paramFacesContext) throws IOException {
		if (AjaxUtil.isAjaxPartialRefresh(paramFacesContext)) {
			String str1 = AjaxUtil.getAjaxMode(paramFacesContext);
			if (StringUtil.equals(str1, "typeahead")) {
				String str2 = getClientId(paramFacesContext);
				if (StringUtil.equals(AjaxUtil.getAjaxComponentId(paramFacesContext), str2)) {
					processAjaxRequest(paramFacesContext);
					HtmlUtil.storeEncodeParameter(paramFacesContext, this, "processAjaxRequest", Boolean.TRUE);
					return;
				}
			}
		}

		super.encodeBegin(paramFacesContext);
	}

	public void encodeEnd(FacesContext paramFacesContext) throws IOException {
		boolean bool = true;
		Object localObject = HtmlUtil.readEncodeParameter(paramFacesContext, this, "processAjaxRequest", bool);
		if (localObject == null) {
			super.encodeEnd(paramFacesContext);
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
			ex.printStackTrace();
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

		}
		return null;
	}

	private String getField(Document docSearch, String strFieldName) {
		try {
			return docSearch.getItemValueString(strFieldName);
		} catch (Exception ex) {
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
		StringBuffer sb = new StringBuffer(buildJSFunctionName());
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
}
