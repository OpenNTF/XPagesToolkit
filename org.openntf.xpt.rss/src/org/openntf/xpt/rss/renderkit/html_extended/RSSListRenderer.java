package org.openntf.xpt.rss.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.rss.component.UIRSSList;
import org.openntf.xpt.rss.resources.XPTRSSResourceProvider;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIScriptCollector;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.renderkit.FacesRenderer;

public class RSSListRenderer extends FacesRenderer {

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
		UIRSSList rssList = (UIRSSList) component;

		boolean rendered = component.isRendered();
		if (!rendered) {
			return;
		}

		// Compose the url
		String url = rssList.getUrl(context);
		url = url.replaceAll("\\\\", "/");

		String strHTMLTemplate = rssList.getHtmlTemplate();
		String strFeeedURL = rssList.getFeedURL();
		String strID = rssList.getClientId(context);
		// Add the dojo modules
		UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
		rootEx.addEncodeResource(context, XPTRSSResourceProvider.XPTRSS_DATE_LOCALE);
		rootEx.addEncodeResource(context, XPTRSSResourceProvider.XPTRSS_WIDGET);
		rootEx.addEncodeResource(context, XPTRSSResourceProvider.XPTRSS_TEMPLATED);
		rootEx.addEncodeResource(context, XPTRSSResourceProvider.XPTRSS_FEEDCONTROLLER);
		rootEx.addEncodeResource(context, XPTRSSResourceProvider.XPTRSS_CSS);
		rootEx.setDojoParseOnLoad(true);

		// Generate the piece of script and add it to the script collector
		StringBuilder b = new StringBuilder(256);
		b.append("new xptrss.list.feedcontroller({\n"); // $NON-NLS-1$
		b.append(" proxyurl: \"");
		b.append(url);
		b.append("\",\n"); // $NON-NLS-1$
		b.append(" feedURL: \"");
		b.append(strFeeedURL);
		b.append("\",\n"); // $NON-NLS-1$
		if (!StringUtil.isEmpty(strHTMLTemplate)) {
			b.append(" templateString: ");
			b.append(strHTMLTemplate);
			b.append(",\n"); // $NON-NLS-1$
		}
		if (rssList.getUseDescription()) {
			b.append(" useDescription: true,\n"); // $NON-NLS-1$
		}
		b.append(" targetid: \"");
		b.append(strID);
		b.append("\"}).placeAt(\"" + strID + "\");\n"); // $NON-NLS-1$

		UIScriptCollector sc = UIScriptCollector.find();
		sc.addScriptOnLoad(b.toString());
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", rssList);
		writer.writeAttribute("id", strID, null);
		writeClassAttribute(rssList.getStyleClass(), writer);
		writeStyleAttribute(rssList.getStyle(), writer);

		writer.startElement("img", null);
		writer.writeAttribute("src", "/xsp/.ibmxspres/.xptrss/img/loader1.gif", null);
		writer.writeAttribute("id", strID + "_feedLoader", null);
		writer.endElement("img");

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
}
