package org.openntf.xpt.test.dss;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.Database;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.openntf.xpt.test.dss.mock.ListDoubleIntStorageServiceIncomplete;

import static org.junit.Assert.*;

public class AbstractStorageServiceTest {

	@Test
	public void testBuildObjectFailsWithExcpetion() {
		try {
			ListDoubleIntStorageServiceIncomplete.getInstance().prepareNewObject();
			assertFalse(true);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetByIDFailsWithExcpetion() {
		try {
			ListDoubleIntStorageServiceIncomplete.getInstance().getByIdFrom("1232",null);
			assertFalse(true);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testGetAllFromViewFailsExcpetion() throws NotesException {
		try {
			Database sourceDatabase = createNiceMock(Database.class);
			View viewMock = createNiceMock(View.class);
			Document docNext = createNiceMock(Document.class);
			Document docNextNext = createNiceMock(Document.class);
			expect(sourceDatabase.getView("myView")).andReturn(viewMock);
			//expect(viewMock.setAutoUpdate(true));
			expectLastCall();
			expect(viewMock.getFirstDocument()).andReturn(docNext);
			expect(viewMock.getNextDocument(docNext)).andReturn(docNextNext);
			replay(sourceDatabase);
			replay(viewMock);
			replay(docNext);
			replay(docNextNext);

			ListDoubleIntStorageServiceIncomplete.getInstance().getAllFrom("myView", sourceDatabase);
			assertFalse(true);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		
	}

}
