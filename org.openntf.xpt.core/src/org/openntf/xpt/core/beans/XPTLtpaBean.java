/*
 * � Copyright WebGate Consulting AG, 2014
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
package org.openntf.xpt.core.beans;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.ltpa.Token;
import org.openntf.xpt.core.ltpa.TokenFactory;

public class XPTLtpaBean {
	public static final String BEAN_NAME = "xptLTPABean"; //$NON-NLS-1$

	public static XPTLtpaBean get(FacesContext context) {
		return (XPTLtpaBean) context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
	}

	public static XPTLtpaBean get() {
		return get(FacesContext.getCurrentInstance());
	}
	
	
	public Token getLtpaTokenFor(String username, String domainName) {
		return TokenFactory.getInstance().buildLTPAToken(username, domainName);
	}
	
	public void assignNewToken(String username, String domainName, HttpServletResponse response) {
		Token token = getLtpaTokenFor(username, domainName);
		Cookie cookie = new Cookie(token.getTokenName(), token.toString());
		cookie.setPath("/");
		cookie.setDomain(token.getDomain());
		response.addCookie(cookie);
	}
}
