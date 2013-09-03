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
import org.openntf.xpt.agents.annotations.XPagesAgent;;

@XPagesAgent(Alias = "longRunAgent", Name = "Long Running UI Action", executionMode = ExecutionMode.ON_REQUEST)
public class TestAgent1 extends XPageAgentJob {

	public TestAgent1(String name) {
		super(name);
	}

	@Override
	public int executeCode(Session arg0, Database arg1) {
		try {
			setCurrentTaskStatus("Task started");
			setTaskCompletion(0);
			System.out.println(arg0.getCommonUserName());
			System.out.println(arg0.getEffectiveUserName());
			System.out.println(arg1.getFilePath());
			if (getExecutionProperties() != null) {
				System.out.println(getExecutionProperties().size());
				System.out.println(getExecutionProperties().get("starttime"));
			}
			int numberOfSubTasks = (int) (Math.random() * 50);
			for (int p = 0; p < numberOfSubTasks; p++) {
				setTaskCompletion((int) ((long) p * (long) 100 / numberOfSubTasks));
				setCurrentTaskStatus("Processing SubTask: " + p +"  .... of "+ numberOfSubTasks);
				try {
					System.out.println("   >>  " + this.getClass().getCanonicalName() + " -->" + getTaskCompletion() + " ["
							+ getCurrentTaskStatus() + "]");
					Thread.sleep((long) (Math.random() * 1000 +1000));
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			for (int p = 100; p > -1; p--) {
				setTaskCompletion(p);
				setCurrentTaskStatus("Undo changes: " + p );
				System.out.println("   >>  " + this.getClass().getCanonicalName() + " -->" + getTaskCompletion() + " ["
						+ getCurrentTaskStatus() + "]");
				Thread.sleep((long) (Math.random() * 500));
			}
			setCurrentTaskStatus("Task finished");

			//setTaskCompletion(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
