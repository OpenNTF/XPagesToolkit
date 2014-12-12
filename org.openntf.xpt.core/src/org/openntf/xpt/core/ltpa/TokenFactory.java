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
package org.openntf.xpt.core.ltpa;

import java.util.Date;
import java.util.HashMap;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;

import org.openntf.xpt.core.XPTRuntimeException;

import com.ibm.designer.runtime.Application;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class TokenFactory {

	private static final String ORG_OPENNTF_XPT_CORE_LTPA_FACTORY = "org.openntf.xpt.core.ltpa.TokenFactory";
	private HashMap<String, TokenConfiguration> m_Config = null;

	public static synchronized TokenFactory getInstance() {
		TokenFactory ds = (TokenFactory) Application.get().getObject(ORG_OPENNTF_XPT_CORE_LTPA_FACTORY);
		if (ds == null) {
			ds = new TokenFactory();
			Application.get().putObject(ORG_OPENNTF_XPT_CORE_LTPA_FACTORY, ds);
		}
		return ds;
	}

	public Token buildLTPAToken(String user, String domainName) {
		TokenConfiguration tokenConfiguration = getConfiguration(domainName);
		if (tokenConfiguration == null) {
			throw new XPTRuntimeException("No TokenConfiguration found for Server: " + domainName);
		}
		try {
			Token token = Token.generate(user, new Date(), tokenConfiguration);
			return token;
		} catch (Exception e) {
			throw new XPTRuntimeException("Error during Tokengeneration", e);
		}
	}

	public TokenConfiguration getConfiguration(String serverFQDN) {
		if (m_Config == null) {
			m_Config = loadConfig();
		}
		if (m_Config.containsKey("." + serverFQDN.toLowerCase())) {
			return m_Config.get("." + serverFQDN.toLowerCase());
		}
		int nStart = serverFQDN.indexOf(".");
		while (nStart > 0 && serverFQDN.length() > 0) {
			serverFQDN = serverFQDN.substring(nStart);
			if (m_Config.containsKey(serverFQDN.toLowerCase())) {
				return m_Config.get(serverFQDN.toLowerCase());
			}
		}
		return null;
	}

	private synchronized HashMap<String, TokenConfiguration> loadConfig() {
		Database ndbNames = null;
		View viwSSO = null;
		HashMap<String, TokenConfiguration> configurations = new HashMap<String, TokenConfiguration>();
		try {
			ndbNames = ExtLibUtil.getCurrentSessionAsSigner().getDatabase(ExtLibUtil.getCurrentSession().getServerName(), "names.nsf");
			if (ndbNames != null) {
				viwSSO = ndbNames.getView("($WebSSOConfigs)");
				Document docNext = viwSSO.getFirstDocument();
				while (docNext != null) {
					Document docConfig = docNext;
					docNext = viwSSO.getNextDocument(docNext);
					TokenConfiguration tokenConfig = buildConfig(docConfig);
					if (tokenConfig != null) {
						configurations.put(tokenConfig.getDomain().toLowerCase(), tokenConfig);
					}
					docConfig.recycle();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configurations;
	}

	private TokenConfiguration buildConfig(Document docCurrent) {
		try {
			String strSecret = docCurrent.getItemValueString("LTPA_DominoSecret");
			String strDomain = docCurrent.getItemValueString("LTPA_TokenDomain");
			int expiration = docCurrent.getItemValueInteger("LTPA_TokenExpiration");
			String strTokenName = docCurrent.getItemValueString("LTPA_TokenName");
			if (strSecret != null && strDomain != null && strTokenName != null) {
				return TokenConfiguration.buildTokenConfiguration(strDomain, strSecret, expiration, strTokenName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
