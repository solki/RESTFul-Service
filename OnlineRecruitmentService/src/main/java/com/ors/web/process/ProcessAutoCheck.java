package com.ors.web.process;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.ui.ModelMap;
import org.xml.sax.SAXException;

import com.ors.bean.ApplicationBean;
import com.ors.bean.AutoCheckBean;
import com.ors.bean.JobBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.AutoCheckDao;
import com.ors.dao.JobDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.AutoCheckJSONHandler;
import com.ors.web.helper.JobJSONHandler;

public class ProcessAutoCheck {

	ModelMap model;
	String appId, shortKey, licenseNumber, fullName, postcode;

	public ProcessAutoCheck(ModelMap model, String shortKey, String appId,
			String licenseNumber, String fullName, String postcode) {
		this.model = model;
		this.shortKey = shortKey;
		this.appId = appId;
		this.licenseNumber = licenseNumber;
		this.fullName = fullName;
		this.postcode = postcode;

	}

	public String excute() {
		AutoCheckDao achDao = new AutoCheckDao();
		String resultDetails = "";
		try {
			resultDetails = achDao.getAutoCheckResult(licenseNumber, fullName,
					postcode);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal server error");
			e.printStackTrace();
			return "error";
		}
		if (resultDetails == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal server error");
			return "error";
		}
		// Map<String, List<String>> header = null;
		Response res = null;
		res = achDao.addAutoCheck(appId, resultDetails, shortKey);
		if (res.getStatus() != 201 && res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		AutoCheckBean autocheck = null;
		res = achDao.getAutoCheckByApp(appId, shortKey);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		AutoCheckJSONHandler handler = new AutoCheckJSONHandler();
		autocheck = handler.JSONtoPOJO(res.readEntity(String.class));
		if (autocheck == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		ApplicationDao appDao = new ApplicationDao();
		AppJSONHandler app_handler = new AppJSONHandler();
		String res_app = "";
		res = appDao.getApp(appId);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		res_app = res.readEntity(String.class);
		ApplicationBean app = null;
		app = app_handler.JSONtoPOJO(res_app);
		if (app == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		
		if (app.getStatus().equals("created")) {
			res = appDao.updateAppStatus(appId, "autochecked");
			if (res.getStatus() != 200) {
				model.addAttribute("code", res.getStatus());
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			res = appDao.getApp(appId);
			if (res.getStatus() != 200) {
				model.addAttribute("code", res.getStatus());
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			res_app = res.readEntity(String.class);
			app = app_handler.JSONtoPOJO(res_app);
			if (app == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
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
		model.addAttribute("app", app);
		model.addAttribute("check", autocheck);

		return "appQuickView";
	}

}
