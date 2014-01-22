package org.openntf.xpt.oneui.component;

import javax.faces.el.ValueBinding;

import org.openntf.xpt.core.utils.ValueBindingSupport;

import com.ibm.xsp.component.UIInputEx;

public class UINamePicker extends UIInputEx{
	
	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uinamepicker"; //$NON-NLS-1$
	
	private String m_Database;
	private String m_View;
	private String m_SearchQuery;
	private Boolean m_MultiValue;
	private String m_MultiValueSeparator;
	private String m_RefreshId;
	
	//private String m_ReturnFieldValue;
	//private String m_ReturnFieldAlias;
	//private String m_SearchQueryAll;
	//private String[] m_DisplayFields;
	//@Formulas?
	
	public UINamePicker() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getDatabase() {
		
		/*if (m_Database != null) {
			return m_Database;
		}
		ValueBinding vb = getValueBinding("database"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		} else {
			return null;
		}
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



}
