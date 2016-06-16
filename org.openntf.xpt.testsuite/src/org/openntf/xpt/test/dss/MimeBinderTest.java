package org.openntf.xpt.test.dss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openntf.xpt.core.dss.DSSException;
import org.openntf.xpt.core.dss.binding.BinderContainer;
import org.openntf.xpt.core.dss.binding.Definition;
import org.openntf.xpt.core.dss.binding.Domino2JavaBinder;
import org.openntf.xpt.core.dss.binding.field.ObjectBinder;
import org.openntf.xpt.test.dss.mock.PropertiesTestMock;

public class MimeBinderTest {
	
	@Test
	public void testMimeBinder() throws DSSException {
		BinderContainer container = new BinderContainer("");
		Domino2JavaBinder d2jBinder = container.getLoader(PropertiesTestMock.class);
		assertNotNull(d2jBinder);
		assertEquals(3, d2jBinder.getDefinitions().size());
		Definition props = getThePropsDefinition(d2jBinder.getDefinitions());
		assertNotNull(props);
		assertTrue(props.getBinder() instanceof ObjectBinder);
	}

	
	private Definition getThePropsDefinition(List<Definition> definitions) {
		for (Definition def : definitions) {
			if ("propsMime".equals(def.getNotesField())) {
				return def;
			}
		}
		return null;
	}

}
