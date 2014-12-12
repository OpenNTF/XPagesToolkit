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
