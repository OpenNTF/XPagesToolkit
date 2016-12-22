/**
 * Copyright 2013, WebGate Consulting AG
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
package org.openntf.xpt.oneui.component;

import javax.faces.context.FacesContext;

import org.openntf.xpt.core.utils.ValueBindingSupport;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class UIAboutBlock extends ValueBindingObjectImpl {

	private String m_Title;
	private String m_Text;
	private String m_Link;
	private String m_LinkTitle;
	private String m_Style;
	private String m_StyleClass;
	private String m_StyleTitle;
	private String m_StyleClassTitle;
	private String m_StyleText;
	private String m_StyleClassText;
	private String m_StyleLink;
	private String m_StyleClassLink;

	public String getTitle() {

		return ValueBindingSupport.getValue(m_Title, "title", this, null, getFacesContext());
	}

	public void setTitle(String title) {
		m_Title = title;
	}

	public String getText() {
		return ValueBindingSupport.getValue(m_Text, "text", this, null, getFacesContext());
	}

	public void setText(String text) {
		m_Text = text;
	}

	public String getLink() {
		return ValueBindingSupport.getValue(m_Link, "link", this, null, getFacesContext());
	}

	public void setLink(String link) {
		m_Link = link;
	}

	public String getLinkTitle() {
		return ValueBindingSupport.getValue(m_LinkTitle, "linkTitle", this, null, getFacesContext());
	}

	public void setLinkTitle(String linkTitle) {
		m_LinkTitle = linkTitle;
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

	public String getStyleTitle() {
		return ValueBindingSupport.getValue(m_StyleTitle, "styleTitle", this, null, getFacesContext());
	}

	public void setStyleTitle(String styleTitle) {
		m_StyleTitle = styleTitle;
	}

	public String getStyleClassTitle() {
		return ValueBindingSupport.getValue(m_StyleClassTitle, "styleClassTitle", this, null, getFacesContext());
	}

	public void setStyleClassTitle(String styleClassTitle) {
		m_StyleClassTitle = styleClassTitle;
	}

	public String getStyleText() {
		return ValueBindingSupport.getValue(m_StyleText, "styleText", this, null, getFacesContext());
	}

	public void setStyleText(String styleText) {
		m_StyleText = styleText;
	}

	public String getStyleClassText() {
		return ValueBindingSupport.getValue(m_StyleClassText, "styleClassText", this, null, getFacesContext());
	}

	public void setStyleClassText(String styleClassText) {
		m_StyleClassText = styleClassText;
	}

	public String getStyleLink() {
		return ValueBindingSupport.getValue(m_StyleLink, "styleLink", this, null, getFacesContext());
	}

	public void setStyleLink(String styleLink) {
		m_StyleLink = styleLink;
	}

	public String getStyleClassLink() {
		return ValueBindingSupport.getValue(m_StyleClassLink, "styleClassLink", this, null, getFacesContext());
	}

	public void setStyleClassLink(String styleClassLink) {
		m_StyleClassLink = styleClassLink;
	}

	@Override
	public void restoreState(FacesContext context, Object value) {
		Object[] state = (Object[]) value;
		super.restoreState(context, state[0]);
		m_Link = (String) state[1];
		m_Style = (String) state[2];
		m_StyleClass = (String) state[3];
		m_StyleClassLink = (String) state[4];
		m_StyleClassText = (String) state[5];
		m_StyleClassTitle = (String) state[6];
		m_StyleLink = (String) state[7];
		m_StyleText = (String) state[8];
		m_StyleTitle = (String) state[9];
		m_Text = (String) state[10];
		m_Title = (String) state[11];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] state = new Object[12];

		state[0] = super.saveState(context);
		state[1] = m_Link;
		state[2] = m_Style;
		state[3] = m_StyleClass;
		state[4] = m_StyleClassLink;
		state[5] = m_StyleClassText;
		state[6] = m_StyleClassTitle;
		state[7] = m_StyleLink;
		state[8] = m_StyleText;
		state[9] = m_StyleTitle;
		state[10] = m_Text;
		state[11] = m_Title;

		return state;
	}

}
