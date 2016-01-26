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

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.beans.XPTAgentBean;
import org.openntf.xpt.agents.servlet.IXPTServletCommand;
import org.openntf.xpt.core.dss.changeLog.ChangeLogService;
import org.openntf.xpt.core.dss.encryption.EncryptionService;
import org.openntf.xpt.core.utils.HttpResponseSupport;

import com.ibm.domino.services.util.JsonWriter;

public class CheckAgents implements IXPTServletCommand {

	@Override
	public void processActionH(HttpServletResponse resp, XPTAgentBean agentBean, FacesContext context) {
		try {

			// CHECK if a possible Encryption Provider is loaded
			EncryptionService.getInstance().agentLoadProvider();
			// CHECK if a possible ChangLogProvider is loaded
			ChangeLogService.getInstance().getChangeLogProcessors();

			HttpResponseSupport.setJSONUTF8ContentType(resp);
			JsonWriter jsWriter = new JsonWriter(resp.getWriter(), true);
			jsWriter.startObject();
			jsWriter.startProperty("status");
			jsWriter.outStringLiteral("ok");
			jsWriter.endProperty();

			jsWriter.startProperty("checkagents");
			jsWriter.outIntLiteral(agentBean.checkSchedule(context));
			jsWriter.endProperty();
			jsWriter.endObject();

			jsWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
