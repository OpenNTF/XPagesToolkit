package org.openntf.xpt.objectlist.utils;

public enum SortOrder {
	NONE,ASC,DESC;
	
	public boolean isAscending() {
		return this == ASC;
	}
}
