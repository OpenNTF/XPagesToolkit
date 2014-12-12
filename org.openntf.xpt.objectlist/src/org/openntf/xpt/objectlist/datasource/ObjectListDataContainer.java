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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.model.AbstractDataContainer;
import com.ibm.xsp.model.TabularDataModel;

public class ObjectListDataContainer extends AbstractDataContainer {

	private SortAttribute[] m_SortableAttributes;
	private List<ObjectListDataEntry> m_ObjectList;
	private ObjectListDataModel m_Model;

	private String m_CurrentSortAttribute;
	private Boolean m_CurrentAscending;

	public ObjectListDataContainer() {
	}

	public ObjectListDataContainer(String strBeanID, String strUniqueID, List<ObjectListDataEntry> objList, String[] sortValues) {
		super(strBeanID, strUniqueID);
		m_ObjectList = objList;
		m_SortableAttributes = buildSortValues(sortValues);
	}

	private SortAttribute[] buildSortValues(String[] sortValues) {
		List<SortAttribute> lstSA = new ArrayList<ObjectListDataContainer.SortAttribute>(sortValues.length);
		for (String sa : sortValues) {
			if (!StringUtil.isEmpty(sa)) {
				lstSA.add(new SortAttribute(sa));
			}
		}
		return lstSA.toArray(new SortAttribute[lstSA.size()]);
	}

	public List<ObjectListDataEntry> getObjectList() {
		return m_ObjectList;
	}

	public void setObjectList(List<ObjectListDataEntry> objectList) {
		m_ObjectList = objectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserialize(ObjectInput in) throws IOException {
		try {
			m_SortableAttributes = (SortAttribute[]) in.readObject();
			m_ObjectList = (List<ObjectListDataEntry>) in.readObject();
			m_CurrentAscending = in.readBoolean();
			m_CurrentSortAttribute = readUTF(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void serialize(ObjectOutput out) throws IOException {
		out.writeObject(m_SortableAttributes);
		out.writeObject(m_ObjectList);
		out.writeBoolean(m_CurrentAscending);
		writeUTF(out, m_CurrentSortAttribute);
	}

	public Object getData() {
		if (m_Model == null) {
			m_Model = new ObjectListDataModel(this);
		}
		return m_Model;
	}

	public void sortList(String strAttribute, boolean ascending) {
		if (m_ObjectList != null) {
			Collections.sort(m_ObjectList, new ObjectEntryComperator(strAttribute, ascending));
		}
		m_CurrentSortAttribute = strAttribute;
		m_CurrentAscending = ascending;
		if (m_Model != null) {
			m_Model.setRowIndex(0);
		}
	}

	public SortAttribute[] getSortableAttributes() {
		return m_SortableAttributes;
	}

	public boolean containsSA(String columnName) {
		for (SortAttribute sa : m_SortableAttributes) {
			if (sa.getAttribute().equals(columnName)) {
				return true;
			}
		}
		return false;
	}

	public int getSortDirection(String columnName) {
		for (SortAttribute sa : m_SortableAttributes) {
			if (sa.getAttribute().equals(columnName)) {
				return sa.getDirection();
			}
		}
		return TabularDataModel.RESORT_NONE;
	}

	public boolean hasSortAttribute() {
		return !StringUtil.isEmpty(m_CurrentSortAttribute);
	}

	public String getCurrentSortAttribute() {
		return m_CurrentSortAttribute;
	}

	public Boolean getCurrentAscending() {
		return m_CurrentAscending;
	}

	public void clearSort() {
		m_CurrentAscending = null;
		m_CurrentSortAttribute = null;

	}

	private static class SortAttribute implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String m_Attribute;
		private final int m_Direction;

		public SortAttribute(String strAttr) {
			int dir = 0;
			String strAt = "";
			if (!strAttr.contains("|")) {
				strAt = strAttr;
			} else {
				String[] arrAttr = strAttr.split("\\|");
				if (arrAttr.length > 1) {
					if ("UP".equalsIgnoreCase(arrAttr[1])) {
						dir = TabularDataModel.RESORT_ASCENDING;
					}
					if ("DOWN".equalsIgnoreCase(arrAttr[1])) {
						dir = TabularDataModel.RESORT_DESCENDING;
					}
					if ("BOTH".equalsIgnoreCase(arrAttr[1])) {
						dir = TabularDataModel.RESORT_BOTH;
					}
				}
				strAt = arrAttr[0];
			}
			if (dir == 0) {
				dir = TabularDataModel.RESORT_BOTH;
			}
			m_Direction = dir;
			m_Attribute = strAt;
		}

		public String getAttribute() {
			return m_Attribute;
		}

		public int getDirection() {
			return m_Direction;
		}

	}

}
