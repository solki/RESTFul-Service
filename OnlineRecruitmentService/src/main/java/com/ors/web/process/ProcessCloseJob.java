package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.JobBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.JobDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.JobJSONHandler;

public class ProcessCloseJob {

	private ModelMap model;
	private String jobId, shortKey;

	public ProcessCloseJob(ModelMap model, String jobId, String shortKey) {
		this.model = model;
		this.jobId = jobId;
		this.shortKey = shortKey;
	}

	public String execute() {
		String returnData = "welcomeMng";
		JobDao jobDao = new JobDao();
		Response res = jobDao.updateJobStatus(jobId, "closed");
		if (res == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "INTERNAL_SERVER_ERROR");
			return "error";
		}
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		} else {
			ApplicationDao appDao = new ApplicationDao();
			res = appDao.getAppsByJob(jobId, shortKey);
			if (res.getStatus() != 200 && res.getStatus() != 404) {
				model.addAttribute("code", res.getStatus());
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			} else if (res.getStatus() == 200) {
				AppJSONHandler appHandler = new AppJSONHandler();
				ArrayList<ApplicationBean> apps = appHandler
						.JSONtoPOJOList(res.readEntity(String.class));
				if (apps == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
				String isAllArchived = "yes";
				for (ApplicationBean app : apps) {
					if (!app.getStatus().equals("archived")) {
						isAllArchived = "no";
						break;
					}
				}
				if (isAllArchived.equals("yes")) {
					res = jobDao.updateJobStatus(jobId, "finalized");
					if (res.getStatus() != 200) {
						model.addAttribute("code", res.getStatus());
						model.addAttribute("message",
								res.getStatusInfo().getReasonPhrase());
						return "error";
					}
				}
			} else {
				res = jobDao.updateJobStatus(jobId, "finalized");
				if (res.getStatus() != 200) {
					model.addAttribute("code", res.getStatus());
					model.addAttribute("message",
							res.getStatusInfo().getReasonPhrase());
					return "error";
				}
			}
			
			res = jobDao.getAllJobs(shortKey);
			String r = "";
			int responseCode = res.getStatus();
			r = res.readEntity(String.class);
			if (responseCode != 200) {
				System.out.println(responseCode);
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			ArrayList<JobBean> jobs = null;
			if (!r.equals("[]")) {
				JobJSONHandler handler = new JobJSONHandler();

				jobs = handler.JSONtoPOJOList(r);
				if (jobs == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
			}

			// model.addAttribute("isAllArchived", isAllArchived);
			model.addAttribute("jobs", jobs);
			return returnData;
		}
	}

}
