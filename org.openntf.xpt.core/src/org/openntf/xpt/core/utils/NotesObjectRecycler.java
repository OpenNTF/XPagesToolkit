package org.openntf.xpt.core.utils;

import org.openntf.xpt.core.utils.logging.LoggerFactory;

import lotus.domino.Base;

public class NotesObjectRecycler {

	public static void recycle(Base... recyclingObjects) {
		for (Base torecycle : recyclingObjects) {
			if (torecycle != null) {
				try {
					torecycle.recycle();
				} catch (Exception ex) {
					LoggerFactory.logError(NotesObjectRecycler.class, "Error during recyle", ex);
				}
			}
		}
	}
}
