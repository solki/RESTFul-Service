package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.AutoCheckBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.AutoCheckDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.AutoCheckJSONHandler;
import com.ors.web.helper.StatusCodeCheckHelper;

public class ProcessAssignment {

	private String appId;
	private ModelMap model;
	private String shortKey;
	private StatusCodeCheckHelper codeChecker;

	public ProcessAssignment(ModelMap model, String appId, String shortKey) {
		super();
		this.model = model;
		this.appId = appId;
		this.shortKey = shortKey;
		codeChecker = new StatusCodeCheckHelper();
		codeChecker.setModel(model);
	}

	public String execute() {
		String res_app = "";
		ApplicationDao appDao = new ApplicationDao();
		Response res = appDao.getApp(appId);
		res_app = res.readEntity(String.class);
		System.out.println(res_app);// test
		int responseCode = res.getStatus();
		if (responseCode != 200) {
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		ApplicationBean app = null;
		AppJSONHandler handler = new AppJSONHandler();
		app = handler.JSONtoPOJO(res_app);
		if (app == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		if (!app.getStatus().equals("created")
				&& !app.getStatus().equals("autochecked")) {
			model.addAttribute("code", "400");
			model.addAttribute("message", "BAD REQUEST");
			return "error";
		}

		res = appDao.updateAppStatus(appId, "in_review");
		if (res == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "INTERNAL_SERVER_ERROR");
			return "error";
		}
		if (res.getStatus() != 200) {
			// System.out.println("I'm here");
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		} else {
			res = appDao.getApp(appId);
			String r = "";
			responseCode = res.getStatus();
			r = res.readEntity(String.class);
			if (responseCode != 200) {
				System.out.println(responseCode);
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			// ArrayList<JobBean> jobs = null;
			app = handler.JSONtoPOJO(r);
			if (app == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			// model.addAttribute("app", app);
			AutoCheckDao acDao = new AutoCheckDao();
			Response res_ac = acDao.getAutoCheckByApp(app.get_appId(),
					"001-manager");
			if (res_ac.getStatus() != 404 && res_ac.getStatus() != 200) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			} else if (res_ac.getStatus() == 404) {
				model.addAttribute("app", app);

			} else {
				String ac = res_ac.readEntity(String.class);
				AutoCheckJSONHandler achandler = new AutoCheckJSONHandler();
				AutoCheckBean autocheck = achandler.JSONtoPOJO(ac);
				if (autocheck == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
				model.addAttribute("app", app);
				model.addAttribute("check", autocheck);

			}
			Response apps_res = appDao.getAllApps(shortKey);
			String appStr = apps_res.readEntity(String.class);
			ArrayList<ApplicationBean> apps = handler.JSONtoPOJOList(appStr);
			if (apps == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			model.addAttribute("apps", apps);
			return "ApplicationList";
		}

	}
}
