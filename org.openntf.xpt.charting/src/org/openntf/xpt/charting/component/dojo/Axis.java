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
package org.openntf.xpt.charting.component.dojo;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

public class Axis extends ValueBindingObjectImpl {

	private Integer m_Min;
	private Integer m_Max;
	private boolean m_Vertical = false;
	private boolean m_LeftBottom = true;
	private String m_Alias;
	private String m_Titl;
	private Integer m_TitleGap;
	private String m_TitleFontColor;
	private String m_TitleOrientation;
	private Boolean m_MajorLabels;
	private Boolean m_MinorTicks;
	private Boolean m_MinorLabels;
	private Boolean m_MicroTicks;
	private Boolean m_Natural;
	private Boolean m_Fixed;
	private Integer m_MajorTickStep;
	private Integer m_MinorTickStep;
	private Integer m_MicroTickStep;
	private String m_Stroke;
	private String m_Font;
	private String m_FontColor;
	public Integer getMin() {
		return m_Min;
	}
	public void setMin(Integer min) {
		m_Min = min;
	}
	public Integer getMax() {
		return m_Max;
	}
	public void setMax(Integer max) {
		m_Max = max;
	}
	public boolean isVertical() {
		return m_Vertical;
	}
	public void setVertical(boolean vertical) {
		m_Vertical = vertical;
	}
	public boolean isLeftBottom() {
		return m_LeftBottom;
	}
	public void setLeftBottom(boolean leftBottom) {
		m_LeftBottom = leftBottom;
	}
	public String getAlias() {
		return m_Alias;
	}
	public void setAlias(String alias) {
		m_Alias = alias;
	}
	public String getTitl() {
		return m_Titl;
	}
	public void setTitl(String titl) {
		m_Titl = titl;
	}
	public Integer getTitleGap() {
		return m_TitleGap;
	}
	public void setTitleGap(Integer titleGap) {
		m_TitleGap = titleGap;
	}
	public String getTitleFontColor() {
		return m_TitleFontColor;
	}
	public void setTitleFontColor(String titleFontColor) {
		m_TitleFontColor = titleFontColor;
	}
	public String getTitleOrientation() {
		return m_TitleOrientation;
	}
	public void setTitleOrientation(String titleOrientation) {
		m_TitleOrientation = titleOrientation;
	}
	public Boolean getMajorLabels() {
		return m_MajorLabels;
	}
	public void setMajorLabels(Boolean majorLabels) {
		m_MajorLabels = majorLabels;
	}
	public Boolean getMinorTicks() {
		return m_MinorTicks;
	}
	public void setMinorTicks(Boolean minorTicks) {
		m_MinorTicks = minorTicks;
	}
	public Boolean getMinorLabels() {
		return m_MinorLabels;
	}
	public void setMinorLabels(Boolean minorLabels) {
		m_MinorLabels = minorLabels;
	}
	public Boolean getMicroTicks() {
		return m_MicroTicks;
	}
	public void setMicroTicks(Boolean microTicks) {
		m_MicroTicks = microTicks;
	}
	public Boolean getNatural() {
		return m_Natural;
	}
	public void setNatural(Boolean natural) {
		m_Natural = natural;
	}
	public Boolean getFixed() {
		return m_Fixed;
	}
	public void setFixed(Boolean fixed) {
		m_Fixed = fixed;
	}
	public Integer getMajorTickStep() {
		return m_MajorTickStep;
	}
	public void setMajorTickStep(Integer majorTickStep) {
		m_MajorTickStep = majorTickStep;
	}
	public Integer getMinorTickStep() {
		return m_MinorTickStep;
	}
	public void setMinorTickStep(Integer minorTickStep) {
		m_MinorTickStep = minorTickStep;
	}
	public Integer getMicroTickStep() {
		return m_MicroTickStep;
	}
	public void setMicroTickStep(Integer microTickStep) {
		m_MicroTickStep = microTickStep;
	}
	public String getStroke() {
		return m_Stroke;
	}
	public void setStroke(String stroke) {
		m_Stroke = stroke;
	}
	public String getFont() {
		return m_Font;
	}
	public void setFont(String font) {
		m_Font = font;
	}
	public String getFontColor() {
		return m_FontColor;
	}
	public void setFontColor(String fontColor) {
		m_FontColor = fontColor;
	}
	
	

}
