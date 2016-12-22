package org.openntf.xpt.core.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.utils.ErrorJSONBuilder;
import org.openntf.xpt.core.utils.HttpResponseSupport;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.services.ServiceException;
import com.ibm.domino.services.rest.RestServiceEngine;
import com.ibm.xsp.extlib.component.rest.CustomService;
import com.ibm.xsp.extlib.component.rest.CustomServiceBean;

public abstract class AbstractRestService extends CustomServiceBean {

	public final Map<String, IRestMethod> methods = new HashMap<String, IRestMethod>();
	public final Map<String, IRestAction> actions = new HashMap<String, IRestAction>();

	public AbstractRestService() {
		initActionsAndMethods();
	}

	protected abstract void initActionsAndMethods();

	@Override
	public void renderService(CustomService customService, RestServiceEngine engine) throws ServiceException {
		HttpServletRequest request = engine.getHttpRequest();
		HttpServletResponse response = engine.getHttpResponse();
		String action = request.getParameter("action");
		String method = request.getMethod();
		HttpResponseSupport.setJSONUTF8ContentType(response);
		if (!StringUtil.isEmpty(action)) {
			handleAction(request, response, action, method);
		} else {
			handleMethod(request, response, method);
		}
	}

	private void handleMethod(HttpServletRequest request, HttpServletResponse response, String method) {
		if (methods.containsKey(method.toLowerCase())) {
			IRestMethod restMethode = methods.get(method.toLowerCase());
			try {
				restMethode.execute(request, response);
			} catch (XPTRestException e) {
				ErrorJSONBuilder.getInstance().processError2JSON(response, 9999, e.getMessage(), e);
			}
		} else {
			ErrorJSONBuilder.getInstance().processError2JSON(response, 1, "Method " + method + " not supported by this RestService!", null);
		}
	}

	private void handleAction(HttpServletRequest request, HttpServletResponse response, String action, String method) {
		if (actions.containsKey(action.toLowerCase())) {
			IRestAction restAction = actions.get(action.toLowerCase());
			if (contains(restAction.getSupportedMethods(), method)) {
				try {
					restAction.execute(request, response);
				} catch (XPTRestException e) {
					ErrorJSONBuilder.getInstance().processError2JSON(response, 9999, e.getMessage(), e);
				}
			} else {
				ErrorJSONBuilder.getInstance().processError2JSON(response, 1, "Method " + method + " not supported by this action: " + restAction.getActionName(), null);
			}
		} else {
			ErrorJSONBuilder.getInstance().processError2JSON(response, 1, "Action " + action + " not supported by this RestService.", null);
		}
	}

	private boolean contains(String[] supportedMethods, String method) { // NOSONAR
		for (String test : supportedMethods) {
			if (test.equalsIgnoreCase(method)) {
				return true;
			}
		}
		return false;
	}

	public boolean useHTTPStatusForError() {
		return false;
	}

	public void addAction(IRestAction action) {
		actions.put(action.getActionName().toLowerCase(), action);
	}

	public void addMethod(IRestMethod method) {
		methods.put(method.getMethodName().toLowerCase(), method);
	}

}
