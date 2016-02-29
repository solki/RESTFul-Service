package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessDeleteApp {

	private String appId;
	private ModelMap model;

	public ProcessDeleteApp(ModelMap model, String appId) {
		this.appId = appId;
		this.model = model;
	}

	public String execute() {
		String returnData = "appQuickView";
		String res_app = "";
		ApplicationDao appDao = new ApplicationDao();
		Response res = appDao.getApp(appId);
		res_app = res.readEntity(String.class);
		// System.out.println(res_app);//test
		int responseCode = res.getStatus();
		ApplicationBean app = null;
		AppJSONHandler handler = new AppJSONHandler();
		if (responseCode == 200) {
			app = handler.JSONtoPOJO(res_app);
			if (app == null) {
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
		//System.out.println(app.getStatus());
		if (app.getStatus().equals("archived")) {
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"Forbidden, this application has already been deleted.");
			return "error";
		}
		res = appDao.deleteApp(appId);
		if (res == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "INTERNAL_SERVER_ERROR");
			return "error";
		}
		//codeChecker.setMsg(res.getStatusInfo().getReasonPhrase());
		if (res.getStatus() != 200) {
			//System.out.println("I'm here");
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"Forbidden, this application has already been deleted.");
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
		}
		model.addAttribute("app", app);
		return returnData;
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
