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
