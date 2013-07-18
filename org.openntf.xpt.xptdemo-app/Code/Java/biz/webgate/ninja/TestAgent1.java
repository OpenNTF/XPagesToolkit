package biz.webgate.ninja;

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
