package org.openntf.xpt.rss.component;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.utils.ErrorJSONBuilder;
import org.openntf.xpt.core.utils.JSONSupport;
import org.openntf.xpt.rss.model.FeedReaderService;
import org.openntf.xpt.rss.model.RSSEntry;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.application.UniqueViewIdManager;
import com.ibm.xsp.component.FacesAjaxComponent;
import com.ibm.xsp.webapp.XspHttpServletResponse;

public class UIRSSList extends UIComponentBase implements FacesAjaxComponent {

	public static final String COMPONENT_TYPE = "org.openntf.xpt.rss.component.uisrsslist"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "org.openntf.xpt.rss.component.uisrsslist"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.rss.component.uisrsslist"; //$NON-NLS-1$

	private String m_FeedURL;
	private String m_HtmlTemplate;
	private String m_Style;
	private String m_StyleClass;

	public UIRSSList() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getHtmlTemplate() {
		if (m_HtmlTemplate != null) {
			return m_HtmlTemplate;
		}
		ValueBinding _vb = getValueBinding("htmlTemplate"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}

	}

	public void setHtmlTemplate(String htmlTemplate) {
		m_HtmlTemplate = htmlTemplate;
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

	public String getFeedURL() {
		if (m_FeedURL != null) {
			return m_FeedURL;
		}
		ValueBinding _vb = getValueBinding("feedURL"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(FacesContext.getCurrentInstance());
		} else {
			return null;
		}
	}

	public void setFeedURL(String feedURL) {
		m_FeedURL = feedURL;
	}

	@Override
	public boolean handles(FacesContext arg0) {
		return false;
	}

	@Override
	public void processAjaxRequest(FacesContext context) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) context.getExternalContext().getResponse();
		HttpServletRequest httpRequest = (HttpServletRequest) context.getExternalContext().getRequest();

		// Disable the XPages response buffer as this will collide with the
		// engine one
		// We mark it as committed and use its delegate instead
		// Logger logCurrent =
		// LoggerFactory.getLogger(this.getClass().getCanonicalName());
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

			final String finURL = json.getString("feedURL");
			if (StringUtil.isEmpty(finURL)) {
				ErrorJSONBuilder.getInstance().processError2JSON(httpResponse, 1000, "No URL found!", null);
			}
			List<RSSEntry> lstResult = AccessController.doPrivileged(new PrivilegedAction<List<RSSEntry>>() {
				@Override
				public List<RSSEntry> run() {
					return FeedReaderService.getInstance().getAllEntriesFromURL(finURL);
				}

			});

			JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
			jsWriter.startObject();
			jsWriter.startProperty("status");
			jsWriter.outStringLiteral("ok");
			jsWriter.endProperty();

			jsWriter.startProperty("entries");
			jsWriter.startArray();
			for (RSSEntry rssE : lstResult) {
				jsWriter.startArrayItem();
				jsWriter.startObject();

				JSONSupport.writeString(jsWriter, "title", rssE.getTitle(), false);
				JSONSupport.writeString(jsWriter, "link", rssE.getLink(), false);
				JSONSupport.writeString(jsWriter, "content", rssE.getContentsTXT(), false);
				JSONSupport.writeString(jsWriter, "author", rssE.getAuthorsTXT(), false);
				JSONSupport.writeString(jsWriter, "categories", rssE.getCategoriesTXT(), false);
				JSONSupport.writeString(jsWriter, "description", rssE.getDescription(), false);
				JSONSupport.writeDate(jsWriter, "created", rssE.getCreated(), false);
				JSONSupport.writeDate(jsWriter, "updated", rssE.getUpdated(), false);

				jsWriter.endObject();
				jsWriter.endArrayItem();
			}
			jsWriter.endArray();
			jsWriter.endProperty();
			jsWriter.endObject();
			jsWriter.close();
		} catch (Exception e) {
			ErrorJSONBuilder.getInstance().processError2JSON(httpResponse, 9999, "Error during parsing!", e);
		}

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

	@Override
	public void restoreState(FacesContext context, Object valCurrent) {
		Object[] values = (Object[]) valCurrent;
		super.restoreState(context, values[0]);
		m_FeedURL = (String) values[1];
		m_HtmlTemplate = (String) values[2];
		m_Style = (String) values[3];
		m_StyleClass = (String) values[4];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = m_FeedURL;
		values[2] = m_HtmlTemplate;
		values[3] = m_Style;
		values[4] = m_StyleClass;
		return values;
	}

}
