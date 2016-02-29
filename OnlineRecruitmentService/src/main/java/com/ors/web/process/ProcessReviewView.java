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

public class ProcessReviewView {

	private String appId, shortKey;
	private ModelMap model;

	public ProcessReviewView(ModelMap model, String appId, String shortKey) {
		super();
		this.model = model;
		this.appId = appId;
		this.shortKey = shortKey;
	}

	public String execute() {
		String reviews = "";
		Response res = new ReviewDao().getReviewsByApp(appId, shortKey);
		reviews = res.readEntity(String.class);
		System.out.println(reviews);// test
		int responseCode = res.getStatus();
		ArrayList<ReviewBean> revs = null;
		if (responseCode == 200) {
			ReviewJSONHandler handler = new ReviewJSONHandler();
			revs = handler.JSONtoPOJOList(reviews);
			if (revs == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			int count = 0;
			for (ReviewBean rev: revs) {
				if (rev.getDecision().equals("accepted") || rev.getDecision().equals("rejected")) {
					count++;
				}
			}
			model.addAttribute("is_finished", count);
			model.addAttribute("count_revs", revs.size());
		} else {
			if (responseCode == 404) {
				revs = null;
			} else {
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
		}
		ApplicationDao appDao = new ApplicationDao();
		res = appDao.getApp(appId);
		if (res.getStatus() != 200) {
			model.addAttribute("code", responseCode);
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		String res_job = res.readEntity(String.class);
		ApplicationBean app = new AppJSONHandler().JSONtoPOJO(res_job);
		if (app == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		model.addAttribute("app", app);
		model.addAttribute("revs", revs);
		return "reviewDtails";

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
