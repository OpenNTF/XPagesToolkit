package org.openntf.xpt.oneui.component;

import com.ibm.xsp.component.xp.XspInputTextarea;

public class UIMarkdownInput extends XspInputTextarea {
	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uimarkdowninput"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uimarkdowninput"; //$NON-NLS-1$

	public UIMarkdownInput() {
		setRendererType(RENDERER_TYPE);
	}
}
