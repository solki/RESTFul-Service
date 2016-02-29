package com.ors.bean;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "_appId", "_jobId", "licenseNumber", "fullName",
		"postcode", "coverLetter", "briefResume", "status" })
public class ApplicationBean {
	private String _appId, _jobId, licenseNumber, fullName, postcode,
			coverLetter, briefResume, status;

	public ApplicationBean() {
		super();
	}

	public ApplicationBean(String _appId, String _jobId, String licenseNumber,
			String fullName, String postcode, String coverLetter,
			String briefResume, String status) {
		super();
		this._appId = _appId;
		this._jobId = _jobId;
		this.licenseNumber = licenseNumber;
		this.fullName = fullName;
		this.postcode = postcode;
		this.coverLetter = coverLetter;
		this.briefResume = briefResume;
		this.status = status;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String get_appId() {
		return _appId;
	}

	public void set_appId(String _appId) {
		this._appId = _appId;
	}

	public String get_jobId() {
		return _jobId;
	}

	public void set_jobId(String _jobId) {
		this._jobId = _jobId;
	}

	public String getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}

	public String getBriefResume() {
		return briefResume;
	}

	public void setBriefResume(String briefResume) {
		this.briefResume = briefResume;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
