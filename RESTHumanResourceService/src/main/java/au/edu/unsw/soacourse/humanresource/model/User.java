package au.edu.unsw.soacourse.humanresource.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlType(propOrder={"_uid", "_pwd", "shortKey", "lastName", "firstName", "role", "department"})
public class User {

	public enum RoleType {
		reviewer,manager
	}
	

	private String _uid;
	private String _pwd;
	private String ShortKey;
	private String LastName;
	private String FirstName;
	private RoleType Role;
	private String Department;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String _uid, String _pwd, String shortKey, String lastName,
			String firstName, RoleType role, String department) {
		super();
		this._uid = _uid;
		this._pwd = _pwd;
		ShortKey = shortKey;
		LastName = lastName;
		FirstName = firstName;
		Role = role;
		Department = department;
	}

	public String get_uid() {
		return _uid;
	}

	public void set_uid(String _uid) {
		this._uid = _uid;
	}

	public String get_pwd() {
		return _pwd;
	}

	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
	}

	public String getShortKey() {
		return ShortKey;
	}

	public void setShortKey(String shortKey) {
		ShortKey = shortKey;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public RoleType getRole() {
		return Role;
	}

	public void setRole(RoleType role) {
		Role = role;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}
	
	
	




}
