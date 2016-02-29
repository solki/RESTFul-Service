package com.ors.bean;

public class UserBean {

	private String uid;
	private String pwd;
	private String shortKey;
	private String lastName;
	private String firstName;
	private String role;
	private String department;

	public UserBean() {

	}

	public UserBean(String uid, String pwd, String shortKey, String lastName, String firstName, String role,
			String department) {
		super();
		this.uid = uid;
		this.pwd = pwd;
		this.shortKey = shortKey;
		this.lastName = lastName;
		this.firstName = firstName;
		this.role = role;
		this.department = department;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getShortKey() {
		return shortKey;
	}

	public void setShortKey(String shortKey) {
		this.shortKey = shortKey;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String string) {
		this.role = string;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
