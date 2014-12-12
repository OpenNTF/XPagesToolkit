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
package org.openntf.xpt.demo;

import lotus.domino.Database;
import lotus.domino.Session;

import org.openntf.xpt.agents.XPageAgentJob;
import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.agents.annotations.XPagesAgent;

@XPagesAgent(Alias="updateAllUserProfiles", Name="Update All UserProfiels", executionMode = ExecutionMode.SCHEDULE, intervall=3)
public class TestAgentBackendUpdateUP extends XPageAgentJob {

	public TestAgentBackendUpdateUP(String name) {
		super(name);
	}

	@Override
	public int executeCode(Session session, Database dbCurrent) {
		try {
			setCurrentTaskStatus("Task started");
			setTaskCompletion(0);
			System.out.println("TEST alll 3 Minutes");
			System.out.println(session.getCommonUserName());
			System.out.println(session.getEffectiveUserName());
			System.out.println(dbCurrent.getFilePath());
			System.out.println("Faces Context: "+getFacesContext());
			System.out.println("Applicaiton: "+getFacesContext().getApplication());
			
			setCurrentTaskStatus("Task finished");
			setTaskCompletion(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


}
