package org.openntf.xpt.test.json;

import org.junit.Test;
import org.openntf.xpt.core.dss.binding.BinderContainer;
import org.openntf.xpt.core.json.JsonBinderContainer;
import org.openntf.xpt.test.json.mock.UserMock;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import static org.junit.Assert.*;

public class Json2ObjectTest {
	private final String RESULT = "{\"accountnumber\":42,\"accountobject\":42,\"name\":\"Marco M\\u00FCller\",\"salary\":123000,\"salaryobject\":123000,\"tags\":[\"Manager\",\"Developer\",\"Leader\"]}";
	private final String RESULT_CHILDREND = "{\"accountnumber\":42,\"accountobject\":42,\"children\":[{\"accountnumber\":21,\"accountobject\":21,\"name\":\"Ren\\u00E9 Meier\",\"salary\":90000,\"salaryobject\":90000,\"tags\":[\"Developer\"]},{\"accountnumber\":19,\"accountobject\":19,\"name\":\"Cecile N\\u00FCnlist\",\"salary\":99000,\"salaryobject\":99000}],\"name\":\"Marco M\\u00FCller\",\"salary\":123000,\"salaryobject\":123000,\"tags\":[\"Manager\",\"Developer\",\"Leader\"]}";

	@Test
	public void testParseObjectFromString() throws JsonException {
		JsonJavaFactory factory = JsonJavaFactory.instanceEx;
		JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, RESULT);
		JsonBinderContainer container = new JsonBinderContainer();
		UserMock userMock = new UserMock();
		container.processJson2Object(json, userMock);
		assertNotNull(userMock);
		assertEquals("Marco Müller", userMock.getName());
		assertEquals(42, userMock.getAccountNumber());
		assertEquals(new Integer(42), userMock.getAccountObject());
		assertEquals(3, userMock.getTags().size());
	}
	
	@Test
	public void testParseObjectFromStringWithChildren() throws JsonException {
		JsonJavaFactory factory = JsonJavaFactory.instanceEx;
		JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, RESULT_CHILDREND);
		JsonBinderContainer container = new JsonBinderContainer();
		UserMock userMock = new UserMock();
		container.processJson2Object(json, userMock);
		assertNotNull(userMock);
		assertEquals("Marco Müller", userMock.getName());
		assertEquals(42, userMock.getAccountNumber());
		assertEquals(new Integer(42), userMock.getAccountObject());
		assertEquals(3, userMock.getTags().size());
		assertEquals(2, userMock.getChildren().size());
		assertEquals("René Meier", userMock.getChildren().get(0).getName());
	}

}
