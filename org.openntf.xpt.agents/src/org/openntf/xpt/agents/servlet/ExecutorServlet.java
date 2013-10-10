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

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.beans.XPTAgentBean;

import com.ibm.commons.util.NotImplementedException;

public class ExecutorServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// The FacesContext factory requires a lifecycle parameter which is not
	// used,
	// but when not present, it generates
	// a NUllPointer exception. Silly thing! So we create an empty one that does
	// nothing...
	private static Lifecycle dummyLifeCycle = new Lifecycle() {

		@Override
		public void render(FacesContext context) throws FacesException {
			throw new NotImplementedException();
		}

		@Override
		public void removePhaseListener(PhaseListener listener) {
			throw new NotImplementedException();
		}

		@Override
		public PhaseListener[] getPhaseListeners() {
			throw new NotImplementedException();
		}

		@Override
		public void execute(FacesContext context) throws FacesException {
			throw new NotImplementedException();
		}

		@Override
		public void addPhaseListener(PhaseListener listener) {
			throw new NotImplementedException();
		}
	};

	private ServletConfig m_Config;
	private FacesContextFactory m_ContextFactory;

	@Override
	public void init(ServletConfig config) throws ServletException {
		m_Config = config;
		m_ContextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			FacesContext fcCurrent = initContext(req, resp);
			System.out.println(fcCurrent);
			XPTAgentBean agBean = XPTAgentBean.get(fcCurrent);
			System.out.println(agBean);
			System.out.println(fcCurrent.getApplication());

			String strAction = req.getParameter("action");
			if (strAction != null) {
				IXPTServletCommand command = CommandFactory.getInstance().getCommand(strAction);
				if (command != null) {
					command.processActionH(resp, agBean, fcCurrent);
					releaseContext(fcCurrent);
					return;
				}
			}
			resp.setContentType("text/plain");
			PrintWriter pwCurrent = resp.getWriter();
			pwCurrent.println("Command: " + strAction + " is not available");
			releaseContext(fcCurrent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public FacesContext initContext(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// Create a temporary FacesContext and make it available

		// FacesContext context =
		// m_ContextFactory.getFacesContext(request.getSession().getServletContext(),
		// request, response, dummyLifeCycle);
		FacesContext context = m_ContextFactory.getFacesContext(m_Config.getServletContext(), request, response, dummyLifeCycle);
		return context;
	}

	public void releaseContext(FacesContext context) throws ServletException, IOException {
		context.release();
	}

}
