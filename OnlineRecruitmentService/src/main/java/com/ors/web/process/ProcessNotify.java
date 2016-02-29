package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessNotify {

	private ModelMap model;
	private String jobId, shortKey;

	public ProcessNotify(ModelMap model, String jobId, String shortKey) {
		this.model = model;
		this.jobId = jobId;
		this.shortKey = shortKey;
	}

	public String execute() {
		ApplicationDao appDao = new ApplicationDao();
		Response res = appDao.getAppsByJob(jobId, shortKey);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		AppJSONHandler handler = new AppJSONHandler();
		ArrayList<ApplicationBean> apps = null;
		apps = handler.JSONtoPOJOList(res.readEntity(String.class));
		if (apps == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		for (ApplicationBean app : apps) {
			if (app.getStatus().equals("archived")) {
				continue;
			}
			if (app.getStatus().equals("accepted")) {
				res = appDao.updateAppStatus(app.get_appId(), "acpt_notified");
			} else if (app.getStatus().equals("rejected")) {
				res = appDao.updateAppStatus(app.get_appId(), "rjct_notified");
			} else {
				model.addAttribute("code", "400");
				model.addAttribute("message", "Bad Request");
				return "error";
			}
			if (res.getStatus() != 200) {
				model.addAttribute("code", res.getStatus());
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
		}
		
		return "done";
	}

}
