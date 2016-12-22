/*
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
package org.openntf.xpt.agents.master;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;

public class ClientSSLResistanceExtender {

		public static HttpClient wrapClient(HttpClient base) {
			try {
				SSLContext ctx = SSLContext.getInstance("TLSv1");
				X509TrustManager tm = new X509TrustManager() {

					public void checkClientTrusted(X509Certificate[] xcs,
							String string) throws CertificateException {
					}

					public void checkServerTrusted(X509Certificate[] xcs,
							String string) throws CertificateException {
					}

					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				};
				X509HostnameVerifier verifier = new X509HostnameVerifier() {

					public void verify(String arg0, SSLSocket arg1)
							throws IOException {
					}

					public void verify(String arg0, X509Certificate arg1)
							throws SSLException {
					}

					public void verify(String arg0, String[] arg1, String[] arg2)
							throws SSLException {
					}

					public boolean verify(String hostname, SSLSession session) {
						return true;
					}

				};
				ctx.init(null, new TrustManager[] { tm }, null);
				SSLSocketFactory ssf = new SSLSocketFactory(ctx, verifier);
				ClientConnectionManager ccm = base.getConnectionManager();
				SchemeRegistry sr = ccm.getSchemeRegistry();
				sr.register(new Scheme("https", 443, ssf));
				return new DefaultHttpClient(ccm, base.getParams());
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
}
