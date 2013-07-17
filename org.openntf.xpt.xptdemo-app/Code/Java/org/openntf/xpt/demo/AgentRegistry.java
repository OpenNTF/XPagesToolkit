package org.openntf.xpt.demo;

import org.openntf.xpt.agents.XPageAgentRegistry;

public class AgentRegistry extends XPageAgentRegistry {

	@Override
	public void registerAgents() {
		initAgent(TestAgent1.class);
		initAgent(TestAgentBackend.class);
		initAgent(TestAgentBackendUpdateUP.class);
		initAgent(TestAgentWithProperties.class);
	}
}
