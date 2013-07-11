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
package org.openntf.xpt.agents.resources;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.resource.Resource;

public class XPTRequestCustomizer implements RequestParameters.ResourceProvider {

	private static XPTRequestCustomizer m_Customizer;

	private XPTRequestCustomizer() {

	}

	public static XPTRequestCustomizer getInstance() {
		if (m_Customizer == null) {
			m_Customizer = new XPTRequestCustomizer();
		}
		return m_Customizer;
	}

	private List<Resource> m_Resource;

	@Override
	public List<Resource> getResources(FacesContext arg0) throws IOException {
		if (m_Resource == null) {

		}
		return m_Resource;
	}

}
