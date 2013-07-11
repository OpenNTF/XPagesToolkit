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
package org.openntf.xpt.agents.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.agents.component.UIAgentProgressbar;
import org.openntf.xpt.agents.resources.XPTAgentResourceProvider;
import org.openntf.xpt.core.utils.XPTLibUtils;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIScriptCollector;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.renderkit.FacesRenderer;

public class ProgressbarRenderer extends FacesRenderer {

	private final static int PROP_PROGRESSBAR_CLASS = 1;
	private final static int PROP_PROGRESSBAR_STYLE = 2;
	private final static int PROP_PROGRESSBAR_TITLE_CLASS = 3;
	private final static int PROP_PROGRESSBAR_TITLE_STYLE = 4;
	private final static int PROP_PROGRESSBAR_TASK_CLASS = 5;
	private final static int PROP_PROGRESSBAR_TASK_STYLE = 6;
	private final static int PROP_PROGRESSBAR_PB_CLASS = 7;
	private final static int PROP_PROGRESSBAR_PB_STYLE = 8;

	protected String getProperty(int prop) {
		switch (prop) {
		case PROP_PROGRESSBAR_CLASS:
			return "xptProgressBar"; // $NON-NLS-1$
		case PROP_PROGRESSBAR_STYLE:
			return "display:none"; // $NON-NLS-1$
		case PROP_PROGRESSBAR_TITLE_CLASS:
			return "xptProgressBarHeader"; // $NON-NLS-1$
		case PROP_PROGRESSBAR_TASK_CLASS:
			return "xptProgressBarBody"; // $NON-NLS-1$
		case PROP_PROGRESSBAR_PB_CLASS:
			return "xptProgressBarPB"; // $NON-NLS-1$
		}
		return null;
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		// Nothing to decode here...
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		UIAgentProgressbar pgBar = (UIAgentProgressbar) component;

		boolean rendered = component.isRendered();
		if (!rendered) {
			return;
		}

		// Compose the url
		String url = pgBar.getUrl(context);
		url = url.replaceAll("\\\\", "/");

		// Get the service name
		String serviceName = pgBar.getServiceName();
		if (StringUtil.isEmpty(serviceName)) {
			return; // no name
		}
		String strAgentname = pgBar.getAgentName();
		if (StringUtil.isEmpty(strAgentname)) {
			return; // no agentname
		}
		String strID = pgBar.getClientId(context);
		// Add the dojo modules
		UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
		rootEx.addEncodeResource(context, XPTAgentResourceProvider.XPTAGENTS_PROGRESSBAR_DOJO);
		rootEx.addEncodeResource(context, XPTAgentResourceProvider.XPTAGENTS_PROGRESSBAR_AGENTCONTROLLER);
		rootEx.addEncodeResource(context, XPTAgentResourceProvider.XPTAGENTS_PROGRESSBAR_CSS);
		rootEx.setDojoParseOnLoad(true);

		// Generate the piece of script and add it to the script collector
		StringBuilder b = new StringBuilder(256);
		b.append(serviceName);
		b.append(" = new xptagents.progressbar.agentcontroller({\n"); // $NON-NLS-1$
		b.append("  \"agentname\": \"");
		b.append(strAgentname);
		b.append("\",\n"); // $NON-NLS-1$
		b.append("  \"serviceurl\": \"");
		b.append(url);
		b.append("\",\n");
		b.append("  \"targetid\": \"");
		b.append(strID);
		b.append("\"});\n"); // $NON-NLS-1$

		UIScriptCollector sc = UIScriptCollector.find();
		sc.addScriptOnLoad(b.toString());
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", pgBar);
		writer.writeAttribute("id", strID, null);
		writeClassAttribute(XPTLibUtils.concatStyleClasses(getProperty(PROP_PROGRESSBAR_CLASS), pgBar.getStyleClass()), writer);
		writeStyleAttribute(XPTLibUtils.concatStyles(getProperty(PROP_PROGRESSBAR_STYLE), pgBar.getStyle()), writer);
		writer.startElement("div", pgBar);
		writer.writeAttribute("id", strID + "_title", null);
		writeClassAttribute(XPTLibUtils.concatStyleClasses(getProperty(PROP_PROGRESSBAR_TITLE_CLASS), pgBar.getStyleClassTitle()), writer);
		writeStyleAttribute(XPTLibUtils.concatStyles(getProperty(PROP_PROGRESSBAR_TITLE_STYLE), pgBar.getStyleTitle()), writer);
		writer.endElement("div");

		writer.startElement("div", pgBar);
		writer.writeAttribute("id", strID + "_task", null);
		writeClassAttribute(XPTLibUtils.concatStyleClasses(getProperty(PROP_PROGRESSBAR_TASK_CLASS), pgBar.getStyleClassTask()), writer);
		writeStyleAttribute(XPTLibUtils.concatStyles(getProperty(PROP_PROGRESSBAR_TASK_STYLE), pgBar.getStyleTask()), writer);
		writer.endElement("div");

		writer.startElement("div", pgBar);
		writer.writeAttribute("id", strID + "_pb", null);
		writer.writeAttribute("dojoType", "dijit.ProgressBar", null);
		writeClassAttribute(XPTLibUtils.concatStyleClasses(getProperty(PROP_PROGRESSBAR_PB_CLASS), pgBar.getStyleClassProgressBar()), writer);
		writeStyleAttribute(XPTLibUtils.concatStyles(getProperty(PROP_PROGRESSBAR_PB_STYLE), pgBar.getStyleProgressBar()), writer);
		writer.endElement("div");

		writer.endElement("div");
	}

	private void writeClassAttribute(String strClass, ResponseWriter writer) throws IOException {
		if (!StringUtil.isEmpty(strClass)) {
			writer.writeAttribute("class", strClass, null);
		}
	}

	private void writeStyleAttribute(String strStyle, ResponseWriter writer) throws IOException {
		if (!StringUtil.isEmpty(strStyle)) {
			writer.writeAttribute("style", strStyle, null);
		}
	}

	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	}
}
