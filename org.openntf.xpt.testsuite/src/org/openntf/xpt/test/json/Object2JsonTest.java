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
	public void testBuildJsonFromObjectWithBinderContainer() {
		UserMock mock = UserMock.buildMockWithChildren();
		StringWriter sw = new StringWriter();
		JsonWriter jsWriter = new JsonWriter(sw, true);
		JsonBinderContainer container = new JsonBinderContainer();
		container.process2JSON(jsWriter, mock);
		assertEquals(RESULT, sw.toString());
	}

}
