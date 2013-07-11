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
package org.openntf.xpt.agents.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.IServletFactory;
import com.ibm.designer.runtime.domino.adapter.ServletMatch;

public class ExecutorServletFactory implements IServletFactory {

	private ComponentModule m_Module;
	public static final String SERVLET_PATH = "/xsp/xpage.agent";
	public ExecutorServlet m_Servlet;

	@Override
	public ServletMatch getServletMatch(String contextPath, String path) throws ServletException {

		if (path.startsWith(SERVLET_PATH)) { // $NON-NLS-1$
			int len = SERVLET_PATH.length(); // $NON-NLS-1$
			String servletPath = path.substring(0, len);
			String pathInfo = path.substring(len);
			return new ServletMatch(getExecutorServlet(), servletPath, pathInfo);
		}
		return null;
	}

	@Override
	public void init(ComponentModule arg0) {
		m_Module = arg0;
	}

	public Servlet getExecutorServlet() throws ServletException {
		if (m_Servlet == null) {
			synchronized (this) {
				if (m_Servlet == null) {
					m_Servlet = (ExecutorServlet) m_Module.createServlet(new ExecutorServlet(), "XPage Agent Executor", null);
				}
			}
		}
		return m_Servlet;
	}
}
