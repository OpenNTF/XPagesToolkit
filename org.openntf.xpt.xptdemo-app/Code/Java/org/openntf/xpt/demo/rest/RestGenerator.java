package org.openntf.xpt.demo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.json.JSONService;
import org.openntf.xpt.core.utils.ErrorJSONBuilder;
import org.openntf.xpt.demo.bo.Contact;
import org.openntf.xpt.demo.bo.ContactStorageService;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.domino.services.util.JsonWriter;

public enum RestGenerator {
	INSTANCE;

	public boolean handleRequest(RestServiceEngine engine) {
		HttpServletRequest hsrequest = engine.getHttpRequest();
		HttpServletResponse hsresponse = engine.getHttpResponse();

		try {
			String strAction = hsrequest.getParameter("action");
			String strID = hsrequest.getParameter("id");
			if (StringUtil.isEmpty(strAction)) {
				ErrorJSONBuilder.getInstance().processError2JSON(hsresponse, 999, "No action attribute defined", null);
				return false;
			}
			if ("entry".equals(strAction) && StringUtil.isEmpty(strID)) {
				ErrorJSONBuilder.getInstance().processError2JSON(hsresponse, 999, "No id defined", null);
				return false;
			}
			if ("entry".equals(strAction)) {
				Contact con = ContactStorageService.getInstance().getById(strID);
				if (con != null) {
					JsonWriter jsWriter = new JsonWriter(hsresponse.getWriter(), true);
					Result res = new Result();
					res.setStatus("ok");
					List<Contact> lstCon = new ArrayList<Contact>();
					lstCon.add(con);
					res.setContacts(lstCon);
					JSONService.getInstance().process2JSON(jsWriter, res);
					jsWriter.close();
					return true;
				} else {
					ErrorJSONBuilder.getInstance().processError2JSON(hsresponse, 999, "No contact found for ID = " + strID, null);
					return false;
				}
			}
			if ("mycontacts".equalsIgnoreCase(strAction)) {
				List<String> lstFields = new ArrayList<String>();
				lstFields.add("Observer");
				List<Contact> lstCon = ContactStorageService.getInstance().getAllMyObjects("AllContacts", lstFields);
				JsonWriter jsWriter = new JsonWriter(hsresponse.getWriter(), true);
				Result res = new Result();
				res.setStatus("ok");
				res.setContacts(lstCon);
				JSONService.getInstance().process2JSON(jsWriter, res);
				jsWriter.close();

				return true;
			}
			if ("allcontacts".equals(strAction)) {
				List<Contact> lstCon = ContactStorageService.getInstance().getAll("AllContacts");
				JsonWriter jsWriter = new JsonWriter(hsresponse.getWriter(), true);
				Result res = new Result();
				res.setStatus("ok");
				res.setContacts(lstCon);
				JSONService.getInstance().process2JSON(jsWriter, res);
				jsWriter.close();

				return true;
			}
		} catch (Exception e) {
			ErrorJSONBuilder.getInstance().processError2JSON(hsresponse, 999, "General Error", e);
		}

		return false;
	}
}
