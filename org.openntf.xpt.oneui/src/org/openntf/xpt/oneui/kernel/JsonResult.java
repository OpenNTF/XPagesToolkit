package org.openntf.xpt.oneui.kernel;

import java.io.Serializable;
import java.util.List;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;

@JSONObject
public class JsonResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONEntity(jsonproperty = "status")
	private final String m_Status;
	@JSONEntity(jsonproperty = "error")
	private final String m_ErrorMessage;

	@JSONEntity(jsonproperty = "trace")
	private final String m_Trace;
	@JSONEntity(jsonproperty = "names")
	private final List<NameEntry> m_Names;

	public static JsonResult generateOKResult(List<NameEntry> lstEntry) {
		return new JsonResult("ok", "", "", lstEntry);
	}

	private JsonResult(String status, String errorMessage, String trace, List<NameEntry> names) {
		super();
		m_Status = status;
		m_ErrorMessage = errorMessage;
		m_Trace = trace;
		m_Names = names;
	}

	public String getStatus() {
		return m_Status;
	}

	public String getTrace() {
		return m_Trace;
	}

	public List<NameEntry> getNames() {
		return m_Names;
	}

	public String getErrorMessage() {
		return m_ErrorMessage;
	}

}
