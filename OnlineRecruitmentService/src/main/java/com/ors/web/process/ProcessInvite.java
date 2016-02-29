package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.dao.ApplicationDao;

public class ProcessInvite {

	private ModelMap model;
	private String appId;

	public ProcessInvite(ModelMap model, String appId) {
		this.model = model;
		this.appId = appId;
	}

	public String execute() {
		ApplicationDao appDao = new ApplicationDao();
		Response res = appDao.updateAppStatus(appId, "invited");
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
		return "invitationSentSuccess";
	}

}
