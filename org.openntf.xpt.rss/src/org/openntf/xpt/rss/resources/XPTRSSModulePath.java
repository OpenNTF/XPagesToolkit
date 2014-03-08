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
package org.openntf.xpt.rss.resources;

import com.ibm.xsp.resource.DojoModulePathResource;

public class XPTRSSModulePath extends DojoModulePathResource {

	public static final String XPT_RSS_MODUL_PATH = "/.ibmxspres/"+XPTRSSResourceProvider.XPT_PREFIX;

	public XPTRSSModulePath() {
		super("xptrss",XPT_RSS_MODUL_PATH);
	}
}
