package org.openntf.xpt.core.dss.binding.field;

import java.lang.reflect.Method;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.RichTextItem;

import org.openntf.xpt.core.XPTRuntimeException;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.IBinder;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.xsp.http.MimeMultipart;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.model.domino.wrapped.DominoRichTextItem;

public class DominoRichTextItemBinder implements IBinder<DominoRichTextItem> {

	private static DominoRichTextItemBinder m_Binder = new DominoRichTextItemBinder();

	public static DominoRichTextItemBinder getInstance() {
		return m_Binder;
	}

	private DominoRichTextItemBinder() {
	}

	@Override
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def) {
		try {
			Method mt = objCurrent.getClass().getMethod("set" + def.getJavaField(), DominoRichTextItem.class);
			mt.invoke(objCurrent, getValueFromStore(docCurrent, vecValues, def));
		} catch (Exception e) {
			LoggerFactory.logWarning(this.getClass(), "Error during processDomino2Java", e);
			throw new XPTRuntimeException("Error during processDomino2Java", e);
		}

	}

	@Override
	public DominoRichTextItem[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def) {
		DominoRichTextItem[] arrRC = new DominoRichTextItem[2];
		try {
			DominoRichTextItem drtOldValue = getValueFromStore(docCurrent, null, def);

			DominoRichTextItem drtValue = getValue(objCurrent, def.getJavaField());
			arrRC[0] = drtOldValue;
			arrRC[1] = drtValue;
			drtValue.getParent().setDocument(docCurrent);
			if (docCurrent.hasItem(def.getNotesField())) {
				drtValue.updateRichTextItem(1);
			} else {
				drtValue.addRichTextItem();
				drtValue.updateRichTextItem(1);
			}
		} catch (Exception ex) {
			LoggerFactory.logWarning(this.getClass(), "Error during processJava2Domino", ex);
			throw new XPTRuntimeException("Error during processJava2Domino", ex);

		}
		return arrRC;

	}

	@Override
	public DominoRichTextItem getValue(Object objCurrent, String strJavaField) {
		try {
			Method mt = objCurrent.getClass().getMethod("get" + strJavaField);
			return (DominoRichTextItem) mt.invoke(objCurrent);
		} catch (Exception ex) {
			LoggerFactory.logWarning(getClass(), "Error during do getValue()", ex);
			throw new XPTRuntimeException("Error during getValue()", ex);

		}
	}

	@Override
	public DominoRichTextItem getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException {
		DominoRichTextItem drtCurrent = null;
		try {
			DominoDocument dd = DominoDocument.wrap(docCurrent.getParentDatabase().getServer() + "!!" + docCurrent.getParentDatabase().getFilePath(), docCurrent, "", "", false, "", "");
			// dd.setDocument(docCurrent);
			MIMEEntity entity = docCurrent.getMIMEEntity(def.getNotesField());
			if (entity != null) {
				drtCurrent = new DominoRichTextItem(dd, entity, def.getNotesField());

			} else {
				Item itmCurrent = docCurrent.getFirstItem(def.getNotesField());
				if (itmCurrent != null && itmCurrent.getType() == Item.RICHTEXT) {
					RichTextItem rti = (RichTextItem) itmCurrent;
					drtCurrent = new DominoRichTextItem(dd, rti);
				} else {
					drtCurrent = new DominoRichTextItem(dd, docCurrent.createMIMEEntity(def.getNotesField()), def.getNotesField());
					drtCurrent.setNewValue(MimeMultipart.fromHTML(""));
				}
			}
		} catch (Exception e) {
			throw new XPTRuntimeException("Error initializing DominoRichTextItem", e);

		}
		return drtCurrent;
	}

}
