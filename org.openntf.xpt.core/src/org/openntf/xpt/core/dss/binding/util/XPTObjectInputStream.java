package org.openntf.xpt.core.dss.binding.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class XPTObjectInputStream extends ObjectInputStream {

	public XPTObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		Class<?> cl = null;
		try {
			cl = super.resolveClass(desc);
		} catch (Exception e) {
		}
		if (cl == null) {
			cl =Thread.currentThread().getContextClassLoader().loadClass(desc.getName());
			if (cl == null) {
				throw new ClassNotFoundException(desc.getName() +" not found (contextClassLoader)!");
			}

		}
		return cl;
	}
	
	@Override
	protected Object resolveObject(Object obj) throws IOException {
		System.out.println("Calling resolveObject");
		return super.resolveObject(obj);
	}
	@Override
	protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
		return super.resolveProxyClass(interfaces);
	}
}
