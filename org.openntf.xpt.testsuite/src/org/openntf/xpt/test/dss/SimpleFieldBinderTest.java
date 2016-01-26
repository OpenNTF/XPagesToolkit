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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.BinderContainer;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.field.BooleanBinder;
import org.openntf.xpt.core.dss.binding.field.BooleanClassBinder;
import org.openntf.xpt.core.dss.binding.field.DateBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleBinder;
import org.openntf.xpt.core.dss.binding.field.DoubleClassBinder;
import org.openntf.xpt.core.dss.binding.field.ENumBinder;
import org.openntf.xpt.core.dss.binding.field.IntBinder;
import org.openntf.xpt.core.dss.binding.field.IntClassBinder;
import org.openntf.xpt.core.dss.binding.field.ListStringBinder;
import org.openntf.xpt.core.dss.binding.field.LongBinder;
import org.openntf.xpt.core.dss.binding.field.LongClassBinder;
import org.openntf.xpt.core.dss.binding.field.StringArrayBinder;
import org.openntf.xpt.core.dss.binding.field.StringBinder;
import org.openntf.xpt.test.dss.mock.FieldBinderTestMock;

public class SimpleFieldBinderTest {

	@Test
	public void testBinderLoader() throws DSSException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(FieldBinderTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(13, d2jBinder.getDefinitions().size());
		Map<String, Definition> fieldDefinitionMap = buildFieldMap(d2jBinder.getDefinitions());

		checkBinder("stringField", StringBinder.class, fieldDefinitionMap);
		checkBinder("stringListField", ListStringBinder.class, fieldDefinitionMap);
		checkBinder("stringArrayField", StringArrayBinder.class, fieldDefinitionMap);
		checkBinder("boolTypeField", BooleanBinder.class, fieldDefinitionMap);
		checkBinder("boolClassField", BooleanClassBinder.class, fieldDefinitionMap);
		checkBinder("intTypeField", IntBinder.class, fieldDefinitionMap);
		checkBinder("intClassField", IntClassBinder.class, fieldDefinitionMap);
		checkBinder("doubleTypeField", DoubleBinder.class, fieldDefinitionMap);
		checkBinder("doubleClassField", DoubleClassBinder.class, fieldDefinitionMap);
		checkBinder("longTypeField", LongBinder.class, fieldDefinitionMap);
		checkBinder("longClassField", LongClassBinder.class, fieldDefinitionMap);
		checkBinder("dateField", DateBinder.class, fieldDefinitionMap);
		checkBinder("enumField", ENumBinder.class, fieldDefinitionMap);
	}

	private Map<String, Definition> buildFieldMap(List<Definition> definitions) {
		Map<String, Definition> map = new HashMap<String, Definition>();
		for (Definition def : definitions) {
			map.put(def.getNotesField(), def);
		}
		return map;
	}

	private void checkBinder(String fieldName, Class<?> binderClass, Map<String, Definition> fieldDefinitionMap) {
		Definition def = fieldDefinitionMap.get(fieldName);
		assertNotNull("Definition for " + fieldName + " not found.", def);
		assertEquals(binderClass, def.getBinder().getClass() );
	}
}
