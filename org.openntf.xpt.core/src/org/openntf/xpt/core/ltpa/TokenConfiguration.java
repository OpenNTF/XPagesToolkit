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

public class TokenConfiguration {

	public static TokenConfiguration buildTokenConfiguration(String domain, String secret, int expiration, String tokenName) {
		TokenConfiguration tc = new TokenConfiguration(domain, secret, expiration, tokenName);
		return tc;
	}
	
	private final String m_Domain;
	private final String m_Secret;
	private final int m_Expiration;
	private final String m_TokenName;
	private TokenConfiguration(String domain, String secret, int expiration, String tokenName) {
		super();
		m_Domain = domain;
		m_Secret = secret;
		m_Expiration = expiration;
		m_TokenName = tokenName;
	}
	public String getDomain() {
		return m_Domain;
	}
	public String getSecret() {
		return m_Secret;
	}
	public int getExpiration() {
		return m_Expiration;
	}
	public String getTokenName() {
		return m_TokenName;
	}

	
}
