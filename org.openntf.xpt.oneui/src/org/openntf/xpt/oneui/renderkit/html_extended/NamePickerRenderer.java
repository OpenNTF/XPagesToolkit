package org.openntf.xpt.oneui.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.openntf.xpt.oneui.component.UINamePicker;

import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.renderkit.dojo.form.DojoFormWidgetRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.resource.DojoModuleResource;

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
			writeMainFrame(context, writer, uit);
			super.encodeBegin(context, component);
		}
	    
		private void writeMainFrame(FacesContext context, ResponseWriter writer, UINamePicker uit) throws IOException {
			
			 String name = getNameAttribute(context, uit);
	            writer.startElement("input", uit); //$NON-NLS-1$
	           // writer.writeAttribute("type", "hidden", null); //$NON-NLS-1$ //$NON-NLS-2$
	            writer.writeAttribute("id", uit.getClientId(context)+HIDDEN_SUFFIX, "id"); //$NON-NLS-1$ //$NON-NLS-2$
	            writer.writeAttribute("name", name, "name"); //$NON-NLS-1$ //$NON-NLS-2$
	            // Write the actual value
	            // For an input tag, it is passed as a parameter
	           // String currentValue = getCurrentValue(context, uiInput);
	            //writeValueAttribute(context, uiInput, writer, currentValue);
	            writer.endElement("input"); //$NON-NLS-1$
	            // If some script is needed...
	           // renderJavaScriptBinding(context, writer, uiInput);

		}
		
	
}
