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
package org.openntf.xpt.oneui.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.core.utils.logging.LoggerFactory;
import org.openntf.xpt.oneui.component.UIWelcomebox;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.renderkit.FacesRenderer;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.JavaScriptUtil;

public class WelcomeBoxRenderer extends FacesRenderer {

	private static final String CLOSEACTION_CLOSE = "_closeaction_close";
	private static final String CLOSEACTION_OPEN = "_closeaction_open";
	private static final String BLANK_GIF = "/oneuiv2.1/images/blank.gif";

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
			if (uiwc.isCloseable() && uiwc.isClosed()) {
				strStyleClass = "lotusRight";
			} else {
				strStyleClass = "lotusWelcomeBox";
			}
		}
		w.startElement("div", uiwc);
		if (!StringUtil.isEmpty(strStyle)) {
			w.writeAttribute("style", strStyle, null);
		}
		w.writeAttribute("class", strStyleClass, null);
		w.writeAttribute("id", strID, null);

		if (!uiwc.isCloseable()) {
			writeWelcomeBox(context, w, uiwc, strID);
		} else {
			if (uiwc.isClosed()) {
				writeWelcomeBoxClosed( w, uiwc, strID);
			} else {
				writeWelcomeBox(context, w, uiwc,  strID);
			}
		}

		w.endElement("div");

	}

	private void writeWelcomeBoxClosed( ResponseWriter w, UIWelcomebox uiwc, String strID) throws IOException {
		String strWCTitle = StringUtil.isEmpty(uiwc.getShowBoxTitle()) ? "show welcomeinfo" : uiwc.getShowBoxTitle();
		w.startElement("a", null);
		w.writeURIAttribute("href", "javascript:;", null);
		w.writeAttribute("id", strID + CLOSEACTION_OPEN, null);
		w.writeAttribute("class", "lotusAction", null);
		w.writeText(strWCTitle, null);
		w.endElement("a");
		setupSubmitOnClick( strID, strID + CLOSEACTION_OPEN);
	}

	private void writeWelcomeBox(FacesContext context, ResponseWriter w, UIWelcomebox uiwc, String strID) throws IOException {
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
		if (uiwc.isCloseable()) {
			w.startElement("a", null);
			w.writeURIAttribute("href", "javascript:;", null);
			w.writeAttribute("id", strID + CLOSEACTION_CLOSE, null);
			w.writeAttribute("class", "lotusBtnImg lotusClose", null);
			w.startElement("img", null);
			w.writeURIAttribute("src", BLANK_GIF, null);
			w.writeAttribute("aria-label", "close button", null);
			w.writeAttribute("alt", "", null);
			w.endElement("img");
			w.startElement("span", null);
			w.writeAttribute("class", "lotusAltText", null);
			w.writeText("X", null);
			w.endElement("span");
			w.endElement("a");
			setupSubmitOnClick(strID, strID + CLOSEACTION_CLOSE);
		}
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//No children do encode
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		//No encodeEnd needed, because of no children
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	protected void setupSubmitOnClick( String welcomeBoxID, String sourceId) throws IOException {
		String execId = null;
		String refreshId = welcomeBoxID;
		final String event = "onclick"; // $NON-NLS-1$
		StringBuilder buff = new StringBuilder();
		JavaScriptUtil.appendAttachPartialRefreshEvent(buff, sourceId, sourceId, execId, event,
		/* clientSideScriptName */null,
		/* immediate */JavaScriptUtil.VALIDATION_NONE,
		/* refreshId */refreshId,
		/* onstart getOnStart(pager) */"",
		/* oncomplete getOnComplete(pager) */"",
		/* onerror getOnError(pager) */"");
		String script = buff.toString();

		// Add the script block we just generated.
		JavaScriptUtil.addScriptOnLoad(script);
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (component instanceof UIWelcomebox) {
			UIWelcomebox uiwc = (UIWelcomebox) component;
			String currentClientId = component.getClientId(context);
			String hiddenValue = FacesUtil.getHiddenFieldValue(context);
			LoggerFactory.logInfo(getClass(),"currentClientID = " + currentClientId,null);
			LoggerFactory.logInfo(getClass(),"hiddenValue =" + hiddenValue, null);
			if (StringUtil.isNotEmpty(hiddenValue) && hiddenValue.startsWith(currentClientId + CLOSEACTION_OPEN)) {
				uiwc.setClosed(false);
				uiwc.processOnStateChange(context, false);
			}
			if (StringUtil.isNotEmpty(hiddenValue) && hiddenValue.startsWith(currentClientId + CLOSEACTION_CLOSE)) {
				uiwc.setClosed(true);
				uiwc.processOnStateChange(context, true);
			}
		}
		super.decode(context, component);
	}
}
