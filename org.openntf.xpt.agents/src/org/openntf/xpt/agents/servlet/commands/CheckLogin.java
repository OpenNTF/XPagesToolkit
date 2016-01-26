/*
 * Copyright 2013, WebGate Consulting AG
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
package org.openntf.xpt.agents.servlet.commands;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.beans.XPTAgentBean;
import org.openntf.xpt.agents.servlet.IXPTServletCommand;
import org.openntf.xpt.core.utils.HttpResponseSupport;

import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class CheckLogin implements IXPTServletCommand {

	@SuppressWarnings("unchecked")
	@Override
	public void processActionH(HttpServletResponse resp, XPTAgentBean agentBean, FacesContext context) {
		HttpResponseSupport.setJSONUTF8ContentType(resp);

		String strUser = null;
		int nAccessLevel = 0;
		List<String> lstRoles = new ArrayList<String>();
		try {
			strUser = ExtLibUtil.getCurrentSession(context).getEffectiveUserName();
			nAccessLevel = ExtLibUtil.getCurrentDatabase(context).getCurrentAccessLevel();
			lstRoles.addAll(ExtLibUtil.getCurrentDatabase(context).queryAccessRoles(strUser));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			JsonWriter jsWriter = new JsonWriter(resp.getWriter(), true);
			jsWriter.startObject();
			jsWriter.startProperty("status");
			jsWriter.outStringLiteral("ok");
			jsWriter.endProperty();

			jsWriter.startProperty("username");
			jsWriter.outStringLiteral(strUser);
			jsWriter.endProperty();

			jsWriter.startProperty("level");
			jsWriter.outIntLiteral(nAccessLevel);
			jsWriter.endProperty();

			jsWriter.startProperty("roles");
			jsWriter.startArray();
			for (String strRole : lstRoles) {
				jsWriter.startArrayItem();
				jsWriter.outStringLiteral(strRole);
				jsWriter.endArrayItem();
			}

			jsWriter.endArray();
			jsWriter.endProperty();

			jsWriter.endObject();
			jsWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
