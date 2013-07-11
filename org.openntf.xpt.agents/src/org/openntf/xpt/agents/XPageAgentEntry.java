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
import java.util.Calendar;
import java.util.Date;

import org.openntf.xpt.agents.annotations.ExecutionDay;
import org.openntf.xpt.agents.annotations.ExecutionMode;;

public class XPageAgentEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Class<XPageAgentJob> m_Agent;
	private String m_Title;
	private String m_Alias;
	private ExecutionMode m_ExecutionMode;
	private int m_Intervall;
	private ExecutionDay[] m_ExecutionDay;
	private int m_execTimeWindowStartHour;
	private int m_execTimeWindowStartMinute;
	private int m_execTimeWindowEndHour;
	private int m_execTimeWindowEndMinute;
	private boolean m_Active;
	
	

	private Date m_LastRun;
	private Date m_NextRun;

	private boolean m_Running = false;

	public boolean isRunning() {
		return m_Running;
	}

	public void setRunning(boolean running) {
		if (!running && m_ExecutionMode.isScheduled()) {
			m_LastRun = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(m_LastRun);
			cal.add(Calendar.MINUTE, m_Intervall);
			m_NextRun = cal.getTime();
		}
		m_Running = running;
	}

	public Class<XPageAgentJob> getAgent() {
		return m_Agent;
	}

	public void setAgent(Class<XPageAgentJob> agent) {
		m_Agent = agent;
	}

	public String getTitle() {
		return m_Title;
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public String getAlias() {
		return m_Alias;
	}

	public void setAlias(String alias) {
		m_Alias = alias;
	}

	public ExecutionMode getExecutionMode() {
		return m_ExecutionMode;
	}

	public void setExecutionMode(ExecutionMode executionMode) {
		m_ExecutionMode = executionMode;
	}

	public int getIntervall() {
		return m_Intervall;
	}

	public void setIntervall(int intervall) {
		if (m_ExecutionMode.isScheduled()) {
			m_LastRun = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(m_LastRun);
			cal.add(Calendar.MINUTE, intervall);
			m_NextRun = cal.getTime();
			m_Intervall = intervall;
		}
	}

	public ExecutionDay[] getExecutionDay() {
		return m_ExecutionDay;
	}

	public void setExecutionDay(ExecutionDay[] executionDay) {
		m_ExecutionDay = executionDay;
	}

	public int getExecTimeWindowStartHour() {
		return m_execTimeWindowStartHour;
	}

	public void setExecTimeWindowStartHour(int execTimeWindowStartHour) {
		m_execTimeWindowStartHour = execTimeWindowStartHour;
	}

	public int getExecTimeWindowStartMinute() {
		return m_execTimeWindowStartMinute;
	}

	public void setExecTimeWindowStartMinute(int execTimeWindowStartMinute) {
		m_execTimeWindowStartMinute = execTimeWindowStartMinute;
	}

	public int getExecTimeWindowEndHour() {
		return m_execTimeWindowEndHour;
	}

	public void setExecTimeWindowEndHour(int execTimeWindowEndHour) {
		m_execTimeWindowEndHour = execTimeWindowEndHour;
	}

	public int getExecTimeWindowEndMinute() {
		return m_execTimeWindowEndMinute;
	}

	public void setExecTimeWindowEndMinute(int execTimeWindowEndMinute) {
		m_execTimeWindowEndMinute = execTimeWindowEndMinute;
	}

	
	public boolean readyToExecute() {
		if (!m_ExecutionMode.isScheduled()) {
			return false;
		}
		return m_Active && m_NextRun.before(new Date());
	}

	public boolean isActive() {
		return m_Active;
	}

	public void setActive(boolean active) {
		m_Active = active;
	}

}
