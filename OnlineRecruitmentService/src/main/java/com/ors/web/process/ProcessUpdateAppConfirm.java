package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.JobBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.JobDao;
import com.ors.web.helper.JobJSONHandler;

public class ProcessUpdateAppConfirm {

	private String appId, jobId, licenseNumber, fullName, postcode, coverLetter,
			briefResume;
	private ModelMap model;

	public ProcessUpdateAppConfirm(String appId, String jobId,
			String licenseNumber, String fullName, String postcode,
			String coverLetter, String briefResume, ModelMap model) {
		this.appId = appId;
		this.jobId = jobId;
		this.licenseNumber = licenseNumber;
		this.fullName = fullName;
		this.postcode = postcode;
		this.coverLetter = coverLetter;
		this.briefResume = briefResume;
		this.model = model;
	}

	public String execute() {
		// check the job existing
		String returnData = "appQuickView";
		String res_job = "";
		JobDao jobDao = new JobDao();
		Response res = jobDao.getJob(jobId);
		int responseCode = res.getStatus();
		JobBean job = null;
		System.out.println(responseCode);
		if (responseCode == 200) {
			res_job = res.readEntity(String.class);
			JobJSONHandler handler = new JobJSONHandler();
			job = handler.JSONtoPOJO(res_job);
			if (job == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			if (!job.getStatus().equals("created")
					&& !job.getStatus().equals("fixed")
					&& !job.getStatus().equals("closed")) {
				model.addAttribute("code", "403");
				model.addAttribute("message", "Forbidden");
				return "error";
			}
		} else {
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}

		ApplicationBean app = new ApplicationBean();
		app.set_appId(appId);
		app.set_jobId(jobId);
		app.setLicenseNumber(licenseNumber);
		app.setFullName(fullName);
		app.setPostcode(postcode);
		app.setCoverLetter(coverLetter);
		app.setBriefResume(briefResume);
		app.setStatus("created");

		ApplicationDao appDao = new ApplicationDao();
		res = appDao.updateApp(app);
		if (res.getStatus() != 200 && res.getStatus() != 201) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}

		model.addAttribute("app", app);
		model.addAttribute("job", job);

		return returnData;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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

	public ModelMap getModel() {
		return model;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

}
