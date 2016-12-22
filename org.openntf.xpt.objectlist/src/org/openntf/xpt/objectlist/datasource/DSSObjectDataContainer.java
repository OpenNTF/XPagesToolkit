/**
 * Copyright 2014, WebGate Consulting AG
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

import org.openntf.xpt.core.XPTRuntimeException;

import com.ibm.xsp.model.AbstractDataContainer;

public class DSSObjectDataContainer extends AbstractDataContainer implements Serializable {

	private DSSObject m_DSSObject;

	public DSSObjectDataContainer() {
	}

	public DSSObjectDataContainer(String strBeanID, String strUniqueID, DSSObject dssObject) {
		super(strBeanID, strUniqueID);
		m_DSSObject = dssObject;
	}

	@Override
	public void deserialize(ObjectInput in) throws IOException {
		try {
			m_DSSObject = (DSSObject) in.readObject();
		} catch (Exception ex) {
			throw new XPTRuntimeException("DSSObjectDataContainer deserizalize failed!", ex);
		}
	}

	@Override
	public void serialize(ObjectOutput out) throws IOException {
		out.writeObject(m_DSSObject);
	}
	
	public DSSObject getDSSObject() {
		return m_DSSObject;
	}
	public Object getBO() {
		return m_DSSObject.getBO();
	}

}
