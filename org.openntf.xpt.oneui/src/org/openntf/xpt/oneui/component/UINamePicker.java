package org.openntf.xpt.oneui.component;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.utils.ValueBindingSupport;
import org.openntf.xpt.oneui.kernel.NamePickerProcessor;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.component.FacesAjaxComponent;
import com.ibm.xsp.component.UIInputEx;
import com.ibm.xsp.util.HtmlUtil;

public class UINamePicker extends UIInputEx implements FacesAjaxComponent {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$

	private String m_Database;
	private String m_View;
	private String m_SearchQuery;
	private Boolean m_MultiValue;
	private String m_MultiValueSeparator;
	private String m_RefreshId;

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

	public boolean isMultiValue() {
		if (m_MultiValue != null) {
			return m_MultiValue;
		}
		ValueBinding vb = getValueBinding("multiValue"); //$NON-NLS-1$
		if (vb != null) {
			return (Boolean) vb.getValue(getFacesContext());
		} else {
			return true;
		}
	}

	public void setMultiValue(boolean multiValue) {
		m_MultiValue = multiValue;
	}

	public String getRefreshId() {
		return ValueBindingSupport.getValue(m_RefreshId, "refreshId", this, null, getFacesContext());
	}

	public void setRefreshId(String refreshId) {
		m_RefreshId = refreshId;
	}

	public String getMultiValueSeparator() {
		return ValueBindingSupport.getValue(m_MultiValueSeparator, "multiValueSeparator", this, null, getFacesContext());
	}

	public void setMultiValueSeparator(String multiValueSeparator) {
		m_MultiValueSeparator = multiValueSeparator;
	}

	@Override
	public boolean handles(FacesContext arg0) {
		System.out.println("test1");
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void processAjaxRequest(FacesContext context) throws IOException {
		
		
		
		 try
		    {
		      HttpServletResponse localHttpServletResponse = (HttpServletResponse)context.getExternalContext().getResponse();
		      localHttpServletResponse.setContentType("text/xml; charset=UTF-8");
		      localHttpServletResponse.setHeader("Cache-Control", "no-cache");
		      localHttpServletResponse.setStatus(200);

		      boolean bool = AjaxUtil.isRendering(context);
		      try {
		        AjaxUtil.setRendering(context, true);

		   //     ViewHandlerEx localViewHandlerEx = (ViewHandlerEx)context.getApplication().getViewHandler();
		   //     localViewHandlerEx.doInitRender(context);

		        ResponseWriter localResponseWriter = context.getResponseWriter();
		        try {
		        	/*
		        	StringBuilder b = new StringBuilder();
					b.append("<ul>"); // $NON-NLS-1$
					for (int i = 0; i < 5; i++) {

						b.append("<li>"); // $NON-NLS-1$

						// Note, use double-quotes instead of single-quotes for
						// attributes, so as to be XHTML-compliant.
						b.append("<span class=\"informal\">"); // $NON-NLS-1$
						b.append(TextUtil.toXMLString("Label" + i));
						b.append("</span>"); // $NON-NLS-1$
						b.append(TextUtil.toXMLString("Value" + i));
						b.append("</li>"); // $NON-NLS-1$

					}
					b.append("</ul>"); // $NON-NLS-1$
					*/
		        	StringBuilder b = NamePickerProcessor.INSTANCE.getTypeAhead(this, "test");
		            localResponseWriter.write(b.toString());
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
	
	
}
