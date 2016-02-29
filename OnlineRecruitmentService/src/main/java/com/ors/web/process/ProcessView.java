package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.JobBean;
import com.ors.dao.JobDao;
import com.ors.web.helper.JobJSONHandler;

public class ProcessView {

	private String jobId;
	private ModelMap model;

	public ProcessView(ModelMap model, String jobId) {
		super();
		this.model = model;
		this.jobId = jobId;
	}

	public String execute() {

		String res_job = "";
		Response res = new JobDao().getJob(jobId);
		res_job = res.readEntity(String.class);
		int responseCode = res.getStatus();
		JobBean job = null;
		if (responseCode == 200) {
			System.out.println("i'm in view!");
			JobJSONHandler handler = new JobJSONHandler();
			job = handler.JSONtoPOJO(res_job);
			if (job == null) {
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

		model.addAttribute("job", job);
		return "quickView";

	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

}
