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

public class ProcessArchiveApp {

	private ModelMap model;
	private String appId, shortKey;

	public ProcessArchiveApp(ModelMap model, String appId, String shortKey) {
		this.model = model;
		this.appId = appId;
		this.shortKey = shortKey;
	}

	public String execute() {
		ApplicationDao appDao = new ApplicationDao();
		Response res = appDao.getApp(appId);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		AppJSONHandler handler = new AppJSONHandler();
		ApplicationBean app = handler.JSONtoPOJO(res.readEntity(String.class));
		if (app == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		res = appDao.getAppsByJob(app.get_jobId(), shortKey);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		ArrayList<ApplicationBean> apps = handler
				.JSONtoPOJOList(res.readEntity(String.class));
		if (apps == null || apps.size() == 0) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		JobDao jobDao = new JobDao();
		res = jobDao.getJob(app.get_jobId());
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		JobJSONHandler jobHandler = new JobJSONHandler();
		JobBean job = jobHandler.JSONtoPOJO(res.readEntity(String.class));
		if (job == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		if (!job.getStatus().equals("closed")) {
			return "done";
		}
		for (ApplicationBean ap : apps) {
			if (!ap.getStatus().equals("archived")) {
				return "done";
			}
		}
		res = jobDao.updateJobStatus(job.get_jobId(), "finalized");
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		return "done";
	}

	public ModelMap getModel() {
		return model;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getShortKey() {
		return shortKey;
	}

	public void setShortKey(String shortKey) {
		this.shortKey = shortKey;
	}

}
