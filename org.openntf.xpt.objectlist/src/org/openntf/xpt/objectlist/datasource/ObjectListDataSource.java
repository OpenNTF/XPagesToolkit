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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.model.DataModel;

import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.model.AbstractDataSource;
import com.ibm.xsp.model.DataContainer;
import com.ibm.xsp.model.ModelDataSource;
import com.ibm.xsp.util.StateHolderUtil;

public class ObjectListDataSource extends AbstractDataSource implements ModelDataSource {

	private MethodBinding m_BuildValues;

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
		return new ObjectListDataContainer(getBeanId(), getUniqueId(), buildList());
	}

	public void sortList(String strAttribute, boolean ascending) {
		ObjectListDataContainer oldc = (ObjectListDataContainer) getDataContainer();
		oldc.sortList(strAttribute, ascending);
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
		Object[] state = new Object[2];
		state[0] = super.saveState(arg0);
		state[1] = StateHolderUtil.saveMethodBinding(getFacesContext(), m_BuildValues);
		return state;
	}

	@Override
	public void restoreState(FacesContext arg0, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(arg0, values[0]);
		m_BuildValues = StateHolderUtil.restoreMethodBinding(getFacesContext(), getComponent(), values[1]);
	}

	@Override
	public void refresh() {
		// instead of delegate to superclass, copy template in
		// com.ibm.xsp.extlib.model.DataAccessorSource.refresh()
		// to do a reduced refresh, that clears
		// the current value but doesn't re-load.
		FacesContext context = getFacesContext();
		if (context == null)
			return;

		// clear the current value
		putDataContainer(context, null);
	}

}
