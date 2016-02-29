package com.ors.web.process;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.JobBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.JobDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.JobJSONHandler;

public class ProcessConfirmApplyJob {

	private ModelMap model;
	private String jobId, licenseNumber, fullName, postcode, coverLetter,
			briefResume;

	public ProcessConfirmApplyJob(ModelMap model, String jobId,
			String licenseNumber, String fullName, String postcode,
			String coverLetter, String briefResume) {
		super();
		this.model = model;
		this.jobId = jobId;
		this.licenseNumber = licenseNumber;
		this.fullName = fullName;
		this.postcode = postcode;
		this.coverLetter = coverLetter;
		this.briefResume = briefResume;

	}

	public String execute() {
		if (licenseNumber.trim().equals("")) {
			model.addAttribute("code", "400");
			model.addAttribute("message",
					"Bad Request, License Number is invalid");
			return "error";
		}
		String res_job = "";
		JobDao jobDao = new JobDao();
		Response res = jobDao.getJob(jobId);
		res_job = res.readEntity(String.class);
		int responseCode = res.getStatus();
		JobBean job = null;
		if (responseCode == 200) {
			JobJSONHandler handler = new JobJSONHandler();
			job = handler.JSONtoPOJO(res_job);
			if (job == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}

		} else {
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		model.addAttribute("job", job);
		/*************************************/
		String returnData = "applyResult";
		ApplicationDao appDao = new ApplicationDao();

		res = appDao.postApp(jobId, licenseNumber, fullName, postcode,
				coverLetter, briefResume);
		if (res.getStatus() != 201) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		ApplicationBean newApp = null;
		jobDao.updateJobStatus(jobId, "fixed");

		MultivaluedMap<String, Object> headers = res.getHeaders();
		System.out.println(headers.toString());

		String link = headers.get("Location").get(0).toString();
		System.out.println(link);
		res = appDao.getAppByLink(link);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}

		String res_app = "";
		res_app = res.readEntity(String.class);
		AppJSONHandler handler = new AppJSONHandler();
		newApp = handler.JSONtoPOJO(res_app);
		if (newApp == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		model.addAttribute("app", newApp);
		String[] ptLink = link.split("/");
		String generatedMsg = "http://localhost:8080/OnlineRecruitmentService/appdetail?appId="
				+ ptLink[ptLink.length - 1];
		model.addAttribute("showLink", generatedMsg);

		return returnData;
	}

}
