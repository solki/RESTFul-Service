package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessUpdateApp {

	private String appId;
	private ModelMap model;

	public ProcessUpdateApp(String appId, ModelMap model) {
		this.appId = appId;
		this.model = model;
	}

	public String execute() {
		String res_app = "";
		Response res = new ApplicationDao().getApp(appId);
		res_app = res.readEntity(String.class);
		System.out.println(res_app);// test
		int responseCode = res.getStatus();
		ApplicationBean app = null;
		if (responseCode == 200) {
			AppJSONHandler handler = new AppJSONHandler();
			app = handler.JSONtoPOJO(res_app);
			if (app == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			if (!app.getStatus().equals("created")) {
				model.addAttribute("code", "403");
				model.addAttribute("message", "Forbidden");
				return "error";
			}
		} else {
			System.out.println(responseCode);// test
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}

		model.addAttribute("app", app);
		return "updateAppForm";
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public ModelMap getModel() {
		return model;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

}
