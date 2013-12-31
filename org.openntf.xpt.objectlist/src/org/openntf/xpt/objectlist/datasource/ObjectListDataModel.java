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
package org.openntf.xpt.objectlist.datasource;

import java.io.Serializable;

import com.ibm.xsp.model.TabularDataModel;

public class ObjectListDataModel extends TabularDataModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectListDataContainer m_Container;

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
		return "" + getRowIndex();
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
		return true;
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

}
