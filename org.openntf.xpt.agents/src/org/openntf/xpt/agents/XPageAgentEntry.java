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
import org.openntf.xpt.agents.annotations.ExecutionMode;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

;

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
	private int m_execTimeWindowStartHour = 0;
	private int m_execTimeWindowStartMinute = 0;
	private int m_execTimeWindowEndHour = 23;
	private int m_execTimeWindowEndMinute = 59;
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
			m_NextRun = calcNextRun(m_LastRun);
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
		if (alias.contains(" ")) {
			alias = alias.replace(" ", "");
		}
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
			m_NextRun = calcNextRun(m_LastRun);
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

	public Date calcNextRun(Date dtCurrent) {
		Calendar calCurrent = Calendar.getInstance();
		Calendar calNextRun = Calendar.getInstance();
		calCurrent.setTime(dtCurrent);
		calNextRun.setTime(dtCurrent);
		calNextRun.add(Calendar.MINUTE, m_Intervall);
		if (calNextRun.get(Calendar.HOUR_OF_DAY) > m_execTimeWindowEndHour || calNextRun.get(Calendar.HOUR_OF_DAY) > m_execTimeWindowEndHour
				&& calNextRun.get(Calendar.MINUTE) > m_execTimeWindowEndMinute) {
			if (m_execTimeWindowEndHour > m_execTimeWindowStartHour) {
				calNextRun.add(Calendar.DAY_OF_YEAR, 1);
			}
			calNextRun.set(Calendar.HOUR_OF_DAY, m_execTimeWindowStartHour);
			calNextRun.set(Calendar.MINUTE, m_execTimeWindowStartMinute);
		}
		if (calNextRun.get(Calendar.HOUR_OF_DAY) < m_execTimeWindowStartHour) {
			calNextRun.set(Calendar.HOUR_OF_DAY, m_execTimeWindowStartHour);
		}
		if (calNextRun.get(Calendar.HOUR_OF_DAY) == m_execTimeWindowStartHour && calNextRun.get(Calendar.MINUTE) < m_execTimeWindowStartMinute) {
			calNextRun.set(Calendar.MINUTE, m_execTimeWindowStartMinute);
		}
		if (m_ExecutionDay != null && m_ExecutionDay.length > 0 && m_ExecutionDay[0] != ExecutionDay.ALLDAY) {
			boolean blFound = false;
			int nCounter = 0;
			while (!blFound && nCounter < 8) {
				int nCurrentDay = calCurrent.get(Calendar.DAY_OF_WEEK);
				for (ExecutionDay ed : m_ExecutionDay) {
					if (ed == ExecutionDay.getByDateWeekday(nCurrentDay)) {
						blFound = true;
						break;
					}
				}
				calNextRun.add(Calendar.DAY_OF_YEAR, 1);
				nCounter++;
				if (nCounter > 8) {
					LoggerFactory.getLogger(this.getClass().getCanonicalName()).severe("Error in find next Day: " + m_Alias);
				}
			}
		}
		return calNextRun.getTime();
	}
}
