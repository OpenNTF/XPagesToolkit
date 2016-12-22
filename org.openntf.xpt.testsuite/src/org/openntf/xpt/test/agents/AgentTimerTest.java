/**
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
package org.openntf.xpt.test.agents;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.openntf.xpt.agents.annotations.XPagesAgent;
import org.openntf.xpt.agents.timer.AgentTimer;

public class AgentTimerTest {

	private XPagesAgent m_XPA55min;

	@Before
	public void buildAll() {
		TestAgentMock ta = new TestAgentMock();
		m_XPA55min = ta.getClass().getAnnotation(XPagesAgent.class);
	}

	@Test
	public void testBuildTimerWithStart() {
		AgentTimer agt = AgentTimer.buildTimer(m_XPA55min);
		assertNotNull("AgentTimer was not created", agt);
	}

	@Test
	public void testIsTimeUpCalendarNow() {
		AgentTimer agt = AgentTimer.buildTimer(m_XPA55min);
		assertFalse("Time should not be up", agt.isTimeUp());
	}

	@Test
	public void testIsTimeUpCalendarNow54() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MINUTE, 54);
		AgentTimer agt = AgentTimer.buildTimer(m_XPA55min);
		assertFalse("Time should not be up", agt.isTimeUp(cal));
	}

	@Test
	public void testIsTimeUpCalendarNow56() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MINUTE, 56);
		AgentTimer agt = AgentTimer.buildTimer(m_XPA55min);
		assertTrue("Time should be up", agt.isTimeUp(cal));
	}

	@Test
	public void testNextTimer() {
		AgentTimer agt = AgentTimer.buildTimer(m_XPA55min);
		AgentTimer agtNext = agt.nextTimer();
		assertNotNull("AgentTimer was not created", agtNext);
	}

	@Test
	public void testGetNextRun() {
		AgentTimer agt = AgentTimer.buildTimer(m_XPA55min);
		Calendar agtNext = agt.getNextRun();
		assertNotNull("nextRun not created", agtNext);
	}

	@Test
	public void testInitialStart() {
		AgentTimer agt = AgentTimer.buildInitialTimer(m_XPA55min);
		assertTrue("Timer should be isTimeUp()", agt.isTimeUp());
	}
}
