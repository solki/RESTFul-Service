package com.ors.web.process;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.JobBean;
import com.ors.dao.JobDao;
import com.ors.web.helper.JobJSONHandler;

public class ProcessDeleteJob {

	private String jobId;
	private ModelMap model;
	private String shortKey;

	public ProcessDeleteJob(ModelMap model, String jobId, String shortKey) {
		super();
		this.model = model;
		this.jobId = jobId;
		this.shortKey = shortKey;
	}

	public String execute() {
		String returnData = "welcomeMng";
		JobDao jobDao = new JobDao();
		Response res = jobDao.deleteJob(jobId, shortKey);
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
		} else {
			res = jobDao.getAllJobs(shortKey);
			String r = "";
			int responseCode = res.getStatus();
			r = res.readEntity(String.class);
			if (responseCode != 200) {
				System.out.println(responseCode);
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			ArrayList<JobBean> jobs = null;
			if (!r.equals("[]")) {
				JobJSONHandler handler = new JobJSONHandler();

				jobs = handler.JSONtoPOJOList(r);
				if (jobs == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
			}

			model.addAttribute("jobs", jobs);
			return returnData;
		}
	}

}
