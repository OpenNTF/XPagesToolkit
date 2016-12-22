/**
 * Copyright 2014, WebGate Consulting AG
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
package org.openntf.xpt.core.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.openntf.xpt.core.beans.XPTI18NBean;
import org.openntf.xpt.core.component.UII18Ncsjs;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIScriptCollector;
import com.ibm.xsp.renderkit.FacesRenderer;

public class I18NcsjsRenderer extends FacesRenderer {

	// Generate I18NClientSide Script

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!(component instanceof UII18Ncsjs)) {
			return;
		}
		UII18Ncsjs uics = (UII18Ncsjs) component;
		if (!uics.isRendered()) {
			return;
		}
		if (StringUtil.isEmpty(uics.getVarName())) {
			return;
		}

		String strJS = XPTI18NBean.get(context).getJSRepresentation(uics.getVarName(), uics.getLanguageForce());
		UIScriptCollector uic = UIScriptCollector.find();
		uic.addScript(strJS);

	}
}
