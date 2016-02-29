package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessCandiAppView {
	private String appId;
	private ModelMap model;

	public ProcessCandiAppView(ModelMap model, String appId) {

		this.model = model;
		this.appId = appId;
	}

	public String excecute() {
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
			if (app.getStatus().equals("archived")) {
				model.addAttribute("code", "404");
				model.addAttribute("message", "Not Found");
				return "error";
			}

		} else {
			System.out.println(responseCode);// test
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		return "appQuickView";
	}

}
