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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.model.DataModel;

import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.model.AbstractDataSource;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.model.ModelDataSource;
import com.ibm.xsp.util.StateHolderUtil;

public class ObjectListDataSource extends AbstractDataSource implements ModelDataSource {

	private MethodBinding m_BuildValues;

	private String m_SortAttribute;
	private Boolean m_Ascending;

	private List<String> m_SortableAttributes;

	public MethodBinding getBuildValues() {
		return m_BuildValues;
	}

	public void setBuildValues(MethodBinding buildValues) {
		m_BuildValues = buildValues;
	}

	@Override
	public DataModel getDataModel() {
		return (DataModel) ((ObjectListDataContainer) getDataContainer()).getData();
	}

	@Override
	protected String composeUniqueId() {
		/*
		 * StringBuilder sb = new StringBuilder(); sb.append("OBJECTLIST|");
		 * String strPrefix = getRequestParamPrefix(); if
		 * (!StringUtil.isEmpty(strPrefix)) { sb.append(strPrefix);
		 * sb.append("|"); } sb.append(UUID.randomUUID()); return sb.toString();
		 */
		return getClass().getName();
	}

	@Override
	public Object getDataObject() {
		return ((ObjectListDataContainer) getDataContainer()).getData();
	}

	@Override
	public boolean isReadonly() {
		return true;
	}

	@Override
	public DataContainer load(FacesContext arg0) throws IOException {
		ObjectListDataContainer dtC = new ObjectListDataContainer(getBeanId(), getUniqueId(), buildList(), getSortableAttributesArray());
		if (dtC.hasSortAttribute()) {
			dtC.sortList(dtC.getCurrentSortAttribute(), dtC.getCurrentAscending());

		} else {
			if (m_SortAttribute != null) {
				if (m_Ascending != null) {
					dtC.sortList(m_SortAttribute, m_Ascending);
				} else {
					dtC.sortList(m_SortAttribute, true);
				}
			}
		}
		return dtC;
	}

	public void sortList(String strAttribute, boolean ascending) {
		ObjectListDataContainer oldc = (ObjectListDataContainer) getDataContainer();
		oldc.sortList(strAttribute, ascending);
	}

	public void clearSort() {
		ObjectListDataContainer oldc = (ObjectListDataContainer) getDataContainer();
		oldc.clearSort();
	}

	private List<ObjectListDataEntry> buildList() throws IOException {
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		if (m_BuildValues != null) {
			logCurrent.info("Exectue BuildValues");
			Object objCurrent = m_BuildValues.invoke(getFacesContext(), null);
			logCurrent.info(objCurrent.getClass().getCanonicalName());
			if (objCurrent instanceof List<?>) {
				List<?> lstBrowse = (List<?>) objCurrent;
				List<ObjectListDataEntry> lstRC = new ArrayList<ObjectListDataEntry>(lstBrowse.size());
				for (Object obj : lstBrowse) {
					ObjectListDataEntry olde = new ObjectListDataEntry(obj);
					lstRC.add(olde);
				}
				return lstRC;
			} else {
				throw new IOException("buildValues must return a java.util.List Object");
			}
		}
		return null;
	}

	@Override
	public void readRequestParams(FacesContext arg0, Map<String, Object> arg1) {
	}

	@Override
	public boolean save(FacesContext arg0, DataContainer arg1) throws FacesExceptionEx {
		return false;
	}

	// SAVE and RESTOR of Datas
	@Override
	public Object saveState(FacesContext arg0) {
		Object[] state = new Object[5];
		state[0] = super.saveState(arg0);
		state[1] = StateHolderUtil.saveMethodBinding(getFacesContext(), m_BuildValues);
		state[2] = m_SortAttribute;
		state[3] = m_Ascending;
		state[4] = getSortableAttributesArray();
		return state;
	}

	@Override
	public void restoreState(FacesContext arg0, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(arg0, values[0]);
		m_BuildValues = StateHolderUtil.restoreMethodBinding(getFacesContext(), getComponent(), values[1]);
		m_SortAttribute = (String) values[2];
		m_Ascending = (Boolean) values[3];
		m_SortableAttributes = Arrays.asList((String[])values[4]);
	}

	@Override
	public void refresh() {
		FacesContext context = getFacesContext();
		if (context == null)
			return;

		// clear the current value
		putDataContainer(context, null);
	}

	public String getSortAttribute() {
		return m_SortAttribute;
	}

	public void setSortAttribute(String sortAttribute) {
		m_SortAttribute = sortAttribute;
	}

	public boolean getAscending() {
		return m_Ascending;
	}

	public void setAscending(boolean ascending) {
		m_Ascending = ascending;
	}

	public List<String> getSortableAttributes() {
		return m_SortableAttributes;
	}

	public void setSortableAttributes(List<String> sortableAttributes) {
		m_SortableAttributes = sortableAttributes;
	}

	public void addSortableAttribute(String strAttribute) {
		if (m_SortableAttributes == null) {
			m_SortableAttributes = new ArrayList<String>();
		}
		m_SortableAttributes.add(strAttribute);
	}

	private String[] getSortableAttributesArray() {
		if (getSortableAttributes() == null) {
			return new String[0];
		}
		List<String> lstSA = getSortableAttributes();
		return lstSA.toArray(new String[lstSA.size()]);
	}

}
