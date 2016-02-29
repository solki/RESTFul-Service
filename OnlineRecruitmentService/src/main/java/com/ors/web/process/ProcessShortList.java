package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.ReviewBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.ReviewDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.ReviewJSONHandler;

public class ProcessShortList {

	ModelMap model;
	String appId, shortKey;

	public ProcessShortList(ModelMap model, String appId, String shortKey) {
		super();
		this.model = model;
		this.appId = appId;
		this.shortKey = shortKey;
	}

	public String execute() {

		ReviewDao reviewDao = new ReviewDao();
		Response res = reviewDao.getReviewsByApp(appId, shortKey);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
		}
		ReviewJSONHandler handler = new ReviewJSONHandler();
		ArrayList<ReviewBean> revs = handler
				.JSONtoPOJOList(res.readEntity(String.class));
		if (revs == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		System.out.println(revs.size() + " from process review confirm");
		ApplicationDao appDao = new ApplicationDao();
		if (revs.size() == 2) {
			if (revs.get(0).getDecision().equals("shortlisted")
					&& revs.get(1).getDecision().equals("shortlisted")) {
				// change application status to shortlisted or notshortlisted
				res = appDao.updateAppStatus(appId, "shortlisted");
				if (res.getStatus() != 200) {
					model.addAttribute("code", res.getStatus());
					model.addAttribute("message",
							res.getStatusInfo().getReasonPhrase());
					return "error";
				}
			} else {
				res = appDao.updateAppStatus(appId, "notshortlisted");
				if (res.getStatus() != 200) {
					model.addAttribute("code", res.getStatus());
					model.addAttribute("message",
							res.getStatusInfo().getReasonPhrase());
					return "error";
				}

			}
			model.addAttribute("revs", revs);
		} else {
			model.addAttribute("code", "403");
			model.addAttribute("message", "Forbidden");
			return "error";
		}
		// ApplicationDao appDao = new ApplicationDao();
		res = appDao.getApp(appId);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		String res_app = res.readEntity(String.class);
		AppJSONHandler appHandler = new AppJSONHandler();
		ApplicationBean app = appHandler.JSONtoPOJO(res_app);
		if (app == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		model.addAttribute("app", app);
		return "reviewDtails";
	}

}
