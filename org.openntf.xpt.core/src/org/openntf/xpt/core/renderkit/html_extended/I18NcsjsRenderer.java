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
