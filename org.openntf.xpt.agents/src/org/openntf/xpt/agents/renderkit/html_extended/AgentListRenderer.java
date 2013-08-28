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
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.openntf.xpt.agents.XPageAgentEntry;
import org.openntf.xpt.agents.component.UIAgentEntry;
import org.openntf.xpt.agents.component.UIAgentList;
import org.openntf.xpt.core.utils.logging.LoggerFactory;


import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.JavaScriptUtil;

public class AgentListRenderer extends Renderer {

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (component instanceof UIAgentList) {
			UIAgentList agl = (UIAgentList) component;
			if (!agl.isRendered()) {
				return;
			}
			ResponseWriter rw = context.getResponseWriter();
			writeMainElement(context, rw, agl);
		}
	}

	private void writeMainElement(FacesContext context, ResponseWriter rw, UIAgentList agl) throws IOException {
		rw.startElement("div", agl);
		String strClientID = agl.getClientId(context);
		rw.writeAttribute("id", strClientID, null);
		rw.startElement("table", agl);
		rw.writeAttribute("class", "lotusTable", null);
		rw.writeAttribute("border", "0", null);
		rw.writeAttribute("cellspacing", "0", null);
		rw.writeAttribute("cellpadding", "0", null);
		rw.writeAttribute("summary", "table summary goes here...", null);

		rw.startElement("tbody", agl);
		// HEADER
		writeHeader(context, rw, agl);

		// AGENTS
		writeAgentList(context, rw, agl, strClientID);
		rw.endElement("tbody");
		rw.endElement("table");
		rw.endElement("div");

	}

	private void writeAgentList(FacesContext context, ResponseWriter rw, UIAgentList agl, String strClientID) throws IOException {
		boolean isFirst = true;

		for (UIAgentEntry uiAgentEntry : agl.getAllEntries()) {
			rw.startElement("tr", null);
			if (isFirst) {
				rw.writeAttribute("class", "lotusFirst", null);
				isFirst = false;
			}

			rw.startElement("td", null);
			rw.writeAttribute("class", "lotusFirstCell", null);
			if (uiAgentEntry.getEntry().getExecutionMode().isScheduled()) {
				if (uiAgentEntry.getEntry().isActive()) {
					writeDeactivate(context, rw, strClientID, uiAgentEntry.getEntry(), agl);
				} else {
					writeActivate(context, rw, strClientID, uiAgentEntry.getEntry(), agl);

				}
			} else {
				rw.startElement("img", null);
				rw.writeAttribute("src", "/icons/vwicn080.gif", null);
				rw.endElement("img");
			}

			rw.endElement("td");

			rw.startElement("td", null);
			rw.writeText(uiAgentEntry.getEntry().getTitle(), null);
			rw.endElement("td");

			rw.startElement("td", null);
			rw.writeText(uiAgentEntry.getEntry().getAlias(), null);
			rw.endElement("td");

			rw.startElement("td", null);
			rw.writeText(uiAgentEntry.getEntry().getExecutionMode(), null);
			rw.endElement("td");

			rw.startElement("td", null);
			rw.writeAttribute("class", "lotusLastCell", null);
			rw.writeText(uiAgentEntry.getEntry().getIntervall(), null);
			rw.endElement("td");

			rw.endElement("tr");
		}

	}

	private void writeActivate(FacesContext context, ResponseWriter rw, String strClientID, XPageAgentEntry entry, UIAgentList agentList) throws IOException {
		String strPowerID = strClientID + "_" + entry.getAlias() + "_on";
		rw.startElement("a", null);
		rw.writeAttribute("id", strPowerID, null);
		rw.writeAttribute("href", "javascript:;", null);
		rw.startElement("img", null);
		rw.writeAttribute("src", "/icons/vwicn081.gif", null);
		rw.endElement("img");
		rw.endElement("a");
		setupSubmitOnClick(context, rw, agentList, strClientID, strPowerID);
	}

	private void writeDeactivate(FacesContext context, ResponseWriter rw, String strClientID, XPageAgentEntry entry, UIAgentList agentList) throws IOException {
		String strPowerID = strClientID + "_" + entry.getAlias() + "_off";
		rw.startElement("a", null);
		rw.writeAttribute("id", strPowerID, null);
		rw.writeAttribute("href", "javascript:;", null);
		rw.startElement("img", null);
		rw.writeAttribute("src", "/icons/vwicn082.gif", null);
		rw.endElement("img");
		rw.endElement("a");
		setupSubmitOnClick(context, rw, agentList, strClientID, strPowerID);

	}

	private void writeHeader(FacesContext context, ResponseWriter rw, UIAgentList agl) throws IOException {
		rw.startElement("tr", agl);
		rw.writeAttribute("class", "lotusFirst lotusSort", null);

		rw.startElement("th", agl);
		rw.writeAttribute("class", "lotusFirstCell  lotusRowHeader", null);
		rw.writeAttribute("colspan", "2", null);
		rw.writeText("Name", null);
		rw.endElement("th");

		rw.startElement("th", agl);
		rw.writeText("Alias", null);
		rw.endElement("th");

		rw.startElement("th", agl);
		rw.writeText("Type", null);
		rw.endElement("th");

		rw.startElement("th", agl);
		rw.writeAttribute("class", "lotusLastCell", null);
		rw.writeText("Execution", null);
		rw.endElement("th");

		rw.endElement("tr");

	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		// TODO Auto-generated method stub
		super.encodeEnd(context, component);
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		super.decode(context, component);
		if (component instanceof UIAgentList) {
			UIAgentList agl = (UIAgentList) component;
			String currentClientId = component.getClientId(context);
			String hiddenValue = FacesUtil.getHiddenFieldValue(context);
			logCurrent.info("currentClientID = " + currentClientId);
			logCurrent.info("hiddenValue =" + hiddenValue);
			if (StringUtil.isNotEmpty(hiddenValue) && hiddenValue.startsWith(currentClientId+"_")) {
				hiddenValue = hiddenValue.substring(currentClientId.length()+1);
				logCurrent.info("hiddenValue cutted: "+hiddenValue);
				String[] arrValues = hiddenValue.split("_");
				if (arrValues.length == 2) {
					for (UIAgentEntry age : agl.getAllEntries()) {
						logCurrent.info("test: " + age.getEntry().getAlias() + " -> " + arrValues[0]);
						if (age.getEntry().getAlias().equals(arrValues[0])) {
							if ("on".equalsIgnoreCase(arrValues[1])) {
								age.getEntry().setActive(true);
							} else {
								age.getEntry().setActive(false);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean getRendersChildren() {
		return false;
	}

	protected void setupSubmitOnClick(FacesContext context, ResponseWriter w, UIAgentList agentList, String clientId, String sourceId) throws IOException {
		boolean immediate = false;

		UIComponent subTree = ((FacesContextEx) context).getSubTreeComponent();

		boolean partialExec = false;
		// boolean partialExec = agentList.isPartialExecute();
		String execId = null;
		if (partialExec) {
			execId = agentList.getClientId(context);
			immediate = true;
		} else {
			if (subTree != null) {
				partialExec = true;
				execId = subTree.getClientId(context);
				immediate = true;
			}
		}

		boolean partialRefresh = false;
		// boolean partialRefresh = agentList.isPartialRefresh();
		String refreshId = null;
		if (partialRefresh) {
			// UIComponent refreshComponent = pager.findSharedDataPagerParent();
			// if (null == refreshComponent) {
			// refreshComponent = (UIComponent) pager.findDataIterator();
			// }
			// refreshId = AjaxUtilEx.getRefreshId(context, refreshComponent);
		} else {
			if (subTree != null) {
				partialRefresh = true;
				refreshId = subTree.getClientId(context);
			}
		}

		// call some JavaScript in xspClient.js
		final String event = "onclick"; // $NON-NLS-1$
		// Note, the onClick event is also triggered if the user tabs to the
		// image\link and presses enter (Not just when clicked with a
		// mouse).

		// When the source is clicked, put its id in the hidden field and
		// submit the form.
		StringBuilder buff = new StringBuilder();
		if (partialRefresh) {
			JavaScriptUtil.appendAttachPartialRefreshEvent(buff, sourceId, sourceId, execId, event,
			/* clientSideScriptName */null, immediate ? JavaScriptUtil.VALIDATION_NONE : JavaScriptUtil.VALIDATION_FULL,
			/* refreshId */refreshId,
			/* onstart getOnStart(pager) */"",
			/* oncomplete getOnComplete(pager) */"",
			/* onerror getOnError(pager) */"");
		} else {
			JavaScriptUtil.appendAttachEvent(buff, sourceId, sourceId, execId, event,
			/* clientSideScriptName */null,
			/* submit */true, immediate ? JavaScriptUtil.VALIDATION_NONE : JavaScriptUtil.VALIDATION_FULL);
		}
		String script = buff.toString();

		// Add the script block we just generated.
		JavaScriptUtil.addScriptOnLoad(script);
	}
}
