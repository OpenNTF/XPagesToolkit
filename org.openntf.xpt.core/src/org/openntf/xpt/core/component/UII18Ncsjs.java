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

package org.openntf.xpt.core.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class UII18Ncsjs extends UIComponentBase {

	public static final String COMPONENT_TYPE_FAMILY_RENDERER = "org.openntf.xpt.core.component.uii18ncsjs"; //$NON-NLS-1$

	private String m_VarName;
	private String m_LanguageForce;

	public UII18Ncsjs() {
		setRendererType(COMPONENT_TYPE_FAMILY_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_TYPE_FAMILY_RENDERER;
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
