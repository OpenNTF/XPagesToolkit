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
package org.openntf.xpt.oneui.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.oneui.component.UIWelcomebox;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.renderkit.FacesRenderer;
import com.ibm.xsp.util.FacesUtil;

public class WelcomeBoxRenderer extends FacesRenderer {

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		UIWelcomebox uiwc = (UIWelcomebox) component;
		if (!uiwc.isRendered()) {
			return;
		}
		ResponseWriter w = context.getResponseWriter();
		writeMainFrame(context, w, uiwc);
	}

	private void writeMainFrame(FacesContext context, ResponseWriter w, UIWelcomebox uiwc) throws IOException {
		String strStyle = uiwc.getStyle();
		String strStyleClass = uiwc.getStyleClass();
		String strID = uiwc.getClientId(context);
		if (StringUtil.isEmpty(strStyleClass)) {
			strStyleClass = "lotusWelcomeBox";
		}
		w.startElement("div", uiwc);
		if (!StringUtil.isEmpty(strStyle)) {
			w.writeAttribute("style", strStyle, null);
		}
		w.writeAttribute("class", strStyleClass, null);
		w.writeAttribute("id", strID, null);

		if (!uiwc.isCloseable()) {
			writeWelcomeBox(context, w, uiwc, false, strID);
		} else {
			if (uiwc.isClosed()) {
				writeWelcomeBoxClosed(context, w, uiwc, strID);
			} else {
				writeWelcomeBox(context, w, uiwc, true, strID);
			}
		}

		w.endElement("div");

	}

	private void writeWelcomeBoxClosed(FacesContext context, ResponseWriter w, UIWelcomebox uiwc, String strID) throws IOException {
		// TODO Auto-generated method stub

	}

	private void writeWelcomeBox(FacesContext context, ResponseWriter w, UIWelcomebox uiwc, boolean b, String strID) throws IOException {
		String strTitle = uiwc.getTitle();
		if (!StringUtil.isEmpty(strTitle)) {
			w.startElement("h2", null);
			w.writeText(strTitle, null);
			w.endElement("h2");
		}
		UIComponent content = uiwc.getFacet(UIWelcomebox.FACET_WELCOMETEXT);
		if (content != null) {
			w.startElement("p", null);
			FacesUtil.renderChildren(context, content);
			w.endElement("p");
		}
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
