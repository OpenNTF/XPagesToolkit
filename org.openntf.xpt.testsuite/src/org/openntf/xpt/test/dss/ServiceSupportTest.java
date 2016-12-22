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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Test;
import org.openntf.xpt.core.utils.ServiceSupport;

public class ServiceSupportTest {

	@Test
	public void testGetClassFieldsUnique() {
		Collection<Field> classFields = ServiceSupport.getClassFields(ChildClassSameFieldName.class);
		assertEquals(1, classFields.size());
	}

	@Test
	public void testGetClassFieldsDiffrent() {
		Collection<Field> classFields = ServiceSupport.getClassFields(ChildClassDiffrentFieldNames.class);
		assertEquals(2, classFields.size());
	}

	@Test
	public void testGetStandardGetterMethod() {
		try {
			Method getterString = ServiceSupport.getGetterMethod(ChildClassWithBooleanAndStringGetter.class, "string");
			assertEquals("getString", getterString.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testGetBooleanGetterMethod() {
		try {
			Method getterBoolean = ServiceSupport.getGetterMethod(ChildClassWithBooleanAndStringGetter.class, "bool");
			assertEquals("isBool", getterBoolean.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testGetNoMethod() {
		try {
			ServiceSupport.getGetterMethod(ChildClassWithBooleanAndStringGetter.class, "notavailablemethod");
			fail("No Exception trown!");
		} catch (Exception ex) {
			assertTrue("Wrong Exception: " + ex.getClass().getCanonicalName(), ex instanceof NoSuchMethodException);
			return;
		}
	}

	public static class TheSuperClass {
		private String m_ID = "SuperClassID";

		public String getID() {
			return m_ID;
		}

		public void setID(String iD) {
			m_ID = iD;
		}
	}

	public static class ChildClassSameFieldName extends TheSuperClass {
		private String m_ID = "theRealID";

		public String getID() {
			return m_ID;
		}

		public void setID(String iD) {
			m_ID = iD;
		}
	}

	public static class ChildClassDiffrentFieldNames extends TheSuperClass {
		private String m_UNID = "unid";

		public String getUNID() {
			return m_UNID;
		}

		public void setUNID(String uNID) {
			m_UNID = uNID;
		}
	}

	public static class ChildClassWithBooleanAndStringGetter extends TheSuperClass {
		private String m_ID = "theRealID";
		private boolean m_Bool = false;
		private String m_String = "";

		public boolean isBool() {
			return m_Bool;
		}

		public void setBool(boolean bool) {
			m_Bool = bool;
		}

		public String getString() {
			return m_String;
		}

		public void setString(String string) {
			m_String = string;
		}

		public String getID() {
			return m_ID;
		}

		public void setID(String iD) {
			m_ID = iD;
		}

	}

}
