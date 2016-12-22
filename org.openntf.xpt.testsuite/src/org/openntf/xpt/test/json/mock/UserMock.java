package org.openntf.xpt.test.json.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openntf.xpt.core.json.annotations.JSONEntity;
import org.openntf.xpt.core.json.annotations.JSONObject;

@JSONObject(JavaFieldPrefix = "")
public class UserMock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONEntity(jsonproperty = "name")
	private String name;
	@JSONEntity(jsonproperty = "accountnumber")
	private int accountNumber;
	@JSONEntity(jsonproperty = "accountobject")
	private Integer accountObject;
	@JSONEntity(jsonproperty = "salary")
	private double salary;
	@JSONEntity(jsonproperty = "salaryobject")
	private double salaryObject;
	@JSONEntity(jsonproperty = "tags")
	private List<String> tags;
	
	@JSONEntity(jsonproperty = "children")
	private List<UserMock> children = new ArrayList<UserMock>();
    
	public static UserMock buildMockWithChildren() {
		UserMock mock = buildMock("Marco Müller", 42, 123000, Arrays.asList("Manager", "Developer", "Leader"));
		mock.addChildern(buildMock("René Meier", 21, 90000, Arrays.asList("Developer")));
		mock.addChildern(buildMock("Cecile Nünlist", 19, 99000, new ArrayList<String>()));
		return mock;
	}
	public static UserMock buildSimpleMock() {
		UserMock mock = buildMock("Marco Müller", 42, 123000, Arrays.asList("Manager", "Developer", "Leader"));
		return mock;
	}

	public static UserMock buildMock(String name, int account, double salary, List<String> tags) {
		UserMock mock = new UserMock();
		mock.name = name;
		mock.accountObject = account;
		mock.accountNumber = account;
		mock.salary = salary;
		mock.salaryObject = salary;
		mock.tags = tags;
		return mock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Integer getAccountObject() {
		return accountObject;
	}

	public void setAccountObject(Integer accountObject) {
		this.accountObject = accountObject;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public double getSalaryObject() {
		return salaryObject;
	}

	public void setSalaryObject(double salaryObject) {
		this.salaryObject = salaryObject;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<UserMock> getChildren() {
		return children;
	}

	public void setChildren(List<UserMock> children) {
		this.children = children;
	}

	public void addChildern(UserMock child) {
		children.add(child);
	}

}
