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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import lotus.domino.Document;
import lotus.domino.NotesException;

import org.easymock.EasyMock;
import org.junit.Test;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.BinderContainer;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.field.ListDoubleBinder;
import org.openntf.xpt.core.dss.binding.field.ListIntegerBinder;
import org.openntf.xpt.test.dss.mock.ListDoubleIntTestMock;

public class ListDoubleBinderTest {

	private static List<Double> DOUBLE_VALUES = Arrays.asList(1.2, 3.5, 6.6);
	private static List<Integer> INT_VALUES = Arrays.asList(1, 3, 6);
	private static Vector<Double> NUMBERS_DOUBLES = new Vector<Double>(DOUBLE_VALUES);
	private static Vector<Integer> NUMBERS_INTEGERS = new Vector<Integer>(INT_VALUES);

	@Test
	public void testBinderLoader() throws DSSException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(ListDoubleIntTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(2, d2jBinder.getDefinitions().size());
		List<Definition> definitions = d2jBinder.getDefinitions();
		Definition defDouble = getTheDoubleDefinition(definitions);
		assertNotNull(defDouble);
		assertTrue(defDouble.getBinder() instanceof ListDoubleBinder);
		Definition defInteger = getTheIntegerDefinition(definitions);
		assertNotNull(defInteger);
		assertTrue(defInteger.getBinder() instanceof ListIntegerBinder);
	}

	@Test
	public void testDoubleBinder2Domino() throws DSSException, NotesException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(ListDoubleIntTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(2, d2jBinder.getDefinitions().size());
		List<Definition> definitions = d2jBinder.getDefinitions();
		Definition defDouble = getTheDoubleDefinition(definitions);
		assertNotNull(defDouble);
		assertTrue(defDouble.getBinder() instanceof ListDoubleBinder);
		ListDoubleIntTestMock mock = new ListDoubleIntTestMock();
		mock.setDoubleList(DOUBLE_VALUES);
		Document docTest = EasyMock.createNiceMock(Document.class);
		expect(docTest.getItemValue(defDouble.getNotesField())).andReturn(NUMBERS_DOUBLES);
		expect(docTest.replaceItemValue(defDouble.getNotesField(), DOUBLE_VALUES)).andReturn(null);
		replay(docTest);
		defDouble.getBinder().processJava2Domino(docTest, mock, defDouble);
		verify(docTest);
	}

	@Test
	public void testDoubleBinderFromDomino() throws DSSException, NotesException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(ListDoubleIntTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(2, d2jBinder.getDefinitions().size());
		List<Definition> definitions = d2jBinder.getDefinitions();
		Definition defDouble = getTheDoubleDefinition(definitions);
		assertNotNull(defDouble);
		assertTrue(defDouble.getBinder() instanceof ListDoubleBinder);
		ListDoubleIntTestMock mock = new ListDoubleIntTestMock();
		Document docTest = EasyMock.createNiceMock(Document.class);
		expect(docTest.getItemValue(defDouble.getNotesField())).andReturn(NUMBERS_DOUBLES);
		replay(docTest);
		defDouble.getBinder().processDomino2Java(docTest, mock, docTest.getItemValue(defDouble.getNotesField()), defDouble);
		verify(docTest);
		assertEquals(DOUBLE_VALUES, mock.getDoubleList());

	}

	@Test
	public void testIntegerBinder2Domino() throws DSSException, NotesException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(ListDoubleIntTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(2, d2jBinder.getDefinitions().size());
		List<Definition> definitions = d2jBinder.getDefinitions();
		Definition defInteger = getTheIntegerDefinition(definitions);
		assertNotNull(defInteger);
		assertTrue(defInteger.getBinder() instanceof ListIntegerBinder);
		ListDoubleIntTestMock mock = new ListDoubleIntTestMock();
		mock.setIntegerList(INT_VALUES);
		Document docTest = EasyMock.createNiceMock(Document.class);
		expect(docTest.getItemValue(defInteger.getNotesField())).andReturn(NUMBERS_INTEGERS);
		expect(docTest.replaceItemValue(defInteger.getNotesField(), INT_VALUES)).andReturn(null);
		replay(docTest);
		defInteger.getBinder().processJava2Domino(docTest, mock, defInteger);
		verify(docTest);
	}

	@Test
	public void testIntegerBinderFromDomino() throws DSSException, NotesException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(ListDoubleIntTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(2, d2jBinder.getDefinitions().size());
		List<Definition> definitions = d2jBinder.getDefinitions();
		Definition defInteger = getTheIntegerDefinition(definitions);
		assertNotNull(defInteger);
		assertTrue(defInteger.getBinder() instanceof ListIntegerBinder);
		ListDoubleIntTestMock mock = new ListDoubleIntTestMock();
		Document docTest = EasyMock.createNiceMock(Document.class);
		expect(docTest.getItemValue(defInteger.getNotesField())).andReturn(NUMBERS_INTEGERS);
		replay(docTest);
		defInteger.getBinder().processDomino2Java(docTest, mock, docTest.getItemValue(defInteger.getNotesField()), defInteger);
		verify(docTest);
		assertEquals(INT_VALUES, mock.getIntegerList());

	}

	private Definition getTheDoubleDefinition(List<Definition> definitions) {
		for (Definition def : definitions) {
			if ("doubleListField".equals(def.getNotesField())) {
				return def;
			}
		}
		return null;
	}
	private Definition getTheIntegerDefinition(List<Definition> definitions) {
		for (Definition def : definitions) {
			if ("integerListField".equals(def.getNotesField())) {
				return def;
			}
		}
		return null;
	}
}
