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

import java.util.HashMap;

import lotus.domino.Database;
import lotus.domino.Session;
import org.openntf.xpt.agents.XPageAgentJob;
import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.agents.annotations.XPagesAgent;;

@XPagesAgent(Alias = "agentWithProperties", Name = "Agent with Properties", executionMode = ExecutionMode.ON_REQUEST)
public class TestAgentWithProperties extends XPageAgentJob {

	public TestAgentWithProperties(String name) {
		super(name);
	}

	@Override
	public int executeCode(Session arg0, Database arg1) {
		try {
			HashMap<String, String > exProp = getExecutionProperties();
		
			setCurrentTaskStatus("Task started at: "+exProp.get("startdate"));
			setName(exProp.get("myname"));
			setTaskCompletion(0);
			int numberOfSubTasks = (int) (Math.random() * 50);
			for (int p = 0; p < numberOfSubTasks; p++) {
				setTaskCompletion((int) ((long) p * (long) 100 / numberOfSubTasks));
				setCurrentTaskStatus("ST: " + p +"  .... of "+ numberOfSubTasks +" / Started at: "+exProp.get("startdate"));
				try {
					Thread.sleep((long) (Math.random() * 1000 +1000));
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			setCurrentTaskStatus("Task finished");
			setTaskCompletion(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
