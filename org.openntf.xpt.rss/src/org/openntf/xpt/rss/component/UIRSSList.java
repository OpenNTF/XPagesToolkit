package org.openntf.xpt.rss.component;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xpt.core.utils.ErrorJSONBuilder;
import org.openntf.xpt.core.utils.JSONSupport;
import org.openntf.xpt.core.utils.ValueBindingSupport;
import org.openntf.xpt.rss.model.FeedReaderService;
import org.openntf.xpt.rss.model.RSSEntry;
import org.openntf.xpt.rss.model.RSSFeed;

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
	private Boolean m_UseDescription;

	public Boolean getUseDescription() {
		return ValueBindingSupport.getValue(m_UseDescription, "useDescription", this, Boolean.FALSE, getFacesContext());
	}

	public void setUseDescription(Boolean useDescription) {
		m_UseDescription = useDescription;
	}

	public UIRSSList() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getHtmlTemplate() {
		return ValueBindingSupport.getValue(m_HtmlTemplate, "htmlTemplate", this, null, getFacesContext());
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
		return ValueBindingSupport.getValue(m_FeedURL, "feedURL",this, null, getFacesContext());
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
			RSSFeed rssFeed = AccessController.doPrivileged(new PrivilegedAction<RSSFeed>() {
				@Override
				public RSSFeed run() {
					return FeedReaderService.getInstance().getFeedFromURL(finURL);
				}

			});

			JsonWriter jsWriter = new JsonWriter(httpResponse.getWriter(), true);
			jsWriter.startObject();

			JSONSupport.writeString(jsWriter, "status", "ok", false);
			JSONSupport.writeString(jsWriter, "url", rssFeed.getURL(), false);
			JSONSupport.writeString(jsWriter, "title", rssFeed.getTitle(), false);
			JSONSupport.writeString(jsWriter, "author", rssFeed.getAuthor(), false);
			JSONSupport.writeString(jsWriter, "description", rssFeed.getDescription(), false);
			JSONSupport.writeString(jsWriter, "imageurl", rssFeed.getImageURL(), false);

			jsWriter.startProperty("entries");
			jsWriter.startArray();
			for (RSSEntry rssE : rssFeed.getEntries()) {
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
		m_UseDescription = (Boolean) values[5];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[6];
		values[0] = super.saveState(context);
		values[1] = m_FeedURL;
		values[2] = m_HtmlTemplate;
		values[3] = m_Style;
		values[4] = m_StyleClass;
		values[5] = m_UseDescription;
		return values;
	}

}
