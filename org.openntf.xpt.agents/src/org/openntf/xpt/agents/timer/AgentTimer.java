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
package org.openntf.xpt.agents.timer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openntf.xpt.agents.annotations.ExecutionDay;
import org.openntf.xpt.agents.annotations.XPagesAgent;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

public final class AgentTimer {

	private final int m_Intervall;
	private final ExecutionDay[] m_ExecutionDay;
	private final int m_execTimeWindowStartHour;
	private final int m_execTimeWindowStartMinute;
	private final int m_execTimeWindowEndHour;
	private final int m_execTimeWindowEndMinute;

	private final Calendar m_LastRun;
	private final Calendar m_NextRun;
	private final String m_RelatedAgentName;

	public static AgentTimer buildTimer(XPagesAgent xpa) {
		return new AgentTimer(xpa.Alias(), xpa.intervall(), xpa.executionDay(), xpa.execTimeWindowStartHour(), xpa.execTimeWindowStartMinute(), xpa.execTimeWindowEndHour(),
				xpa.execTimeWindowEndMinute(), null);
	}

	public static AgentTimer buildTimerWithStart(XPagesAgent xpa, Calendar start) {
		return new AgentTimer(xpa.Alias(), xpa.intervall(), xpa.executionDay(), xpa.execTimeWindowStartHour(), xpa.execTimeWindowStartMinute(), xpa.execTimeWindowEndHour(),
				xpa.execTimeWindowEndMinute(), start);
	}

	private AgentTimer(String relatedAgentName, int intervall, ExecutionDay[] executionDay, int execTimeWindowStartHour, int execTimeWindowStartMinute, int execTimeWindowEndHour,
			int execTimeWindowEndMinute, Calendar lastRun) {
		super();
		m_Intervall = intervall;
		m_ExecutionDay = executionDay;
		m_execTimeWindowStartHour = execTimeWindowStartHour;
		m_execTimeWindowStartMinute = execTimeWindowStartMinute;
		m_execTimeWindowEndHour = execTimeWindowEndHour;
		m_execTimeWindowEndMinute = execTimeWindowEndMinute;
		m_LastRun = lastRun;
		m_NextRun = calcNextRun(m_LastRun);
		m_RelatedAgentName = relatedAgentName;
	}

	private Calendar calcNextRun(Calendar calCurrent) {
		if (calCurrent == null) {
			calCurrent = GregorianCalendar.getInstance();
		}
		Calendar calNextRun = (Calendar) calCurrent.clone();

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
					LoggerFactory.logError(this.getClass(), "Error in find next Day: " + m_RelatedAgentName, null);
				}
			}
		}
		return calNextRun;
	}

	public boolean isTimeUp() {
		return m_NextRun.before(GregorianCalendar.getInstance());
	}

	public boolean isTimeUp(Calendar cal) {
		return m_NextRun.before(cal);
	}

	public AgentTimer nextTimer() {
		return new AgentTimer(m_RelatedAgentName, m_Intervall, m_ExecutionDay, m_execTimeWindowStartHour, m_execTimeWindowStartMinute, m_execTimeWindowEndHour, m_execTimeWindowEndMinute, m_NextRun);
	}

	public AgentTimer nextTimer(Calendar calStart) {
		return new AgentTimer(m_RelatedAgentName, m_Intervall, m_ExecutionDay, m_execTimeWindowStartHour, m_execTimeWindowStartMinute, m_execTimeWindowEndHour, m_execTimeWindowEndMinute, calStart);
	}

	public AgentTimer changeIntervall(int minute) {
		return new AgentTimer(m_RelatedAgentName, minute, m_ExecutionDay, m_execTimeWindowStartHour, m_execTimeWindowStartMinute, m_execTimeWindowEndHour, m_execTimeWindowEndMinute, m_LastRun);
	}

	public int getIntervall() {
		return m_Intervall;
	}

	public ExecutionDay[] getExecutionDay() {
		return m_ExecutionDay;
	}

	public int getExecTimeWindowStartHour() {
		return m_execTimeWindowStartHour;
	}

	public int getExecTimeWindowStartMinute() {
		return m_execTimeWindowStartMinute;
	}

	public int getExecTimeWindowEndHour() {
		return m_execTimeWindowEndHour;
	}

	public int getExecTimeWindowEndMinute() {
		return m_execTimeWindowEndMinute;
	}

	public Calendar getLastRun() {
		return m_LastRun;
	}

	public Calendar getNextRun() {
		return m_NextRun;
	}

	public String getRelatedAgentName() {
		return m_RelatedAgentName;
	}
}
