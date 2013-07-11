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
import java.util.List;

import com.ibm.xsp.model.AbstractDataContainer;

public class ObjectListDataContainer extends AbstractDataContainer {

	private List<ObjectListDataEntry> m_ObjectList;
	private ObjectListDataModel m_Model;

	public ObjectListDataContainer() {
		super();
	}

	public ObjectListDataContainer(String strBeanID, String strUniqueID,
			List<ObjectListDataEntry> objList) {
		super(strBeanID, strUniqueID);
		m_ObjectList = objList;
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
			m_ObjectList = (List<ObjectListDataEntry>) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void serialize(ObjectOutput out) throws IOException {
		out.writeObject(m_ObjectList);

	}

	public Object getData() {
		if (m_Model == null) {
			m_Model = new ObjectListDataModel(this);
		}
		return m_Model;
	}

}
