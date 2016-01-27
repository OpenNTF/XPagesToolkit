package org.openntf.xpt.core.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.json.JSONService;
import org.openntf.xpt.core.rest.XPTRestException;

import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.services.util.JsonWriter;

public class BaseJson2ObjectRestBinding {

	public void extractObjectFromRequest(HttpServletRequest request, Object object) throws XPTRestException {
		try {
			JsonJavaFactory factory = JsonJavaFactory.instanceEx;
			JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, request.getReader());
			int nresult = JSONService.getInstance().processJson2Object(json, object);
			if (nresult < 1) {
				throw new XPTRestException("Error during Json -> object conversation: " + nresult);
			}
		} catch (XPTRestException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new XPTRestException(ex);
		}
	}

	public void emitObject(HttpServletResponse response, Object object) throws XPTRestException {
		try {
			JsonWriter jsonWriter = new JsonWriter(response.getWriter(), true);
			int nresult = JSONService.getInstance().process2JSON(jsonWriter, object);
			if (nresult < 1) {
				throw new XPTRestException("Error during object -> json conversation: " + nresult);
			}
		} catch (XPTRestException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new XPTRestException(ex);
		}
	}
}
