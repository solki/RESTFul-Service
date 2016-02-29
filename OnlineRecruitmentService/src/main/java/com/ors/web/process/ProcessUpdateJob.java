package com.ors.web.process;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.JobBean;
import com.ors.dao.JobDao;
import com.ors.web.helper.JobJSONHandler;

public class ProcessUpdateJob {
	
	private String jobId;
	private ModelMap model;

	
	
	public ProcessUpdateJob(String jobId, ModelMap model) {
		super();
		this.jobId = jobId;
		this.model = model;
	}

	public String execute() {
		
		String res_job = "";
		Response res = new JobDao().getJob(jobId);
		res_job = res.readEntity(String.class);
		System.out.println(res_job);//test
		int responseCode = res.getStatus();
		JobBean job = null;
		if (responseCode == 200) {
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
			model.addAttribute("message", res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		
		model.addAttribute("job", job);
		return "updateJobForm";	
		
	}

	public String getJobId() {
		return jobId;
	}

	public ModelMap getModel() {
		return model;
	}
	

}
