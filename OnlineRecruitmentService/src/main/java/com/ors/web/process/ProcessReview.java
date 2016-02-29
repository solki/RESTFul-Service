package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.JobBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.JobDao;
import com.ors.dao.ReviewDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.JobJSONHandler;

public class ProcessReview {

	private String uid, appId, shortKey;
	private ModelMap model;

	public ProcessReview(ModelMap model, String uid, String appId,
			String shortKey) {

		this.appId = appId;
		this.uid = uid;
		this.shortKey = shortKey;
		this.model = model;
	}

	public String execute() {
		ReviewDao reviewDao = new ReviewDao();
		Response res = reviewDao.getReviewByAppAndUid(appId, uid, shortKey);
		if (res.getStatus() == 200) {
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"Forbidden, you have already reviewed this application.");
			return "error";
		}
		String res_app = "";
		res = new ApplicationDao().getApp(appId);
		res_app = res.readEntity(String.class);
		// System.out.println(res_job);// test
		JobBean job = null;
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
			if (app.getStatus().equals("in_review")) {
				JobDao jobDao = new JobDao();
				res = jobDao.getJob(app.get_jobId());
				if (res.getStatus() != 200) {
					model.addAttribute("code", res.getStatus());
					model.addAttribute("message",
							res.getStatusInfo().getReasonPhrase());
					return "error";
				}
				String res_job = res.readEntity(String.class);
				JobJSONHandler jobHandler = new JobJSONHandler();
				job = jobHandler.JSONtoPOJO(res_job);
				if (job == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
			} else {
				model.addAttribute("code", 403);
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
		model.addAttribute("job", job);
		return "comment";
	}
}
