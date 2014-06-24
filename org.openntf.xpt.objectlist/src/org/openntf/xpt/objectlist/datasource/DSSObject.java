package org.openntf.xpt.objectlist.datasource;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.dss.DominoStorageService;
import org.openntf.xpt.core.dss.annotations.XPTPresentationControl;
import org.openntf.xpt.core.utils.RoleAndGroupProvider;
import org.openntf.xpt.core.utils.ServiceSupport;
import org.openntf.xpt.core.utils.logging.LoggerFactory;
import org.openntf.xpt.objectlist.utils.DominoRichTextItemProcessor;
import org.openntf.xpt.objectlist.utils.ListProcessor;
import org.openntf.xpt.objectlist.utils.MIMEMultipartProcessor;

import com.ibm.xsp.component.UIFileuploadEx;
import com.ibm.xsp.component.UIInputRichText;
import com.ibm.xsp.http.IMimeMultipart;
import com.ibm.xsp.http.MimeMultipart;
import com.ibm.xsp.model.DataObject;
import com.ibm.xsp.model.domino.wrapped.DominoRichTextItem;

public class DSSObject implements DataObject, Serializable {

	private static final long serialVersionUID = 1L;
	private final Object m_BO;
	private final Map<String, Class<?>> m_Fields = new HashMap<String, Class<?>>();
	private final boolean m_EditMode;
	private final boolean m_IsNew;

	public DSSObject(Object obj, boolean editMode, boolean isNew) {
		m_BO = obj;
		m_EditMode = editMode;
		m_IsNew = isNew;
	}

	@Override
	public Class<?> getType(Object field) {
		String elField = "" + field;
		if (m_Fields.containsKey(elField)) {
			return m_Fields.get(elField);
		}
		try {
			Method mt = ServiceSupport.getGetterMethod(m_BO.getClass(), elField);
			m_Fields.put(elField, mt.getReturnType());
			return mt.getReturnType();
		} catch (Exception ex) {
			throw new XPTRuntimeException("Error during getType for " + elField, ex);
		}
	}

	@Override
	public Object getValue(Object field) {
		String elField = "" + field;
		try {
			Method mt = ServiceSupport.getGetterMethod(m_BO.getClass(), elField);
			m_Fields.put(elField, mt.getReturnType());
			if (mt.getReturnType().equals(DominoRichTextItem.class)) {
				DominoRichTextItem dtr = (DominoRichTextItem) mt.invoke(m_BO);
				dtr.getParent().restoreWrappedDocument();
				return dtr;
			}
			return mt.invoke(m_BO);
		} catch (Exception ex) {
			throw new XPTRuntimeException("Error during getValue for " + elField, ex);
		}

	}

	@Override
	public boolean isReadOnly(Object field) {
		if (m_EditMode) {
			try {
				XPTPresentationControl ctrl = DominoStorageService.getInstance().getXPTPresentationControl(m_BO, "" + field);
				if (ctrl == null) {
					return false;
				} else {
					if (ctrl.editNewOnly() && !m_IsNew) {
						return true;
					}
					if (ctrl.modifyOnly() && m_IsNew) {
						return true;
					}
					if (ctrl.editRoles().length == 0) {
						return false;
					} else {
						List<String> lstEditors = Arrays.asList(ctrl.editRoles());
						for (String strRole : RoleAndGroupProvider.getInstance().getMyGroupsAndRoles()) {
							if (lstEditors.contains(strRole)) {
								return false;
							}
						}
					}
					return true;
				}
			} catch (Exception ex) {
				throw new XPTRuntimeException("Error during isReadOnly for " + field, ex);
			}
		} else {
			return true;
		}
	}

	@Override
	public void setValue(Object field, Object value) {
		String elField = "" + field;
		Object value4setter = value;
		Class<?> setterClass = null;
		try {
			setterClass = getType(field);
			if (value != null && !setterClass.getCanonicalName().equals(value.getClass().getCanonicalName())) {
				LoggerFactory.logInfo(this.getClass(), "The class for arg1 is:" + value.getClass() + " but should be: " + setterClass, null);
				if ("com.ibm.xsp.http.MimeMultipart".equals(setterClass.getCanonicalName()) && value instanceof UIInputRichText.EmbeddedImage) {
					MimeMultipart mm = MIMEMultipartProcessor.INSTANCE.addEmbeddedImage((UIInputRichText.EmbeddedImage) value, (MimeMultipart) getValue(elField));
					value4setter = mm;
				}
				if (setterClass.equals(List.class) || Arrays.asList(setterClass.getInterfaces()).contains(java.util.List.class)) {
					value4setter = ListProcessor.INSTANCE.process2List(setterClass, value);
				}
				if (setterClass.equals(DominoRichTextItem.class)) {
					// EXIT after Execution of the DomionRichTextItemProcessor
					if (value instanceof UIFileuploadEx.UploadedFile) {
						DominoRichTextItemProcessor.INSTANCE.addMFileUpload((UIFileuploadEx.UploadedFile) value, (DominoRichTextItem) getValue(elField));
						return;
					}
					if (value instanceof UIInputRichText.EmbeddedImage) {
						DominoRichTextItemProcessor.INSTANCE.addEmbeddedImage((UIInputRichText.EmbeddedImage) value, (DominoRichTextItem) getValue(elField));
						return;
					}
					if (value instanceof IMimeMultipart) {
						DominoRichTextItemProcessor.INSTANCE.addMimeMultiPart((IMimeMultipart) value, (DominoRichTextItem) getValue(elField));
						return;
					}
				}
			}
			String strMethode = ServiceSupport.makeSetter(elField);
			Method mt = m_BO.getClass().getMethod(strMethode, setterClass);
			mt.invoke(m_BO, value4setter);

		} catch (Exception ex) {
			LoggerFactory.logError(this.getClass(), "Error in setValue for " + elField + "-> FIELDCLASS: " + setterClass == null ? "null" : setterClass.getCanonicalName() + " ValueClass: "
					+ value.getClass().getCanonicalName(), ex);
			throw new XPTRuntimeException("Error during setValue for " + elField, ex);
		}
	}

	public Object getBO() {
		return m_BO;
	}

	public boolean isEditable() {
		return m_EditMode;
	}

}
