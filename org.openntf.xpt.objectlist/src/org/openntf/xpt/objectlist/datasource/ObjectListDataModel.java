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
package org.openntf.xpt.objectlist.datasource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.xsp.model.TabularDataModel;

public class ObjectListDataModel extends TabularDataModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectListDataContainer m_Container;
	private List<String> selectedIds = new ArrayList<String>();


	public ObjectListDataModel(ObjectListDataContainer container) {
		super();
		m_Container = container;
		setWrappedData(m_Container.getObjectList());
	}

	@Override
	public int getRowCount() {
		return m_Container.getObjectList().size();
	}

	@Override
	public Object getRowData() {
		return m_Container.getObjectList().get(getRowIndex());
	}

	@Override
	public boolean isRowAvailable() {
		if (getRowIndex() < getRowCount()) {
			return true;
		}
		return false;
	}

	@Override
	public String getRowId() {
		return m_Container.getRowID(getRowIndex());
	}

	@Override
	public boolean isRowExpanded() {
		return true;
	}

	public ObjectListDataContainer getContainer() {
		return m_Container;
	}

	public void setContainer(ObjectListDataContainer container) {
		m_Container = container;
	}

	@Override
	public boolean isCategorized() {
		return false;
	}

	@Override
	public boolean isColumnSortable(String arg0) {
		return m_Container.containsSA(arg0);
	}

	@Override
	public boolean isRowTotal() {
		return false;
	}

	@Override
	public boolean isRowLeaf() {
		return true;
	}

	@Override
	public boolean isColumnCategorized(String arg0) {
		return false;
	}

	@Override
	public boolean isRowCategory() {
		return false;
	}

	@Override
	public String getRowPosition() {
		return "" + getRowIndex();
	}

	@Override
	public int getResortType(String arg0) {
		if (isColumnSortable(arg0)) {
			return m_Container.getSortDirection(arg0);
		}
		return RESORT_NONE;
	}

	@Override
	public void setResortOrder(String columnName, String sortOrder) {
		if (columnName.equals(m_Container.getCurrentSortAttribute())) {
			if (SORT_TOGGLE.equals(sortOrder)) {
				m_Container.sortList(columnName, !m_Container.getCurrentAscending());
				return;
			}
		}
		if (SORT_TOGGLE.equals(sortOrder)) {
			m_Container.sortList(columnName, true);
			return;
		}
		if (SORT_ASCENDING.equals(sortOrder)) {
			m_Container.sortList(columnName, true);
		}
		if (SORT_DESCENDING.equals(sortOrder)) {
			m_Container.sortList(columnName, false);
		}
	}

	@Override
	public String getResortColumn() {
		return m_Container.getCurrentSortAttribute();
	}

	@Override
	public int resetResortState(String arg0) {
		return RESORT_NONE;
	}

	@Override
	public int getResortState(String columnName) {
		if (!columnName.equals(m_Container.getCurrentSortAttribute())) {
			return RESORT_NONE;
		}
		if (getResortType(columnName) == RESORT_BOTH) {
			return m_Container.getCurrentAscending() ? RESORT_ASCENDING : RESORT_DESCENDING;
		} else {
			return !m_Container.getCurrentAscending() ? RESORT_ASCENDING : RESORT_DESCENDING;

		}
	}
	
	@Override
	public void addSelectedId(String id) {
		selectedIds.add(id);
	}
	
	@Override
	public void clearSelectedIds() {
		selectedIds.clear();
	}
	
	@Override
	public boolean isSelectedId(String id) {
		return selectedIds.contains(id);
	}
	
	@Override
	public void removeSelectedId(String id) {
		selectedIds.remove(id);
	}
	@Override
	public Iterator<String> getSelectedIds() {
		return selectedIds.iterator();
	}
	
}
