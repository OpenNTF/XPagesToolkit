package org.openntf.xpt.oneui.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.openntf.xpt.core.utils.ValueBindingSupport;

import com.ibm.xsp.util.StateHolderUtil;

public class UITips extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uitips"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.oneui.component.uitips"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uitips"; //$NON-NLS-1$

	private String m_Title;
	private String m_Text;
	private String m_Style;
	private String m_StyleClass;

	public UITips() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getTitle() {
		return ValueBindingSupport.getValue(m_Title, "title", this, null, getFacesContext());
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public String getText() {
		return ValueBindingSupport.getValue(m_Text, "text", this, null, getFacesContext());
	}

	public void setText(String text) {
		m_Text = text;
	}

	public String getStyle() {
		return ValueBindingSupport.getValue(m_Style, "style", this, null, getFacesContext());
	}

	public void setStyle(String style) {
		m_Style = style;
	}

	public String getStyleClass() {
		return ValueBindingSupport.getValue(m_StyleClass, "styleClass", this, null, getFacesContext());
	}

	public void setStyleClass(String styleClass) {
		m_StyleClass = styleClass;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_Title = (String) values[1];
		m_Text = (String) values[2];
		m_Style = (String) values[3];
		m_StyleClass = (String) values[4];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = m_Title;
		values[2] = m_Text;
		values[3] = m_Style;
		values[4] = m_StyleClass;

		return values;
	}
}
