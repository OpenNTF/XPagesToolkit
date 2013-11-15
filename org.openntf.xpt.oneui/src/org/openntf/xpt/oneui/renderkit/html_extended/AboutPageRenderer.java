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
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.oneui.component.UIAboutBlock;
import org.openntf.xpt.oneui.component.UIAboutPage;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIScriptCollector;
import com.ibm.xsp.renderkit.FacesRenderer;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;
import com.ibm.xsp.util.FacesUtil;

public class AboutPageRenderer extends FacesRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		System.out.println("DECODE");
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		UIAboutPage uip = (UIAboutPage) component;
		if (!uip.isRendered()) {
			return;
		}
		String strBodyChange = "document.getElementsByTagName(\"body\")[0].className += \" lotusAbout\"";
		UIScriptCollector sc = UIScriptCollector.find();
		sc.addScriptOnLoad(strBodyChange);

		ResponseWriter writer = context.getResponseWriter();
		writeAboutBox(context, writer, uip);
		if (uip.getLeftColumnBlocks() != null && uip.getLeftColumnBlocks().size() > 0) {
			writeColumnBlocks(context, writer, uip.getLeftColumnBlocks(), true);
		}
		if (uip.getRightColumnBlocks() != null && uip.getRightColumnBlocks().size() > 0) {
			writeColumnBlocks(context, writer, uip.getRightColumnBlocks(), false);
		}
	}

	private void writeColumnBlocks(FacesContext context, ResponseWriter writer, List<UIAboutBlock> columnBlocks, boolean isLeft) throws IOException {
		String strClass = isLeft ? "lotusContentColOne" : "lotusContentColTwo";

		writer.startElement("div", null);
		writer.writeAttribute("class", strClass, null);

		for (UIAboutBlock block : columnBlocks) {
			writer.startElement("h2", null);
			writer.writeText(block.getTitle(), null);
			writer.endElement("h2");
			writer.startElement("p", null);
			writer.writeText(block.getText(), null);
			writer.endElement("p");
			if (!StringUtil.isEmpty(block.getLink())) {
				writer.startElement("p", null);
				writer.startElement("a", null);
				writer.writeAttribute("class", "lotusAction", null);
				writer.writeURIAttribute("href", block.getLink(), null);
				if (!StringUtil.isEmpty(block.getLinkTitle())) {
					writer.writeText(block.getLinkTitle(), null);
				} else {
					writer.writeText(block.getLink(), null);
				}
				writer.endElement("a");
				writer.endElement("p");
			}
		}
		writer.endElement("div");
	}

	private void writeAboutBox(FacesContext context, ResponseWriter writer, UIAboutPage uip) throws IOException {
		String strID = uip.getClientId(context);
		String strStyleClass = uip.getStyleClass();
		String strStyle = uip.getStyle();
		String strTitle = uip.getTitle();
		String strMarketingClaim = uip.getMarketingClaim();
		String strText = uip.getText();
		String strApplicationLog = uip.getApplicationLogo();
		if (StringUtil.isEmpty(strStyleClass)) {
			strStyleClass = "lotusAboutBox";
		}
		writer.startElement("div", uip);
		writer.writeAttribute("id", strID, null);
		writer.writeAttribute("class", strStyleClass, null);
		if (!StringUtil.isEmpty(strStyle)) {
			writer.writeAttribute("style", strStyle, null);
		}
		writeApplicationLogo(context, writer, uip, strApplicationLog);
		writeAboutText(context, writer, uip, strTitle, strMarketingClaim, strText);
		

		writer.endElement("div");
	}

	private void writeApplicationLogo(FacesContext context, ResponseWriter writer, UIAboutPage uip, String strApplicationLog) throws IOException {
		if (StringUtil.isNotEmpty(strApplicationLog)) {
			String imgSrc = HtmlRendererUtil.getImageURL(context, strApplicationLog);
			writer.startElement("img", uip); // $NON-NLS-1$
			writer.writeURIAttribute("src", imgSrc, null); // $NON-NLS-1$
			writer.writeAttribute("alt", "ApplicationLogo", null); // $NON-NLS-1$
			writer.endElement("img"); // $NON-NLS-1$
		}
	}

	private void writeAboutText(FacesContext context, ResponseWriter writer, UIAboutPage uip, String strTitle, String strMarketingClaim, String strText)
			throws IOException {
		writer.startElement("div", null);
		writer.writeAttribute("class", "lotusAboutText", null);

		if (!StringUtil.isEmpty(strTitle)) {
			writer.startElement("h1", null);
			writer.writeText(strTitle, null);
			writer.endElement("h1");
		}
		if (!StringUtil.isEmpty(strMarketingClaim)) {
			writer.startElement("h3", null);
			writer.writeText(strMarketingClaim, null);
			writer.endElement("h3");
		}

		if (!StringUtil.isEmpty(strText)) {
			writer.startElement("p", null);
			writer.writeText(strText, null);
			writer.endElement("p");
		}
		UIComponent content = uip.getFacet(UIAboutPage.FACET_CONTENT);
		if (content != null) {
			writer.startElement("p", null);
			FacesUtil.renderChildren(context, content);
			writer.endElement("p");
		}

		writer.endElement("div");
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
