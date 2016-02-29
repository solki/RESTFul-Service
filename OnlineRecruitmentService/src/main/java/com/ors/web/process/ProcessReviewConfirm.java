package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.ReviewDao;
import com.ors.web.helper.AppJSONHandler;

public class ProcessReviewConfirm {

	private ModelMap model;
	private String uid, appId, jobId, comments, decision, shortKey;

	public ProcessReviewConfirm(ModelMap model, String uid, String appId,
			String jobId, String comments, String decision, String shortKey) {
		super();
		this.model = model;
		this.uid = uid;
		this.appId = appId;
		this.jobId = jobId;
		this.comments = comments;
		this.decision = decision;
		this.shortKey = shortKey;
	}

	public String execute() {
		String returnData = "reviewResult";

		if (comments.equals("")) {
			comments = "No Comment";
		}
		if (decision.equals("")) {
			model.addAttribute("code", "400");
			model.addAttribute("message", "Bad Request, decision is missing.");
			return "error";
		}
		// add review object
		ReviewDao reviewDao = new ReviewDao();
		Response res = reviewDao.addReview(appId, uid, comments, decision,
				shortKey);
		if (res.getStatus() != 201) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			returnData = "error";
		}
		ApplicationDao appDao = new ApplicationDao();
		res = appDao.getApp(appId);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			returnData = "error";
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
		return returnData;
	}

	public ModelMap getModel() {
		return model;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

}
