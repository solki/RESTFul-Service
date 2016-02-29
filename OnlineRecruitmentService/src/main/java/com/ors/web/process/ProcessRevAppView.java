package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ReviewBean;
import com.ors.dao.ReviewDao;
import com.ors.web.helper.ReviewJSONHandler;

public class ProcessRevAppView {

	private ModelMap model;
	private String appId, uid, shortKey;

	public ProcessRevAppView(ModelMap model, String appId, String uid,
			String shortKey) {
		this.model = model;
		this.appId = appId;
		this.uid = uid;
		this.shortKey = shortKey;
	}

	public String execute() {

		ReviewDao reviewDao = new ReviewDao();
		Response res = reviewDao.getReviewByAppAndUid(appId, uid, shortKey);
		if (res.getStatus() != 200 && res.getStatus() != 404) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		if (res.getStatus() == 404) {
			return "appQuickView";
		}
		ReviewJSONHandler handler = new ReviewJSONHandler();
		ReviewBean rev = handler.JSONtoPOJO(res.readEntity(String.class));
		if (rev == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "INTERNAL_SERVER_ERROR");
			return "error";
		}
		model.addAttribute("rev", rev);
		return "appQuickView";
	}

}
