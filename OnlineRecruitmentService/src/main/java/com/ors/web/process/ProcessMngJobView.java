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

public class ProcessMngJobView {

	private String jobId, shortKey;
	private ModelMap model;

	public ProcessMngJobView(ModelMap model, String jobId, String shortKey) {
		super();
		this.model = model;
		this.jobId = jobId;
		this.shortKey = shortKey;
	}

	public String execute() {

		String res_job = "";
		Response res = new JobDao().getJob(jobId);
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
			System.out.println(responseCode);// test
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		model.addAttribute("job", job);
		ApplicationDao appDao = new ApplicationDao();
		res = appDao.getAppsByJob(jobId, shortKey);
		if (res.getStatus() == 404) {
			return "quickView";
		} else if (res.getStatus() == 200) {
			ArrayList<ApplicationBean> apps = new ArrayList<ApplicationBean>();
			String res_apps = res.readEntity(String.class);
			AppJSONHandler appHandler = new AppJSONHandler();
			apps = appHandler.JSONtoPOJOList(res_apps);
			if (apps == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			String allDone = "yes";
			if (job.getStatus().equals("closed")) {

				for (ApplicationBean app : apps) {
					if (!app.getStatus().equals("archived")
							&& !app.getStatus().equals("accepted")
							&& !app.getStatus().equals("rejected")) {
						allDone = "no";
						break;
					}
				}
			} else {
				allDone = "no";

			}
			model.addAttribute("allDone", allDone);
			model.addAttribute("apps", apps);
			return "quickView";

		} else {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}

	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

}
