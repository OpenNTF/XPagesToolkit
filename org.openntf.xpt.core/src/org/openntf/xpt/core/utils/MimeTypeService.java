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
