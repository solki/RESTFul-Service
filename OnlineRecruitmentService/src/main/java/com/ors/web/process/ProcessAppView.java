package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.AutoCheckBean;
import com.ors.bean.JobBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.AutoCheckDao;
import com.ors.dao.JobDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.AutoCheckJSONHandler;
import com.ors.web.helper.JobJSONHandler;

public class ProcessAppView {

	private String appId;
	private ModelMap model;

	public ProcessAppView(ModelMap model, String appId) {
		super();
		this.model = model;
		this.appId = appId;
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

		} else {
			System.out.println(responseCode);// test
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}

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
			AutoCheckJSONHandler handler = new AutoCheckJSONHandler();
			AutoCheckBean autocheck = handler.JSONtoPOJO(ac);
			model.addAttribute("app", app);
			model.addAttribute("check", autocheck);
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
		model.addAttribute("job", job);
		return "appQuickView";
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
