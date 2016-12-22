/**
 * Copyright 2014, WebGate Consulting AG
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
