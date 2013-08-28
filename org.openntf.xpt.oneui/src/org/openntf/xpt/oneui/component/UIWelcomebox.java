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
package org.openntf.xpt.oneui.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.binding.MethodBindingEx;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.StateHolderUtil;

public class UIWelcomebox extends UIComponentBase {

	public static final String FACET_WELCOMETEXT = "welcomeText";//$NON-NLS-1$

	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uiwelcomebox"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "javax.faces.Panel"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uiwelcomebox"; //$NON-NLS-1$

	private String m_Title;
	private Boolean m_Closeable;
	private Boolean m_Closed;
	private String m_ShowBoxTitle;
	private String m_Style;
	private String m_StyleClass;

	private MethodBinding m_OnStateChange;

	public UIWelcomebox() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getTitle() {
		if (m_Title != null) {
			return m_Title;
		}
		ValueBinding vb = getValueBinding("title"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		} else {
			return null;
		}
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public boolean isCloseable() {
		if (m_Closeable != null) {
			return m_Closeable;
		}
		ValueBinding vb = getValueBinding("closeable"); //$NON-NLS-1$
		if (vb != null) {
			return (Boolean) vb.getValue(getFacesContext());
		} else {
			return false;
		}

	}

	public void setCloseable(boolean closeable) {
		m_Closeable = closeable;
	}

	public boolean isClosed() {
		if (m_Closed != null) {
			return m_Closed;
		}
		ValueBinding vb = getValueBinding("closed"); //$NON-NLS-1$
		if (vb != null) {
			return (Boolean) vb.getValue(getFacesContext());
		} else {
			return false;
		}

	}

	public void setClosed(boolean closed) {
		m_Closed = closed;
	}

	public String getShowBoxTitle() {
		if (m_ShowBoxTitle != null) {
			return m_ShowBoxTitle;
		}
		ValueBinding vb = getValueBinding("showBoxTitle"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		} else {
			return null;
		}

	}

	public void setShowBoxTitle(String showBoxTitle) {
		m_ShowBoxTitle = showBoxTitle;
	}

	public String getStyle() {
		if (m_Style != null) {
			return m_Style;
		}
		ValueBinding vb = getValueBinding("style"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		} else {
			return null;
		}

	}

	public void setStyle(String style) {
		m_Style = style;
	}

	public String getStyleClass() {
		if (m_Style != null) {
			return m_StyleClass;
		}
		ValueBinding vb = getValueBinding("styleClass"); //$NON-NLS-1$
		if (vb != null) {
			return (String) vb.getValue(getFacesContext());
		} else {
			return null;
		}

	}

	public void setStyleClass(String styleClass) {
		m_StyleClass = styleClass;
	}

	public MethodBinding getOnStateChange() {
		return m_OnStateChange;
	}

	public void setOnStateChange(MethodBinding onStateChange) {
		m_OnStateChange = onStateChange;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_Title = (String) values[1];
		m_Closeable = (Boolean) values[2];
		m_Closed = (Boolean) values[3];
		m_ShowBoxTitle = (String) values[4];
		m_Style = (String) values[5];
		m_StyleClass = (String) values[6];
		m_OnStateChange = StateHolderUtil.restoreMethodBinding(context, this, values[7]);
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[8];
		values[0] = super.saveState(context);
		values[1] = m_Title;
		values[2] = m_Closeable;
		values[3] = m_Closed;
		values[4] = m_ShowBoxTitle;
		values[5] = m_Style;
		values[6] = m_StyleClass;
		values[7] = StateHolderUtil.saveMethodBinding(context, m_OnStateChange);
		return values;
	}

	public boolean processOnStateChange(FacesContext context, boolean blOpen) {
		if (m_OnStateChange != null) {
			Object[] params = null;
			if (m_OnStateChange instanceof MethodBindingEx) {
				params = new Object[] { blOpen };
				((MethodBindingEx) m_OnStateChange).setComponent(this);
				((MethodBindingEx) m_OnStateChange).setParamNames(s_processOnStateChangeParam);
			}
			if (FacesUtil.isCancelled(m_OnStateChange.invoke(context, params))) {
				return false;
			}
			return true;
		}
		return true;
	}

	private static final String[] s_processOnStateChangeParam = { "isClosed" }; // $NON-NLS-1$
}
