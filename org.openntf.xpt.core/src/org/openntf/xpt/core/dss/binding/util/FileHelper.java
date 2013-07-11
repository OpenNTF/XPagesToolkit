package org.openntf.xpt.core.dss.binding.util;

import java.io.Serializable;
import java.util.Date;

public class FileHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private String m_Id;
	private String m_Server;
	private String m_Path;
	private String m_DocID;
	private String m_FieldName;
	private String m_DisplayName;
	private int m_FileSize;
	private String m_FileType;
	private String m_Name;
	//private byte[] m_ByteFile;
	private Date m_Created;
	private boolean m_NewFile;
	
	public String getServer() {
		return m_Server;
	}

	public void setServer(String server) {
		m_Server = server;
	}

	public void setPath(String path) {
		m_Path = path;
	}

	public String getPath() {
		return m_Path;
	}

	public String getDocID() {
		return m_DocID;
	}

	public void setDocID(String docID) {
		m_DocID = docID;
	}

	public Date getCreated() {
		return m_Created;
	}

	public void setCreated(Date created) {
		m_Created = created;
	}

	public void setId(String id) {
		m_Id = id;
	}

	public String getId() {
		return m_Id;
	}
	public int getFileSize() {
		return m_FileSize;
	}

	public void setFileSize(int fileSize) {
		m_FileSize = fileSize;
	}

	public String getFileType() {
		return m_FileType;
	}

	public void setFileType(String fileType) {
		m_FileType = fileType;
	}

	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
	}

	public void setFieldName(String fieldName) {
		m_FieldName = fieldName;
	}

	public String getFieldName() {
		return m_FieldName;
	}

	public void setNewFile(boolean newFile) {
		m_NewFile = newFile;
	}

	public boolean isNewFile() {
		return m_NewFile;
	}

	public void setDisplayName(String displayName) {
		m_DisplayName = displayName;
	}

	public String getDisplayName() {
		return m_DisplayName;
	}


	/*
	public void setByteFile(byte[] byteFile) {
		m_ByteFile = byteFile;
	}

	public byte[] getByteFile() {
		return m_ByteFile;
	}
	*/
}
