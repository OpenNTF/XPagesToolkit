package org.openntf.xpt.test.rss;

import javax.faces.context.FacesContext;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;

public class UIRSSTest {

	@Test
	public void getURLTest() {
		FacesContext context = EasyMock.createNiceMock(FacesContext.class);
		assertNotNull(context);
	}

}
