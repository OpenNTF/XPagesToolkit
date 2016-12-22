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

import java.util.Comparator;

public class ObjectEntryComperator implements Comparator<ObjectListDataEntry> {

	private String m_Attribute;
	private boolean m_Ascending;

	public ObjectEntryComperator(String attribute, boolean ascending) {
		super();
		m_Attribute = attribute;
		m_Ascending = ascending;
	}

	@Override
	public int compare(ObjectListDataEntry arg0, ObjectListDataEntry arg1) {
		Object val1 = arg0.getColumnValue(m_Attribute);
		Object val2 = arg1.getColumnValue(m_Attribute);
		if (val1 == null && val2 == null) {
			return 0;
		}
		if (val1 == null) {
			return -1 * (m_Ascending ? 1 : -1);
		}
		if (val2 == null) {
			return 1 * (m_Ascending ? 1 : -1);
		}
		if (val1 instanceof String && val2 instanceof String) {
			String sVal1 = (String)val1;
			String sVal2 = (String)val2;
			return sVal1.toLowerCase().compareTo(sVal2.toLowerCase()) * (m_Ascending ? 1 : -1);
		}
		if (val1 instanceof Comparable<?> && val2 instanceof Comparable<?>) {
			@SuppressWarnings("unchecked")
			Comparable<Object> valO1 = (Comparable<Object>) val1;
			@SuppressWarnings("unchecked")
			Comparable<Object> valO2 = (Comparable<Object>) val2;

			return valO1.compareTo(valO2)* (m_Ascending ? 1 : -1);
		}
		return 0;
	}

}
