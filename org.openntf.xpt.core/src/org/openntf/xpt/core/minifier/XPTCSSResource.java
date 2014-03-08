package org.openntf.xpt.core.minifier;

import java.net.URL;

import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.minifier.ResourceLoader.UrlCSSResource;

public class XPTCSSResource extends UrlCSSResource {
	public XPTCSSResource(DojoLibrary dojoLibrary, String name, URL url) {
		super(dojoLibrary, name, url);
	}

	@Override
	protected String calculateUrlPrefix() {
		String s = super.calculateUrlPrefix();
		// If we try to access a resource through a servlet, add the
		// prefix...
		if (s.startsWith("/.ibmxspres/")) { // $NON-NLS-1$
			s = "/xsp" + s; // $NON-NLS-1$
		}
		return s;
	}
}
