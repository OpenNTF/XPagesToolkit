package org.openntf.xpt.test.json;

import java.io.StringWriter;

import org.junit.Ignore;
import org.junit.Test;
import org.openntf.xpt.core.json.JSONService;
import org.openntf.xpt.core.json.JsonBinderContainer;
import org.openntf.xpt.test.json.mock.UserMock;

import com.ibm.domino.services.util.JsonWriter;
import static org.junit.Assert.*;
public class Object2JsonTest {
	
	private final String RESULT = "{\"accountnumber\":42,\"accountobject\":42,\"name\":\"Marco M\\u00FCller\",\"salary\":123000,\"salaryobject\":123000,\"tags\":[\"Manager\",\"Developer\",\"Leader\"]}";
	private final String RESULT_CHILDREND = "{\"accountnumber\":42,\"accountobject\":42,\"children\":[{\"accountnumber\":21,\"accountobject\":21,\"name\":\"Ren\\u00E9 Meier\",\"salary\":90000,\"salaryobject\":90000,\"tags\":[\"Developer\"]},{\"accountnumber\":19,\"accountobject\":19,\"name\":\"Cecile N\\u00FCnlist\",\"salary\":99000,\"salaryobject\":99000}],\"name\":\"Marco M\\u00FCller\",\"salary\":123000,\"salaryobject\":123000,\"tags\":[\"Manager\",\"Developer\",\"Leader\"]}";
	@Test
	@Ignore
	public void testBuildJsonFromObject() {
		UserMock mock = UserMock.buildMockWithChildren();
		StringWriter sw = new StringWriter();
		JsonWriter jsWriter = new JsonWriter(sw, true);
		JSONService.getTestInstance().process2JSON(jsWriter, mock);
		assertEquals(RESULT, sw.toString());
	}

	@Test
	public void testBuildJsonFromObjectSimpleMock() {
		UserMock mock = UserMock.buildSimpleMock();
		StringWriter sw = new StringWriter();
		JsonWriter jsWriter = new JsonWriter(sw, true);
		JsonBinderContainer container = new JsonBinderContainer();
		container.process2JSON(jsWriter, mock);
		assertEquals(RESULT, sw.toString());
	}
	@Test
	public void testBuildJsonFromObjectChildren() {
		UserMock mock = UserMock.buildMockWithChildren();
		StringWriter sw = new StringWriter();
		JsonWriter jsWriter = new JsonWriter(sw, true);
		JsonBinderContainer container = new JsonBinderContainer();
		container.process2JSON(jsWriter, mock);
		System.out.println(sw.toString());
		assertEquals(RESULT_CHILDREND, sw.toString());
	}

}
