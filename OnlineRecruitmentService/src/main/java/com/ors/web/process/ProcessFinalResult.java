package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ReviewBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.ReviewDao;
import com.ors.web.helper.ReviewJSONHandler;

public class ProcessFinalResult {

	private ModelMap model;
	private String appId, shortKey;

	public ProcessFinalResult(ModelMap model, String appId, String shortKey) {
		this.model = model;
		this.appId = appId;
		this.shortKey = shortKey;
	}

	public String execute() {
		ReviewDao reviewDao = new ReviewDao();
		ReviewJSONHandler handler = new ReviewJSONHandler();
		Response res = reviewDao.getReviewsByApp(appId, shortKey);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		String res_revs = res.readEntity(String.class);
		ArrayList<ReviewBean> revs = handler.JSONtoPOJOList(res_revs);
		if (revs == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "INTERNAL_SERVER_ERROR");
			return "error";
		}
		int count = 0;
		for (ReviewBean review : revs) {
			if (review.getDecision().equals("rejected")) {
				count++;
			}
		}
		ApplicationDao appDao = new ApplicationDao();
		if (count == 0) {
			res = appDao.updateAppStatus(appId, "accepted");
		} else {
			res = appDao.updateAppStatus(appId, "rejected");
		}
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
