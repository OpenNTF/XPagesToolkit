package org.openntf.xpt.test.oneui;

import java.util.HashMap;
import java.util.Map;

import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.Database;

import org.junit.Test;
import org.openntf.xpt.oneui.component.UINamePicker;
import org.openntf.xpt.oneui.kernel.NamePickerProcessor;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class NamePickerProcessorTest {

	@Test
	public void testNamePickerProcessorWithValueNotFound() throws NotesException {

		Database dbCurrent = createNiceMock(Database.class);
		View view = createNiceMock(View.class);
		expect(dbCurrent.getView("$users")).andReturn(view);
		expect(view.getDocumentByKey("user000")).andReturn(null);
		replay(dbCurrent, view);

		UINamePicker uip = new UINamePicker();
		uip.setView("$users");
		String[] values = { "user000" };
		Map<String, String> result = new HashMap<String, String>();
		NamePickerProcessor.INSTANCE.buildDisplayLabelsFromDatabase(uip, values,result,dbCurrent);
		assertNotNull(result);
		assertTrue(result.containsKey("user000"));
		assertEquals("user000", result.get("user000"));
	}
}
