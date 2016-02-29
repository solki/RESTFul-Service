package com.ors.bean;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"_jobId", "closingDate", "jobDescriptions", "location", "positionType", "salary", "department", "status"})
public class JobBean {

	private String _jobId;
	private String closingDate;
	private String salary;
	private String positionType;
	private String location;
	private String jobDescriptions;
	private String department;
	private String status;

	public JobBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobBean(String _jobId, String closingDate, String salary,
			String positionType, String location, String jobDescriptions,
			String department, String status) {
		super();
		this._jobId = _jobId;
		this.closingDate = closingDate;
		this.salary = salary;
		this.positionType = positionType;
		this.location = location;
		this.jobDescriptions = jobDescriptions;
		this.department = department;
		this.status = status;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String get_jobId() {
		return _jobId;
	}

	public void set_jobId(String _jobId) {
		this._jobId = _jobId;
	}

	public String getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getJobDescriptions() {
		return jobDescriptions;
	}

	public void setJobDescriptions(String jobDescriptions) {
		this.jobDescriptions = jobDescriptions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
