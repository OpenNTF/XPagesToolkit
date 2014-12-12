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
