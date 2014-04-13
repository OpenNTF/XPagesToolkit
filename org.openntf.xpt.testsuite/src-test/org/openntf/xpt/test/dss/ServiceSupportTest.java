package org.openntf.xpt.test.dss;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;
import org.openntf.xpt.core.utils.ServiceSupport;

public class ServiceSupportTest {

	@Test
	public void testGetClassFieldsUnique() {
		Collection<Field> classFields = ServiceSupport.getClassFields(myClass.class);
		assertEquals(1, classFields.size());
	}

	@Test
	public void testGetClassFieldsDiffrent() {
		Collection<Field> classFields = ServiceSupport.getClassFields(myClass2.class);
		assertEquals(2, classFields.size());
	}
	public static class mySuperClass {
		private String m_ID = "SuperClassID";
	}

	public static class myClass extends mySuperClass {
		private String m_ID = "theRealID";
	}
	
	public static class myClass2 extends mySuperClass {
		private String m_UNID = "unid";
	}
}
