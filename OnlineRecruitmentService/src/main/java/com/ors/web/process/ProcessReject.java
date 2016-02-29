package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessReject {
	private ModelMap model;
	private String appId;

	public ProcessReject(ModelMap model, String appId) {
		this.model = model;
		this.appId = appId;

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
			model.addAttribute("message", "INTERNAL_SERVER_ERROR");
			return "error";
		}
		if (!app.getStatus().equals("itv_accepted")) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "Forbidden, you are not allowed to do that!");
			return "error";
		}
		res = appDao.updateAppStatus(appId, "rejected");
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
		}
		return "done";
	}
}
