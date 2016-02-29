package com.ors.web.process;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.dao.JobDao;

public class ProcessPostJob {

	private String closingDate, jobDescriptions, location, positionType, department, salary;
	private String shortKey;
	private ModelMap model;

	public ProcessPostJob(ModelMap model, String closingDate,
			String jobDescriptions, String location, String positionType, String department,
			String salary, String shortKey) {
		super();
		this.model = model;
		this.closingDate = closingDate;
		this.jobDescriptions = jobDescriptions;
		this.location = location;
		this.positionType = positionType;
		this.department = department;
		this.salary = salary;
		this.shortKey = shortKey;

	}

	public String execute() throws IOException {

		String returnData = "postjobsuccess";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		if (closingDate.equals("")) {

			Date now = new Date();
			closingDate = sdf.format(now.getTime() + 10 * 24 * 3600 * 1000);
		}
		// else {
		System.out.println(closingDate);
		if (!closingDate
				.matches("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$")) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "Forbidden, date format is wrong.");
			return "error";
		}
		try {
			sdf.parse(closingDate);
		} catch (ParseException e) {

			e.printStackTrace();
			model.addAttribute("code", "403");
			model.addAttribute("message", "Forbidden, date format is wrong.");
			return "error";
		}
		//System.out.println(closingDate);
		if (department.equals("")) {
			model.addAttribute("code", "400");
			model.addAttribute("message", "Bad Request, Department is missing.");
			return "error";
		}
		if (!salary.matches("^[0-9]+$")) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "Forbidden, salary must be in numbers.");
			return "error";
		}
		if (jobDescriptions.equals(""))
			jobDescriptions = "No Description";
		if (salary.equals("0"))
			salary = "Negotiable";

		JobDao postJob = new JobDao();
		Response res = postJob.postJob(closingDate, jobDescriptions, location,
				positionType, department, salary, shortKey);
		if (res.getStatus() != 201) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			returnData = "error";
		}
		return returnData;

	}

	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}

	public void setJobDescriptions(String jobDescriptions) {
		this.jobDescriptions = jobDescriptions;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

}
