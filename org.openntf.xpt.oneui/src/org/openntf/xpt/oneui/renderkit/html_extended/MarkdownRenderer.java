package org.openntf.xpt.oneui.renderkit.html_extended;

import java.io.IOException;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.markdown4j.Markdown4jProcessor;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.renderkit.html_basic.TextAreaRenderer;
import com.ibm.xsp.util.FacesUtil;

public class MarkdownRenderer extends TextAreaRenderer {

	@Override
	protected void writeTag(FacesContext context, UIInput uiinput, ResponseWriter writer, String value) throws IOException {
		if (!uiinput.isRendered()) {
			return;
		}
		if (FacesUtil.isComponentReadOnly(context, uiinput)) {
			writeMarkdown(context, writer, value);
		} else {
			super.writeTag(context, uiinput, writer, value);
		}
	}

	private void writeMarkdown(FacesContext context, ResponseWriter writer, String value) {
		try {
			String md = new Markdown4jProcessor().process(value);
			writer.write(md);
		} catch (Exception ex) {
			LoggerFactory.logError(getClass(), "Error during write MD", ex);
		}

	}
}
