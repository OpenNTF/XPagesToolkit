package org.openntf.xpt.oneui.renderkit.html_extended;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.oneui.component.UINamePicker;
import org.openntf.xpt.oneui.kernel.NamePickerProcessor;
import org.openntf.xpt.oneui.ressources.XPTONEUIResourceProvider;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.util.JsonBuilder;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.context.DojoLibraryFactory;
import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.renderkit.dojo.DojoRendererUtil;
import com.ibm.xsp.extlib.renderkit.dojo.form.DojoFormWidgetRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.resource.DojoModuleResource;
import com.ibm.xsp.util.FacesUtil;

public class NamePickerRenderer extends DojoFormWidgetRenderer {

	@Override
	protected String getDefaultDojoType(FacesContext context, FacesDojoComponent component) {
		return "extlib.dijit.ListTextBox"; // $NON-NLS-1$
	}

	@Override
	protected DojoModuleResource getDefaultDojoModule(FacesContext context, FacesDojoComponent component) {
		return ExtLibResources.extlibListTextBox;
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		UINamePicker uit = (UINamePicker) component;
		if (!uit.isRendered()) {
			return;
		}
		ResponseWriter writer = context.getResponseWriter();
		// Only if the control is not readOnyl
		if (!isReadOnly(uit)) {
			UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
			DojoLibrary djLib = DojoLibraryFactory.getDefaultLibrary();
			boolean pre17 = djLib.getVersionNumber() < 10700;
			if (pre17) {
				rootEx.addEncodeResource(context, XPTONEUIResourceProvider.XPTONEUI_NAMEPICKER_TYPEAHED_DATASTORE_161);
				rootEx.addEncodeResource(context, XPTONEUIResourceProvider.XPTONEUI_NAMEPICKER_TYPEAHED_WIDGET_161);

			} else {
				rootEx.addEncodeResource(context, XPTONEUIResourceProvider.XPTONEUI_NAMEPICKER_TYPEAHED_DATASTORE);
				rootEx.addEncodeResource(context, XPTONEUIResourceProvider.XPTONEUI_NAMEPICKER_TYPEAHED_WIDGET);
			}
			rootEx.setDojoParseOnLoad(true);
			// MVSEP und MV Check
			rootEx.addScriptOnce(buildScript(uit.buildJSFunctionName(), uit.getClientId(context), !StringUtil.isEmpty(uit.getMultipleSeparator()), uit.getMultipleSeparator()));
			writeInputField(context, writer, uit, pre17);
		}
		super.encodeBegin(context, component);
	}

	private void writeInputField(FacesContext context, ResponseWriter writer, UINamePicker uit, boolean pre17) throws IOException {

		String jsUitId = uit.getClientId(context).replace(":", "_");
		writer.startElement("span", uit);
		writer.writeAttribute("id", uit.getClientId(context) + "_store", "id"); //$NON-NLS-1$ //$NON-NLS-2$
		if (pre17) {
			writer.writeAttribute("dojoType", "xptoneui.typeahead.pre17.ReadStore", "dojoType");
		} else {
			writer.writeAttribute("dojoType", "xptoneui.typeahead.ReadStore", "dojoType");
		}
		writer.writeAttribute("jsId", jsUitId + "_store", "jsId");
		writer.writeAttribute("ajaxId", uit.getClientId(context), null);
		writer.writeAttribute("axtarget", uit.getClientId(context), null);
		writer.writeAttribute("mode", "partial", "mode");
		writer.endElement("span");

		writer.startElement("input", uit); //$NON-NLS-1$
		writer.writeAttribute("id", uit.getClientId(context) + "_typeahead", null);
		writer.writeAttribute("name", uit.getClientId(context) + "_typeahead", null); //$NON-NLS-1$ //$NON-NLS-2$
		writer.writeAttribute("type", "text", null);
		if (pre17) {
			writer.writeAttribute("dojoType", "xptoneui.typeahead.pre17.widget", null);
		} else {
			writer.writeAttribute("dojoType", "xptoneui.typeahead.widget", null);
		}
		writer.writeAttribute("class", "xspInputFieldEditBox", null);
		writer.writeAttribute("store", jsUitId + "_store", null);
		writer.writeAttribute("jsCallback", uit.buildJSFunctionName(), null);
		writer.endElement("input"); //$NON-NLS-1$

	}

	private String buildScript(String strFunctionName, String strID, boolean isMV, String strMVSep) {
		StringBuilder sb = new StringBuilder("function ");
		sb.append(strFunctionName);
		sb.append("( label, value ) {");
		sb.append("var c = dojo.byId('" + strID + "');");
		sb.append("if(c) {");
		sb.append("var dj = dijit.byId('" + strID + "');");
		sb.append("if(dj) {");
		if (isMV) {
			// MV Code
			sb.append("if(!dj.labels[value]) {");
			sb.append("dj.labels[value]=label;");
			sb.append("}");
			sb.append("var oldValue = dj.value;");
			sb.append("if (oldValue != '') {");
			sb.append("dj._setValueAttr(oldValue +'" + strMVSep + "' +value,true);");
			sb.append("} else {");
			sb.append("dj._setValueAttr(value,true);");
			sb.append("}");
			// MV Code End
		} else {
			// SINGLE Value Code
			sb.append("if(!dj.labels[value]) {");
			sb.append("dj.labels[value]=label;");
			sb.append("}");
			sb.append("dj._setValueAttr(value,true);");
			// SINGLE Value Code End
		}
		sb.append("window.setTimeout(\"dojo.byId('" + strID + "_typeahead" + "').value ='';\", 500);");
		sb.append("}");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	@Override
	protected void initDojoAttributes(FacesContext context, FacesDojoComponent dojoComponent, Map<String, String> attrs) throws IOException {
		super.initDojoAttributes(context, dojoComponent, attrs);
		if (dojoComponent instanceof UINamePicker) {
			UINamePicker c = (UINamePicker) dojoComponent;
			String msep = c.getMultipleSeparator();
			if (!StringUtil.equals(msep, ",")) {
				DojoRendererUtil.addDojoHtmlAttributes(attrs, "msep", msep); // $NON-NLS-1$
			}
			// Fill the labels if required
			if (c.isDisplayLabel()) {
				DojoRendererUtil.addDojoHtmlAttributes(attrs, "displayLabel", true); // $NON-NLS-1$
				String[] values = getValues(context, c, msep);
				if (values != null) {
					StringBuilder b = new StringBuilder();
					JsonBuilder w = new JsonBuilder(b, true);
					w.startObject();
					HashMap<String, String> entries = NamePickerProcessor.INSTANCE.getDislplayLabels(c, values);
					if (entries != null) {
						for (String strKey : entries.keySet()) {
							w.startProperty(strKey);
							w.outStringLiteral(entries.get(strKey));
							w.endProperty();

						}
					}
					w.endObject();
					DojoRendererUtil.addDojoHtmlAttributes(attrs, "labels", b.toString()); // $NON-NLS-1$
				}
			}
		}
	}

	protected String[] getValues(FacesContext context, UINamePicker c, String msep) {
		String value = FacesUtil.convertValue(context, c);
		if (StringUtil.isNotEmpty(value)) {
			String[] values = StringUtil.isNotEmpty(msep) ? StringUtil.splitString(value, msep.charAt(0)) : new String[] { value };
			return values;
		}
		return null;
	}
}
