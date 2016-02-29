package au.edu.unsw.soacourse.humanresource.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "_appId", "_jobId", "licenseNumber", "fullName",
		"postcode", "coverLetter", "briefResume", "status" })
public class Application {

	public enum AppStatus {
		created, autochecked, in_review, shortlisted, notshortlisted, invited, itv_accepted, itv_rejected, accepted, rejected, acpt_notified, rjct_notified, archived;
	}

	private String _appId, _jobId, licenseNumber, fullName, postcode,
			coverLetter, briefResume;
	private AppStatus status;

	public Application() {
		super();
	}

	public Application(String _appId, String _jobId, String licenseNumber,
			String fullName, String postcode, String coverLetter,
			String briefResume, AppStatus status) {
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

	public AppStatus getStatus() {
		return status;
	}

	public void setStatus(AppStatus status) {
		this.status = status;
	}

	public String toString(AppStatus status) {
		return status.name();
	}

}
