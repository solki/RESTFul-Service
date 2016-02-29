package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessAllApps {

	private ModelMap model;
	private String shortKey;

	public ProcessAllApps() {
	}

	public ProcessAllApps(ModelMap model, String shortKey) {
		super();
		this.model = model;
		this.shortKey = shortKey;
	}

	public String execute() {
		String returnData = "ApplicationList";
		ApplicationDao applicationDao = new ApplicationDao();
		Response res = applicationDao.getAllApps(shortKey);
		System.out.println(res.getStatusInfo().getReasonPhrase()
				+ " from get all apps Process");// test
		ArrayList<ApplicationBean> apps = null;
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		String r = "";
		r = res.readEntity(String.class);
		if (!r.equals("[]")) {
			AppJSONHandler handler = new AppJSONHandler();
			apps = handler.JSONtoPOJOList(r);
			if (apps == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
		}
		model.addAttribute("apps", apps);
		return returnData;

	}

}
