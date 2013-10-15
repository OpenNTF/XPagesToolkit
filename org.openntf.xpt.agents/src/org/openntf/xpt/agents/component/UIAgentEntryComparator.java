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
