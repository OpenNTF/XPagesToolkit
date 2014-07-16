/*
 * © Copyright WebGate Consulting AG, 2014
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
package org.openntf.xpt.core.utils;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.openntf.xpt.core.Activator;

import com.ibm.icu.charset.CharsetProviderICU;

/**
 * This class helps to convert Strings as LMBCS Strings to Bytes. It uses the
 * com.ibm.icu.charset package
 * 
 * @author Christian Guedemann
 * 
 */
public enum LMBCSConverter {
	INSTANCE;

	private CharsetProvider m_Provider;

	/**
	 * Converts the String input to a ByteArray using LMBCS Support
	 * 
	 * @param input
	 * @return
	 */
	public byte[] convertString(final String input) {

		return AccessController.doPrivileged(new PrivilegedAction<byte[]>() {

			@Override
			public byte[] run() {
				if (m_Provider == null) {
					m_Provider = new CharsetProviderICU();
				}
				Thread currentThread = Thread.currentThread();
				ClassLoader clCurrent = currentThread.getContextClassLoader();
				try {

					currentThread.setContextClassLoader(Activator.class.getClassLoader());
					Charset cs = m_Provider.charsetForName("LMBCS");
					if (cs != null) {
						return input.getBytes(cs);
					} else {
						System.out.println("XPT Warning: NO LMBCS Support!");
						return input.getBytes();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return input.getBytes();

				} finally {
					currentThread.setContextClassLoader(clCurrent);
				}
			}

		});

	}

}
