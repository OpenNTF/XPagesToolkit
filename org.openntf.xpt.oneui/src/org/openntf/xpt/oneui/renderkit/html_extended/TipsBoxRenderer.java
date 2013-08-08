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

import org.openntf.xpt.oneui.component.UITips;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.renderkit.FacesRenderer;

public class TipsBoxRenderer extends FacesRenderer {
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		UITips uit = (UITips) component;
		if (!uit.isRendered()) {
			return;
		}
		ResponseWriter writer = context.getResponseWriter();
		writeMainFrame(context, writer, uit);
	}

	private void writeMainFrame(FacesContext context, ResponseWriter writer, UITips uit) throws IOException {
		String strID = uit.getClientId(context);
		String strStyle = uit.getStyle();
		String strStyleClass = uit.getStyleClass();
		String strTitle = uit.getTitle();
		String strText = uit.getText();
		writer.startElement("div", uit);
		writer.writeAttribute("id", strID, null);

		if (StringUtil.isEmpty(strStyleClass)) {
			strStyleClass = "lotusInfoBox";
		}
		writer.writeAttribute("class", strStyleClass, null);
		if (!StringUtil.isEmpty(strStyle)) {
			writer.writeAttribute("style", strStyle, null);
		}
		writer.writeAttribute("role", "note", null);

		writeTitle(context, writer, uit, strTitle);
		writeText(context, writer, uit, strText);

		writer.endElement("div");

	}

	private void writeText(FacesContext context, ResponseWriter writer, UITips uit, String strText) throws IOException {
		writer.startElement("p", uit);
		if (!StringUtil.isEmpty(strText)) {
			writer.writeText(strText, null);
		}
		writer.endElement("p");

	}

	private void writeTitle(FacesContext context, ResponseWriter writer, UITips uit, String strTitle) throws IOException {
		writer.startElement("h3", uit);
		writer.startElement("span", null);
		writer.writeAttribute("class", "lotusLeft", null);
		if (!StringUtil.isEmpty(strTitle)) {
			writer.writeText(strTitle, null);
		}
		writer.endElement("span");
		writer.endElement("h3");

	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

}
