package org.openntf.xpt.test.objectlist;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.openntf.xpt.objectlist.utils.ListProcessor;

public class ListProcessorTest {

	private List<?> m_StringList;
	private List<?> m_DoubleList;
	private List<?> m_VectorList;
	private List<?> m_LinkedList;

	public ListProcessorTest() {
	}

	@Before
	public void initAllLists() {
		Vector<String> vecTest = new Vector<String>();
		vecTest.add("test");
		vecTest.add("test2");
		m_StringList = ListProcessor.INSTANCE.process2List(List.class, "test");
		m_DoubleList = ListProcessor.INSTANCE.process2List(List.class, 55.1);
		m_VectorList = ListProcessor.INSTANCE.process2List(List.class, vecTest);
		m_LinkedList = ListProcessor.INSTANCE.process2List(LinkedList.class, "test");

	}

	@Test
	public void testHasOneElement() {
		assertEquals("STRING: List contains one element", 1, m_StringList.size());
	}

	@Test
	public void testElementIsString() {
		assertEquals("STRING: List contains string", String.class, m_StringList.get(0).getClass());
	}

	@Test
	public void testListIsArrayList() {
		assertEquals("STRING: List is ArrayList", ArrayList.class, m_StringList.getClass());
	}

	@Test
	public void testElementCalledTest() {
		assertEquals("STRING: Is the value of the 1. element = test", "test", m_StringList.get(0));
	}

	@Test
	public void testHasOneElementDobule() {
		assertEquals("DOUBLE: List contains one element", 1, m_DoubleList.size());
	}

	@Test
	public void testElementIsSDouble() {
		assertEquals("DOUBLE: List contains string", Double.class, m_DoubleList.get(0).getClass());
	}

	@Test
	public void testListIsArrayListDouble() {
		assertEquals("DOUBLE: List is ArrayList", ArrayList.class, m_DoubleList.getClass());
	}

	@Test
	public void testElementCalled551() {
		assertEquals("DOUBLE: Is the value of the 1. element = 55.1", 55.1, m_DoubleList.get(0));
	}

	@Test
	public void testHas2ElementsVector() {
		assertEquals("VECTOR: List contains one element", 2, m_VectorList.size());
	}

	@Test
	public void testElementIsStringVector() {
		assertEquals("VECTOR: List contains string", String.class, m_VectorList.get(0).getClass());
	}

	@Test
	public void testListIsVector() {
		assertEquals("VECTOR: List is Vector", Vector.class, m_VectorList.getClass());
	}

	@Test
	public void testLastElementVector() {
		assertEquals("VECTOR: Is the value of the last element = test2", "test2", m_VectorList.get(m_VectorList.size() - 1));
	}

	@Test
	public void testHas1ElementsLinkedList() {
		assertEquals("LINKEDLIST: List contains one element", 1, m_LinkedList.size());
	}

	@Test
	public void testElementIsStringLinkedList() {
		assertEquals("LINKEDLIST: List contains string", String.class, m_LinkedList.get(0).getClass());
	}

	@Test
	public void testListIsLinkedList() {
		assertEquals("LINKEDLIST: List is Vector", LinkedList.class, m_LinkedList.getClass());
	}

	@Test
	public void testFirstElementLinkedList() {
		assertEquals("LINKEDLIST: Is the value of the last element = test", "test", m_LinkedList.get(0));
	}

}
