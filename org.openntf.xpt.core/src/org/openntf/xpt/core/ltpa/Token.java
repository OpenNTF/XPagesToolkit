/**
 * Copyright 2014, WebGate Consulting AG
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
package org.openntf.xpt.core.ltpa;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.openntf.xpt.core.utils.LMBCSConverter;

public class Token {

	private byte[] m_Creation = new byte[8];
	private byte[] m_Digest = new byte[20];
	private byte[] m_Expires = new byte[8];
	private byte[] m_Header = new byte[4];
	private byte[] m_RawToken;
	private byte[] m_User;

	private Date m_CreationDate;
	private Date m_ExpiresDate;
	private String m_LtpaToken;
	private String m_TokenName;
	private String m_Domain;

	/*** Constructor for the LtpaToken object */
	public Token() {
	}

	/***
	 * Constructor for the LtpaToken object**
	 * 
	 * @param token
	 *            Description of the Parameter
	 */
	public Token(String token, String tokenName, String tokenDomain) {
		m_LtpaToken = token;
		m_RawToken = Base64.decodeBase64(token);
		m_User = new byte[(m_RawToken.length) - 40];
		for (int i = 0; i < 4; i++) {
			m_Header = m_RawToken;
		}
		for (int i = 4; i < 12; i++) {
			m_Creation[i - 4] = m_RawToken[i];
		}
		for (int i = 12; i < 20; i++) {
			m_Expires[i - 12] = m_RawToken[i];
		}
		for (int i = 20; i < (m_RawToken.length - 20); i++) {
			m_User[i - 20] = m_RawToken[i];
		}
		for (int i = (m_RawToken.length - 20); i < m_RawToken.length; i++) {
			m_Digest[i - (m_RawToken.length - 20)] = m_RawToken[i];
		}
		setCreationDate(new Date(Long.parseLong(new String(m_Creation), 16) * 1000));
		setExpiresDate(new Date(Long.parseLong(new String(m_Expires), 16) * 1000));
		m_Domain = tokenDomain;
		m_TokenName = tokenName;
	}

	/**
	 * Generates a new token.
	 * 
	 * @param canonicalUser
	 *            e.g. CN=Christian Guedemann/o=EXAMPLE/C=CH
	 * @param strSecret
	 *            the tokens secrete
	 * @param tokenCreation
	 *            Date for the token creation
	 * @param expDuration
	 *            Expiraiton Time in Minutes
	 * @return New Token Object
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static Token generate(String canonicalUser, Date tokenCreation, TokenConfiguration config) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Token ltpa = new Token();
		Calendar calendar = Calendar.getInstance();
		MessageDigest md;

		md = MessageDigest.getInstance("SHA-1");
		ltpa.m_Header = new byte[] { 0, 1, 2, 3 };
		ltpa.m_User = LMBCSConverter.INSTANCE.convertString(canonicalUser);
		byte[] token = null;
		calendar.setTime(tokenCreation);
		ltpa.m_Creation = Long.toHexString(calendar.getTime().getTime() / 1000).toUpperCase().getBytes();
		calendar.setTime(tokenCreation);
		calendar.add(Calendar.MINUTE, config.getExpiration());
		ltpa.m_Expires = Long.toHexString(calendar.getTime().getTime() / 1000).toUpperCase().getBytes();
		token = concatenate(token, ltpa.m_Header);
		token = concatenate(token, ltpa.m_Creation);
		token = concatenate(token, ltpa.m_Expires);
		token = concatenate(token, ltpa.m_User);
		md.update(token);
		ltpa.m_Digest = md.digest(org.apache.commons.codec.binary.Base64.decodeBase64(config.getSecret()));
		token = concatenate(token, ltpa.m_Digest);
		ltpa.m_RawToken = token;
		ltpa.m_LtpaToken = org.apache.commons.codec.binary.Base64.encodeBase64String(token);
		ltpa.m_TokenName = config.getTokenName();
		ltpa.m_Domain = config.getDomain();
		return ltpa;
	}

	/***
	 * Helper method to concatenate a byte array.** @param a Byte array a.* @param
	 * b Byte array b.* @return a + b.
	 */
	private static byte[] concatenate(byte[] a, byte[] b) {
		if (a == null) {
			return b;
		} else {
			byte[] bytes = new byte[a.length + b.length];
			System.arraycopy(a, 0, bytes, 0, a.length);
			System.arraycopy(b, 0, bytes, a.length, b.length);
			return bytes;
		}
	}

	/***
	 * String representation of LtpaToken object.** @return Returns token String
	 * suitable for cookie value.
	 */
	public String toString() {
		return m_LtpaToken;
	}

	public void setExpiresDate(Date expiresDate) {
		this.m_ExpiresDate = expiresDate;
	}

	public Date getExpiresDate() {
		return m_ExpiresDate;
	}

	public void setCreationDate(Date creationDate) {
		this.m_CreationDate = creationDate;
	}

	public Date getCreationDate() {
		return m_CreationDate;
	}

	public byte[] getUser() {
		return m_User;
	}

	public String getTokenName() {
		return m_TokenName;
	}

	public void setTokenName(String tokenName) {
		m_TokenName = tokenName;
	}

	public String getDomain() {
		return m_Domain;
	}

	public String getLtpaToken() {
		return m_LtpaToken;
	}

	public void setDomain(String domain) {
		m_Domain = domain;
	}

}
