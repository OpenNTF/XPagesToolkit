package org.openntf.xpt.oneui.component;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.openntf.xpt.core.utils.ValueBindingSupport;

import com.ibm.xsp.util.StateHolderUtil;

public class UIAboutPage extends UIComponentBase {

	public static final String FACET_CONTENT = "content";//$NON-NLS-1$

	public static final String COMPONENT_TYPE = "org.openntf.xpt.oneui.component.uiaboutpage"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "javax.faces.Panel"; //$NON-NLS-1$
	public static final String RENDERER_TYPE = "org.openntf.xpt.oneui.component.uiaboutpage"; //$NON-NLS-1$

	private List<UIAboutBlock> m_LeftColumnBlocks;
	private List<UIAboutBlock> m_RightColumnBlocks;

	private String m_Title;
	private String m_MarketingClaim;
	private String m_Text;
	private String m_Style;
	private String m_StyleClass;

	public UIAboutPage() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public void addLeftColumnBlock(UIAboutBlock abCurrent) {
		if (m_LeftColumnBlocks == null) {
			m_LeftColumnBlocks = new ArrayList<UIAboutBlock>();
		}
		m_LeftColumnBlocks.add(abCurrent);
	}

	public List<UIAboutBlock> getLeftColumnBlocks() {
		return m_LeftColumnBlocks;
	}

	public void setLeftColumnBlocks(List<UIAboutBlock> leftColumnBlocks) {
		m_LeftColumnBlocks = leftColumnBlocks;
	}

	public void addRightColumnBlock(UIAboutBlock abCurrent) {
		if (m_RightColumnBlocks == null) {
			m_RightColumnBlocks = new ArrayList<UIAboutBlock>();
		}
		m_RightColumnBlocks.add(abCurrent);
	}

	public List<UIAboutBlock> getRightColumnBlocks() {
		return m_RightColumnBlocks;
	}

	public void setRightColumnBlocks(List<UIAboutBlock> rightColumnBlocks) {
		m_RightColumnBlocks = rightColumnBlocks;
	}

	public String getTitle() {
		return ValueBindingSupport.getValue(m_Title, "title", this, null, getFacesContext());
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public String getMarketingClaim() {
		return ValueBindingSupport.getValue(m_MarketingClaim, "marketingClaim", this, null, getFacesContext());
	}

	public void setMarketingClaim(String marketingClaim) {
		m_MarketingClaim = marketingClaim;
	}

	public String getText() {
		return ValueBindingSupport.getValue(m_Text, "text", this, null, getFacesContext());
	}

	public void setText(String text) {
		m_Text = text;
	}

	public String getStyle() {
		return ValueBindingSupport.getValue(m_Style, "style", this, null, getFacesContext());
	}

	public void setStyle(String style) {
		m_Style = style;
	}

	public String getStyleClass() {
		return ValueBindingSupport.getValue(m_StyleClass, "styleClass", this, null, getFacesContext());
	}

	public void setStyleClass(String styleClass) {
		m_StyleClass = styleClass;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		m_Title = (String) values[1];
		m_MarketingClaim = (String) values[2];
		m_Text = (String) values[3];
		m_Style = (String) values[4];
		m_StyleClass = (String) values[5];
		m_LeftColumnBlocks = StateHolderUtil.restoreList(context, getParent(), values[6]);
		m_RightColumnBlocks = StateHolderUtil.restoreList(context, getParent(), values[7]);
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[8];
		values[0] = super.saveState(context);
		values[1] = m_Title;
		values[2] = m_MarketingClaim;
		values[3] = m_Text;
		values[4] = m_Style;
		values[5] = m_StyleClass;
		values[6] = StateHolderUtil.saveList(context, m_LeftColumnBlocks);
		values[7] = StateHolderUtil.saveList(context, m_RightColumnBlocks);

		return values;
	}
}
