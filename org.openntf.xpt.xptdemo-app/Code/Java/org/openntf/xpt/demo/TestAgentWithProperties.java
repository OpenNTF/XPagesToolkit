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
				setCurrentTaskStatus("ST: " + p +"  .... of "+ numberOfSubTasks +" / Started at:"+exProp.get("startdate"));
				try {
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
