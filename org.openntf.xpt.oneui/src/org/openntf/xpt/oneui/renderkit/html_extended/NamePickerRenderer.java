package org.openntf.xpt.oneui.renderkit.html_extended;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.oneui.component.UINamePicker;
import org.openntf.xpt.oneui.kernel.NamePickerProcessor;
import org.openntf.xpt.oneui.ressources.XPTONUIResourceProvider;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.util.JsonBuilder;
import com.ibm.xsp.component.UIViewRootEx;
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

		UIViewRootEx rootEx = (UIViewRootEx) context.getViewRoot();
		rootEx.addEncodeResource(context, XPTONUIResourceProvider.XPTONEUI_NAMEPICKER_TYPEAHED_DATASTORE);
		rootEx.addEncodeResource(context, XPTONUIResourceProvider.XPTONEUI_NAMEPICKER_TYPEAHED_WIDGET);
		rootEx.setDojoParseOnLoad(true);
		// MVSEP und MV Check
		rootEx.addScriptOnce(buildScript(uit.buildJSFunctionName(), uit.getClientId(context), !StringUtil.isEmpty(uit.getMultipleSeparator()),
				uit.getMultipleSeparator()));
		writeInputField(context, writer, uit);
		super.encodeBegin(context, component);
	}

	private void writeInputField(FacesContext context, ResponseWriter writer, UINamePicker uit) throws IOException {

		String name = getNameAttribute(context, uit);
		String jsUitId = uit.getClientId(context).replace(":", "_");

		writer.startElement("input", uit); //$NON-NLS-1$
		writer.writeAttribute("type", "hidden", null); //$NON-NLS-1$ //$NON-NLS-2$
		writer.writeAttribute("id", uit.getClientId(context) + HIDDEN_SUFFIX, "id"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.writeAttribute("name", name, "name"); //$NON-NLS-1$ //$NON-NLS-2$
		// Write the actual value
		// For an input tag, it is passed as a parameter
		// String currentValue = getCurrentValue(context, uiInput);
		// writeValueAttribute(context, uiInput, writer, currentValue);
		writer.endElement("input"); //$NON-NLS-1$
		// If some script is needed...
		// renderJavaScriptBinding(context, writer, uiInput);

		writer.startElement("span", uit);
		writer.writeAttribute("id", uit.getClientId(context) + "_store", "id"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.writeAttribute("dojoType", "ibm.xsp.widget.layout.data.TypeAheadReadStore", "dojoType");
		writer.writeAttribute("jsId", jsUitId + "_store", "jsId");
		writer.writeAttribute("ajaxId", uit.getClientId(context), null);
		writer.writeAttribute("axtarget", uit.getClientId(context), null);
		writer.writeAttribute("mode", "partial", "mode");
		writer.endElement("span");

		writer.startElement("input", uit); //$NON-NLS-1$
		writer.writeAttribute("id", uit.getClientId(context) + "_typeahead", "id");
		writer.writeAttribute("name", uit.getClientId(context) + "_typeahead", "name"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.writeAttribute("type", "text", "type");
		writer.writeAttribute("dojoType", "ibm.xsp.widget.layout.TypeAhead", "dojoType");
		writer.writeAttribute("class", "xspInputFieldEditBox", null);
		writer.writeAttribute("store", jsUitId + "_store", "store");
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
		sb.append("window.setTimeout(\"dojo.byId('"+strID +"_typeahead"+"').value ='';\", 500);");
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
