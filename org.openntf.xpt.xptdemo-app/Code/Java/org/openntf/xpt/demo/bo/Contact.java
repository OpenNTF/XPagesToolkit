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
import java.util.LinkedList;
import java.util.List;

import org.openntf.xpt.core.dss.annotations.DominoEntity;
import org.openntf.xpt.core.dss.annotations.DominoStore;
import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;

import com.ibm.xsp.http.MimeMultipart;

@JSONObject
@DominoStore(Form = "Contact", PrimaryFieldClass = String.class, PrimaryKeyField = "ID", View = "LUPContactByID")
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONEntity(jsonproperty = "id")
	@DominoEntity(FieldName = "ID")
	private String m_ID;
	@JSONEntity(jsonproperty = "firstname")
	@DominoEntity(FieldName = "FirstName")
	private String m_FirstName;
	@JSONEntity(jsonproperty = "lastname")
	@DominoEntity(FieldName = "LastName")
	private String m_LastName;
	@JSONEntity(jsonproperty = "city")
	@DominoEntity(FieldName = "City")
	private String m_City;
	@JSONEntity(jsonproperty = "email")
	@DominoEntity(FieldName = "Email")
	private String m_Email;
	@JSONEntity(jsonproperty = "state")
	@DominoEntity(FieldName = "State")
	private String m_State;
	@JSONEntity(jsonproperty = "observer")
	@DominoEntity(FieldName = "Observer", isNames = true)
	private List<String> m_Observers;

	// CHANGELOG Demo/TestCase
	@DominoEntity(FieldName = "Salary", changeLog = true)
	private double m_Salary;
	@DominoEntity(FieldName = "CompanyCar", changeLog = true)
	private double m_CompanyCar;
	@JSONEntity(jsonproperty = "jobfunction")
	@DominoEntity(FieldName = "JobFunction", changeLog = true)
	private String m_JobFunction;
	@DominoEntity(FieldName = "Devices", changeLog = true)
	private int m_Devices;
	@DominoEntity(FieldName = "LastInterview", changeLog = true)
	private Date m_LastInterview;

	@DominoEntity(FieldName = "Latitude", changeLog = true)
	private Double m_Latitude;
	@DominoEntity(FieldName = "Longitude", changeLog = true)
	private Double m_Longitude;
	@DominoEntity(FieldName = "Elevation", changeLog = true)
	private Integer m_Elevation;

	@DominoEntity(FieldName = "BodyMIME")
	private MimeMultipart m_Comment;

	@DominoEntity(FieldName = "TagCloud")
	private List<String> m_TagCloud;

	@DominoEntity(FieldName = "contactStatus")
	private ContactStatus m_Status = ContactStatus.UNKNOWN;

	@DominoEntity(FieldName = "ModDate")
	private Date m_ModDate;

	@DominoEntity(FieldName = "RESP", embedded = true)
	private Responsible m_Responsible = new Responsible();

	@DominoEntity(FieldName = "OTHER", embedded = true)
	private List<Address> m_OtherAddresses;

	@DominoEntity(FieldName = "PrimaryContact")
	private boolean m_PrimaryContact;

	@DominoEntity(FieldName = "SupportResponsible")
	private String m_SupportResponsible;

	@DominoEntity(FieldName = "OtherSupportResponsible")
	private List<String> m_OtherSupportResponsible;

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

	public void setLatitude(Double latitude) {
		m_Latitude = latitude;
	}

	public Double getLatitude() {
		return m_Latitude;
	}

	public void setLongitude(Double longitude) {
		m_Longitude = longitude;
	}

	public Double getLongitude() {
		return m_Longitude;
	}

	public void setElevation(Integer elevation) {
		m_Elevation = elevation;
	}

	public Integer getElevation() {
		return m_Elevation;
	}

	public MimeMultipart getComment() {
		return m_Comment;
	}

	public void setComment(MimeMultipart comment) {
		m_Comment = comment;
	}

	public void setTagCloud(List<String> tagCloud) {
		m_TagCloud = tagCloud;
	}

	public List<String> getTagCloud() {
		return m_TagCloud;
	}

	public void setStatus(ContactStatus status) {
		m_Status = status;
	}

	public ContactStatus getStatus() {
		return m_Status;
	}

	public void setStatusTXT(String status) {
		m_Status = ContactStatus.valueOf(status);
	}

	public String getStatusTXT() {
		return m_Status.name();
	}

	public void setModDate(Date modDate) {
		m_ModDate = modDate;
	}

	public Date getModDate() {
		return m_ModDate;
	}

	public Responsible getResponsible() {
		if (m_Responsible == null) {
			m_Responsible = new Responsible();
		}
		return m_Responsible;
	}

	public void setResponsible(Responsible responsible) {
		m_Responsible = responsible;
	}

	public void setOtherAddresses(List<Address> ohterAddresses) {
		m_OtherAddresses = ohterAddresses;
	}

	public List<Address> getOtherAddresses() {
		return m_OtherAddresses;
	}

	public void addOtherAddresses(Address addr) {
		if (m_OtherAddresses == null) {
			m_OtherAddresses = new LinkedList<Address>();
		}
		m_OtherAddresses.add(addr);
	}

	public void setPrimaryContact(boolean primaryContact) {
		m_PrimaryContact = primaryContact;
	}

	public boolean isPrimaryContact() {
		return m_PrimaryContact;
	}

	public void setSupportResponsible(String supportResponsible) {
		m_SupportResponsible = supportResponsible;
	}

	public String getSupportResponsible() {
		return m_SupportResponsible;
	}

	public void setOtherSupportResponsible(List<String> otherSupportResponsible) {
		m_OtherSupportResponsible = otherSupportResponsible;
	}

	public List<String> getOtherSupportResponsible() {
		return m_OtherSupportResponsible;
	}

}
