/*
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
package org.openntf.xpt.agents.component;

import java.util.Comparator;

public class UIAgentEntryComparator implements Comparator<UIAgentEntry> {
	private String m_SortType;
	private boolean m_ASC;
	public UIAgentEntryComparator(String sortType) {
		super();
		m_SortType = sortType;
		m_ASC = m_SortType.endsWith("_ASC"); 
	}

	@Override
	public int compare(UIAgentEntry arg0, UIAgentEntry arg1) {
		if (m_SortType.startsWith(UIAgentList.SORT_NAME)) {
			return arg0.getEntry().getTitle().compareTo(arg1.getEntry().getTitle()) * (m_ASC ? 1:-1);
		}
		if (m_SortType.startsWith(UIAgentList.SORT_ALIAS)) {
			return arg0.getEntry().getAlias().compareTo(arg1.getEntry().getAlias()) * (m_ASC ? 1:-1);
		}
		if (m_SortType.startsWith(UIAgentList.SORT_EXECUTION)) {
			return new Integer(arg0.getEntry().getIntervall()).compareTo(arg1.getEntry().getIntervall()) * (m_ASC ? 1:-1);
		}
		if (m_SortType.startsWith(UIAgentList.SORT_TYPE)) {
			return arg0.getEntry().getExecutionMode().compareTo(arg1.getEntry().getExecutionMode()) * (m_ASC ? 1:-1);
		}
		if (m_SortType.startsWith(UIAgentList.SORT_ACTIVE)) {
			return new Boolean(arg0.getEntry().isActive()).compareTo(arg1.getEntry().isActive()) * (m_ASC ? 1:-1);
		}
		return 0;
	}
	
	

}
