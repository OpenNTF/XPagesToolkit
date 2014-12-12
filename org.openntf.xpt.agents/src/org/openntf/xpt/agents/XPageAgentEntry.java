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
package org.openntf.xpt.agents;

import java.io.Serializable;
import java.util.GregorianCalendar;

import org.openntf.xpt.agents.annotations.ExecutionDay;
import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.agents.annotations.XPagesAgent;
import org.openntf.xpt.agents.timer.AgentTimer;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

;

public class XPageAgentEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Class<XPageAgentJob> m_Agent;
	private final String m_Title;
	private final String m_Alias;
	private final ExecutionMode m_ExecutionMode;
	private final XPagesAgent m_XPA;

	// StateControlled
	private AgentTimer m_Timer;
	private boolean m_Active;
	private boolean m_Running = false;

	public static XPageAgentEntry buildXPagesAgentEntry(Class<XPageAgentJob> clAgent, XPagesAgent xpa, boolean active) {
		return new XPageAgentEntry(clAgent, xpa.Name(), xpa.Alias(), xpa.executionMode(), AgentTimer.buildInitialTimer(xpa), active, xpa);
	}

	private XPageAgentEntry(Class<XPageAgentJob> agent, String title, String alias, ExecutionMode executionMode, AgentTimer timer, boolean active, XPagesAgent xpa) {
		super();

		m_Agent = agent;
		m_Title = title;
		if (alias.contains(" ")) {
			alias = alias.replace(" ", "");
		}
		m_Alias = alias;
		m_ExecutionMode = executionMode;
		m_Timer = timer;
		m_XPA = xpa;
		m_Active = active;
		m_Running = false;
	}

	public boolean isRunning() {
		return m_Running;
	}

	public void runScheduled() {
		m_Running = true;
	}

	public void endSchedules() {
		LoggerFactory.logFine(this.getClass(), "endSchedules called!", null);
		LoggerFactory.logFine(this.getClass(), "Intervall is set to:" + m_Timer.getIntervall(), null);
		m_Running = false;
		if (m_Timer.getLastRun() == null) {
			LoggerFactory.logFine(this.getClass(), "getLastRun is null / nextRun was: " + m_Timer.getNextRun().getTime(), null);
			m_Timer = m_Timer.nextTimer(GregorianCalendar.getInstance());
		} else {
			LoggerFactory.logFine(this.getClass(), "getLast was: " + m_Timer.getLastRun().getTime() + " / nextRun was: " + m_Timer.getNextRun().getTime(), null);
			m_Timer = m_Timer.nextTimer();
		}
		LoggerFactory.logInfo(this.getClass(), "New Timer is set to: " + m_Timer.getNextRun().getTime(), null);
	}

	public Class<XPageAgentJob> getAgent() {
		return m_Agent;
	}

	public String getTitle() {
		return m_Title;
	}

	public String getAlias() {
		return m_Alias;
	}

	public ExecutionMode getExecutionMode() {
		return m_ExecutionMode;
	}

	public int getIntervall() {
		return m_Timer.getIntervall();
	}

	public void setIntervall(int intervall) {
		if (m_ExecutionMode.isScheduled()) {
			// TODO: was muss ich hier wirklich machen?
		}
	}

	public ExecutionDay[] getExecutionDay() {
		return m_Timer.getExecutionDay();
	}

	public int getExecTimeWindowStartHour() {
		return m_Timer.getExecTimeWindowStartHour();
	}

	public int getExecTimeWindowStartMinute() {
		return m_Timer.getExecTimeWindowStartMinute();
	}

	public int getExecTimeWindowEndHour() {
		return m_Timer.getExecTimeWindowEndHour();
	}

	public int getExecTimeWindowEndMinute() {
		return m_Timer.getExecTimeWindowEndMinute();
	}

	public boolean readyToExecuteScheduled() {
		if (!m_ExecutionMode.isScheduled()) {
			return false;
		}
		if (isRunning()) {
			return false;
		}
		return (m_Active && m_Timer.isTimeUp());
	}

	public boolean isActive() {
		return m_Active;
	}

	public void setActive(boolean active) {
		m_Timer = AgentTimer.buildInitialTimer(m_XPA);
		m_Active = active;
	}

}
