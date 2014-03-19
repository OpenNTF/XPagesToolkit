package org.openntf.xpt.core.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class UII18Ncsjs extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.core.component.uii18ncsjs"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.core.component.uii18ncsjs"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.core.component.uii18ncsjs"; //$NON-NLS-1$

	private String m_VarName;
	private String m_LanguageForce;

	public UII18Ncsjs() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getVarName() {
		return m_VarName;
	}

	public void setVarName(String varName) {
		m_VarName = varName;
	}

	public String getLanguageForce() {
		return m_LanguageForce;
	}

	public void setLanguageForce(String languageForce) {
		m_LanguageForce = languageForce;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_VarName = (String) values[1];
		m_LanguageForce = (String) values[2];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = m_VarName;
		values[2] = m_LanguageForce;
		return values;
	}

}
