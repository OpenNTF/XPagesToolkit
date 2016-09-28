package org.openntf.xpt.demo;

import java.util.UUID;

import org.openntf.xpt.core.dss.AbstractStorageService;
import org.openntf.xpt.demo.bo.BlogEntry;

public class BlogStorageService extends AbstractStorageService<BlogEntry> {

	private final static BlogStorageService service = new BlogStorageService();

	public final static BlogStorageService getInstance() {
		return service;
	}

	private BlogStorageService() {
	}

	@Override
	protected BlogEntry createObject() {
		BlogEntry be = new BlogEntry();
		be.setID(UUID.randomUUID().toString());
		return be;
	}

}
