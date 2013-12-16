package org.openntf.xpt.agents.component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.agents.beans.XPTAgentBean;
import org.openntf.xpt.core.utils.logging.LoggerFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.jscript.json.JsonJavaScriptFactory;
import com.ibm.jscript.types.FBSValue;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.application.UniqueViewIdManager;
import com.ibm.xsp.component.FacesAjaxComponent;
import com.ibm.xsp.util.JavaScriptUtil;
import com.ibm.xsp.util.StateHolderUtil;
import com.ibm.xsp.webapp.XspHttpServletResponse;

public class UIAgentProgressbar extends UIComponentBase implements FacesAjaxComponent {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.agents.component.uiagentprogressbar"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.agents.component.uiagentprogressbar"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.agents.component.uiagentprogressbar"; //$NON-NLS-1$

	private String m_AgentName;
	private String m_ServiceName;
	private String m_Style;
	private String m_StyleClass;
	private String m_StyleTitle;
	private String m_StyleClassTitle;
	private String m_StyleTask;
	private String m_StyleClassTask;
	private String m_StyleProgressBar;
	private String m_StyleClassProgressBar;
	private List<UIAgentProperty> m_AgentProperties;

	public UIAgentProgressbar() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getAgentName() {
		if (null != m_AgentName) {
			return m_AgentName;
		}
		ValueBinding _vb = getValueBinding("agentName"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}
	}

	public void setAgentName(String agentName) {
		m_AgentName = agentName;
	}

	public String getServiceName() {
		if (null != m_ServiceName) {
			return m_ServiceName;
		}
		ValueBinding _vb = getValueBinding("serviceName"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}
	}

	public void setServiceName(String serviceName) {
		m_ServiceName = serviceName;
	}

	public String getStyle() {
		return m_Style;
	}

	public void setStyle(String style) {
		m_Style = style;
	}

	public String getStyleClass() {
		return m_StyleClass;
	}

	public void setStyleClass(String styleClass) {
		m_StyleClass = styleClass;
	}

	public String getStyleTitle() {
		return m_StyleTitle;
	}

	public void setStyleTitle(String styleTitle) {
		m_StyleTitle = styleTitle;
	}

	public String getStyleClassTitle() {
		return m_StyleClassTitle;
	}

	public void setStyleClassTitle(String styleClassTitle) {
		m_StyleClassTitle = styleClassTitle;
	}

	public String getStyleTask() {
		return m_StyleTask;
	}

	public void setStyleTask(String styleTask) {
		m_StyleTask = styleTask;
	}

	public String getStyleClassTask() {
		return m_StyleClassTask;
	}

	public void setStyleClassTask(String styleClassTask) {
		m_StyleClassTask = styleClassTask;
	}

	public String getStyleProgressBar() {
		return m_StyleProgressBar;
	}

	public void setStyleProgressBar(String styleProgressBar) {
		m_StyleProgressBar = styleProgressBar;
	}

	public String getStyleClassProgressBar() {
		return m_StyleClassProgressBar;
	}

	public void setStyleClassProgressBar(String styleClassProgressBar) {
		m_StyleClassProgressBar = styleClassProgressBar;
	}

	public List<UIAgentProperty> getAgentProperties() {
		return m_AgentProperties;
	}

	public void setAgentProperties(List<UIAgentProperty> agentProperties) {
		m_AgentProperties = agentProperties;
	}

	public void addAgentProperty(UIAgentProperty prop) {
		if (m_AgentProperties == null) {
			m_AgentProperties = new ArrayList<UIAgentProperty>();
		}
		m_AgentProperties.add(prop);
	}

	public String getUrl(FacesContext context) {

		ExternalContext externalContext = context.getExternalContext();
		String contextPath = externalContext.getRequestContextPath();
		String servletPath = externalContext.getRequestServletPath();

		StringBuilder bURL = new StringBuilder();
		bURL.append(contextPath);
		bURL.append(servletPath);

		boolean hasQ = false;

		// Compose the query string
		String vid = UniqueViewIdManager.getUniqueViewId(context.getViewRoot());
		if (StringUtil.isNotEmpty(vid)) {
			bURL.append((hasQ ? "&" : "?") + AjaxUtil.AJAX_VIEWID + "=" + vid);
			hasQ = true;
		}
		// If not path info was specified,use the component ajax id
		String axTarget = getClientId(context);
		if (StringUtil.isNotEmpty(axTarget)) {
			bURL.append((hasQ ? "&" : "?") + AjaxUtil.AJAX_AXTARGET + "=" + axTarget);
			hasQ = true;
		}

		return bURL.toString();
	}

	// AJAX HANDLING
	@Override
	public boolean handles(FacesContext context) {
		System.out.println(context.getExternalContext().getRequestContextPath() + " -> " + context.getExternalContext().getRequestServletPath());
		return false;
	}

	@Override
	public void processAjaxRequest(FacesContext context) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) context.getExternalContext().getResponse();
		HttpServletRequest httpRequest = (HttpServletRequest) context.getExternalContext().getRequest();

		// Disable the XPages response buffer as this will collide with the
		// engine one
		// We mark it as committed and use its delegate instead
		Logger logCurrent = LoggerFactory.getLogger(this.getClass().getCanonicalName());
		if (httpResponse instanceof XspHttpServletResponse) {
			XspHttpServletResponse r = (XspHttpServletResponse) httpResponse;
			r.setCommitted(true);
			httpResponse = r.getDelegate();
		}
		JsonJavaObject json = null;
		JsonJavaFactory factory = JsonJavaFactory.instanceEx;
		try {
			httpResponse.setContentType("text/json");
			httpResponse.setCharacterEncoding("utf-8");
			json = (JsonJavaObject) JsonParser.fromJson(factory, httpRequest.getReader());
			String strMethod = json.getString("method");
			if ("startAgent".equals(strMethod)) {
				JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
				String strAgentName = json.getString("agentname");
				HashMap<String, String> hmProps = new HashMap<String, String>();

				// Parsing the SSJS AgentArguments
				if (m_AgentProperties != null) {
					for (UIAgentProperty uip : m_AgentProperties) {
						hmProps.put(uip.getKey(), uip.getValue());
					}
				}

				// Parsing the Arguments form JavaScript submit
				Object jsObjArguments = json.getJsonProperty("arguments");
				if (jsObjArguments instanceof JsonJavaObject) {
					logCurrent.info("Arguments from the CSJS");
					JsonJavaObject jsObj = (JsonJavaObject) jsObjArguments;
					for (Iterator<String> itProp = jsObj.getProperties(); itProp.hasNext();) {
						
						String strProp = itProp.next();
						logCurrent.info(strProp +" -> "+ jsObj.getString(strProp));
						hmProps.put(strProp, jsObj.getString(strProp));
					}
				}

				String jobID = null;
				if (hmProps.size() > 0) {
					jobID = XPTAgentBean.get(context).executeAgentUI(strAgentName, hmProps);
				} else {
					jobID = XPTAgentBean.get(context).executeAgentUI(strAgentName);
				}
				jsWriter.startObject();
				jsWriter.startProperty("status");
				jsWriter.outStringLiteral("ok");
				jsWriter.endProperty();
				jsWriter.startProperty("jobid");
				jsWriter.outStringLiteral(jobID);
				jsWriter.endProperty();
				jsWriter.endObject();
				jsWriter.close();
				return;
			}
			if ("getStatus".equals(strMethod)) {
				JsonFactory factory2 = new JsonJavaScriptFactory(JavaScriptUtil.getJSContext());
				String strJobID = json.getString("jobid");
				FBSValue fbv = XPTAgentBean.get(context).getJobStatus(strJobID);
				JsonGenerator.toJson(factory2, httpResponse.getWriter(), fbv, true);
				httpResponse.getWriter().flush();
				return;
			}
			JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
			jsWriter.startObject();
			jsWriter.startProperty("status");
			jsWriter.outStringLiteral("error");
			jsWriter.endProperty();
			jsWriter.startProperty("error");
			jsWriter.outStringLiteral("method not found ->" + strMethod);
			jsWriter.endProperty();
			jsWriter.endObject();
			jsWriter.close();

		} catch (Exception e) {
			try {
				JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
				jsWriter.startObject();
				jsWriter.startProperty("status");
				jsWriter.outStringLiteral("error");
				jsWriter.endProperty();
				jsWriter.startProperty("error");
				StringWriter srw = new StringWriter();
				PrintWriter pw = new PrintWriter(srw);
				e.printStackTrace(pw);
				jsWriter.outStringLiteral(srw.toString());
				jsWriter.endProperty();
				jsWriter.endObject();
				jsWriter.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				httpResponse.getWriter().flush();
			}
		}
	}

	@Override
	public void restoreState(FacesContext context, Object valCurrent) {
		Object[] values = (Object[]) valCurrent;
		super.restoreState(context, values[0]);
		m_AgentName = (String) values[1];
		m_ServiceName = (String) values[2];
		m_Style = (String) values[3];
		m_StyleClass = (String) values[4];
		m_StyleTitle = (String) values[5];
		m_StyleClassTitle = (String) values[6];
		m_StyleTask = (String) values[7];
		m_StyleClassTask = (String) values[8];
		m_StyleProgressBar = (String) values[9];
		m_StyleClassProgressBar = (String) values[10];
		m_AgentProperties = StateHolderUtil.restoreList(context, this, values[11]);
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[12];
		values[0] = super.saveState(context);
		values[1] = m_AgentName;
		values[2] = m_ServiceName;
		values[3] = m_Style;
		values[4] = m_StyleClass;
		values[5] = m_StyleTitle;
		values[6] = m_StyleClassTitle;
		values[7] = m_StyleTask;
		values[8] = m_StyleClassTask;
		values[9] = m_StyleProgressBar;
		values[10] = m_StyleClassProgressBar;
		values[11] = StateHolderUtil.saveList(context, m_AgentProperties);
		return values;
	}

}
