/*
 * © Copyright WebGate Consulting AG, 2013
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

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.util.EntityUtils;

import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;

public class PasswordService {

	private static PasswordService m_Service;

	private SecretKeySpec m_Key;

	private PasswordService() {

	}

	public static PasswordService getInstance() {
		if (m_Service == null) {
			m_Service = new PasswordService();
		}
		return m_Service;
	}

	public ExecutionUserProperties checkPassword(String strUser, String strPW, String strURL) {
		ExecutionUserProperties eupRC = new ExecutionUserProperties();
		eupRC.setLoggedIn(false);
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			httpClient = (DefaultHttpClient) ClientSSLResistanceExtender.wrapClient(httpClient);
			httpClient.setRedirectStrategy(new DefaultRedirectStrategy());
			/*
			 * String strNSFURL = strURL; String strRedirection = strNSFURL +
			 * "/xsp/xpage.agent?loginCheck"; java.util.List<NameValuePair>
			 * formparams = new ArrayList<NameValuePair>(); formparams.add(new
			 * BasicNameValuePair("username", strUser)); formparams.add(new
			 * BasicNameValuePair("password", strPW)); formparams.add(new
			 * BasicNameValuePair("redirectto", strRedirection));
			 * UrlEncodedFormEntity entity = new
			 * UrlEncodedFormEntity(formparams, "UTF-8");
			 * 
			 * HttpPost postRequest = new HttpPost(strNSFURL + "?login");
			 * postRequest.getParams().setParameter(ClientPNames.COOKIE_POLICY,
			 * org
			 * .apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
			 * 
			 * postRequest.setHeader("Content-Type",
			 * "application/x-www-form-urlencoded");
			 * postRequest.addHeader("accept", "application/json");
			 * postRequest.setEntity(entity); HttpResponse hsr =
			 * httpClient.execute(postRequest); for (Cookie ck :
			 * httpClient.getCookieStore().getCookies()) { if
			 * ("LtpaToken".equalsIgnoreCase(ck.getName())) { blRC = true; } if
			 * ("DomAuthSessId".equalsIgnoreCase(ck.getName())) { blRC = true; }
			 * }
			 */
			String strNSFURL = strURL;
			String strRedirection = strNSFURL + "/xsp/xpage.agent?action=checkLogin";
			HttpGet getRequestINIT = new HttpGet(strNSFURL);

			HttpGet getRequest = new HttpGet(strRedirection);
			getRequest.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(strUser, strPW), "UTF-8", false));
			getRequestINIT.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(strUser, strPW), "UTF-8", false));
			HttpResponse hsrINTI = httpClient.execute(getRequestINIT);
			if (hsrINTI.getStatusLine().getStatusCode() == 200) {
				// TODO: PARSE RESULT AS JSON!
				EntityUtils.consume(hsrINTI.getEntity());
				HttpResponse hsr = httpClient.execute(getRequest);
				JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(JsonJavaFactory.instanceEx, EntityUtils.toString(hsr.getEntity()));
				System.out.println(EntityUtils.toString(hsr.getEntity()));
			
				
				if (json.getString("status").equalsIgnoreCase("ok")) {
					eupRC.setLoggedIn(true);
					eupRC.setUserName(json.getString("username"));
					eupRC.setAccessLevel(json.getInt("level"));
				} else {
					eupRC.setLoggedIn(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eupRC;
	}

	public String encrypt(String strPW) {
		String strRC = "";
		try {
			Cipher aes = Cipher.getInstance("AES");
			aes.init(Cipher.ENCRYPT_MODE, getKey());
			byte[] ciphertext = aes.doFinal(strPW.getBytes());
			strRC = Base64.encodeBase64String(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRC;
	}

	public String decrypt(String strHash) {
		String strRC = "";
		try {
			Cipher aes = Cipher.getInstance("AES");
			byte[] ciphertext = Base64.decodeBase64(strHash);
			aes.init(Cipher.DECRYPT_MODE, getKey());
			strRC = new String(aes.doFinal(ciphertext));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRC;

	}

	private SecretKeySpec getKey() {
		if (m_Key == null) {
			try {
				String passphrase = "some is cool then other as some";
				MessageDigest digest = MessageDigest.getInstance("SHA");
				digest.update(passphrase.getBytes());
				m_Key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return m_Key;
	}
}
