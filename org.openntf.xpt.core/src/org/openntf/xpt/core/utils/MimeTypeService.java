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
package org.openntf.xpt.core.utils;

import javax.activation.MimetypesFileTypeMap;

public class MimeTypeService {
	private MimetypesFileTypeMap m_Mtftm;

	private static MimeTypeService m_Service;

	private MimeTypeService() {

	}

	public static MimeTypeService getInstance() {
		if (m_Service == null) {
			m_Service = new MimeTypeService();
		}
		return m_Service;
	}

	public MimetypesFileTypeMap getMimeTypes() {
		if (m_Mtftm == null) {
			m_Mtftm = new MimetypesFileTypeMap();
			m_Mtftm.addMimeTypes("application/pdf pdf");
			m_Mtftm.addMimeTypes("text/html htm html htmls");
			m_Mtftm.addMimeTypes("application/vnd.ms-excel xls");
			m_Mtftm.addMimeTypes("application/msword doc");
		}
		return m_Mtftm;
	}

}
