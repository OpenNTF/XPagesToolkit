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
package org.openntf.xpt.agents;

import java.util.logging.Logger;

import org.eclipse.core.runtime.Plugin;
import org.openntf.xpt.agents.master.XPageAgentManager;
import org.openntf.xpt.core.utils.logging.LoggerFactory;
import org.osgi.framework.BundleContext;

public class XPTAgentActivator extends Plugin {

	private static XPTAgentActivator m_Activator;

	public XPTAgentActivator() {
		m_Activator = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		try {
			Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
			logCurrent.info("XPT AgentManager FrameWork loaded.");
			XPageAgentManager.getInstance().startJob();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		logCurrent.info("XPT AgentManager FrameWork stopped.");

	}

	public static XPTAgentActivator getInstance() {
		return m_Activator;
	}
}
