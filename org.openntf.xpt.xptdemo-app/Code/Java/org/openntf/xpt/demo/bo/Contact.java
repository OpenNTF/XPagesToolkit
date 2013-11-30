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
package org.openntf.xpt.demo.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;

@DominoStore(Form = "Contact", PrimaryFieldClass = String.class, PrimaryKeyField = "ID", View = "LUPContactByID")
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DominoEntity(FieldName = "ID")
	private String m_ID;
	@DominoEntity(FieldName = "FirstName")
	private String m_FirstName;
	@DominoEntity(FieldName = "LastName")
	private String m_LastName;
	@DominoEntity(FieldName = "City")
	private String m_City;
	@DominoEntity(FieldName = "Email")
	private String m_Email;
	@DominoEntity(FieldName = "State")
	private String m_State;
	@DominoEntity(FieldName = "Observer", isNames = true )
	private List<String> m_Observers;
	@DominoEntity(FieldName="Salary")
	private double m_Salary;
	@DominoEntity(FieldName="Salary")
	private double m_CompanyCar;
	@DominoEntity(FieldName="JobFunction")
	private String m_JobFunction;
	@DominoEntity(FieldName="Devices")
	private int	m_Devices;
	@DominoEntity(FieldName="LastInterview")
	private Date m_LastInterview;

	public String getID() {
		return m_ID;
	}

	public void setID(String id) {
		m_ID = id;
	}

	public String getFirstName() {
		return m_FirstName;
	}

	public void setFirstName(String firstName) {
		m_FirstName = firstName;
	}

	public String getLastName() {
		return m_LastName;
	}

	public void setLastName(String lastName) {
		m_LastName = lastName;
	}

	public String getCity() {
		return m_City;
	}

	public void setCity(String city) {
		m_City = city;
	}

	public String getEmail() {
		return m_Email;
	}

	public void setEmail(String email) {
		m_Email = email;
	}

	public String getState() {
		return m_State;
	}

	public void setState(String state) {
		m_State = state;
	}

	public void setObservers(List<String> observers) {
		m_Observers = observers;
	}

	public List<String> getObservers() {
		return m_Observers;
	}

	public void addObserver(String strObserver) {
		if (m_Observers == null) {
			m_Observers = new ArrayList<String>();
		}
		if (!m_Observers.contains(strObserver)) {
			m_Observers.add(strObserver);
		}
	}

	public void removeObserver(String effectiveUserName) {
		if (m_Observers != null) {
			m_Observers.remove(effectiveUserName);
		}

	}

	public void setSalary(double salary) {
		m_Salary = salary;
	}

	public double getSalary() {
		return m_Salary;
	}

	public void setCompanyCar(double companyCar) {
		m_CompanyCar = companyCar;
	}

	public double getCompanyCar() {
		return m_CompanyCar;
	}

	public void setJobFunction(String jobFunction) {
		m_JobFunction = jobFunction;
	}

	public String getJobFunction() {
		return m_JobFunction;
	}

	public void setDevices(int devices) {
		m_Devices = devices;
	}

	public int getDevices() {
		return m_Devices;
	}

	public void setLastInterview(Date lastInterview) {
		m_LastInterview = lastInterview;
	}

	public Date getLastInterview() {
		return m_LastInterview;
	}

}
