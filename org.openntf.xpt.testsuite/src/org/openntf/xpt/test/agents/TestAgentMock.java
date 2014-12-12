package org.openntf.xpt.test.agents;

import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.agents.annotations.XPagesAgent;

@XPagesAgent(Alias = "agent", Name = "Test Agent", intervall = 55, executionMode = ExecutionMode.SCHEDULE)
public class TestAgentMock {

	public TestAgentMock() {
		// TODO Auto-generated constructor stub
	}

}
