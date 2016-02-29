package com.ors.web.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.JobBean;
import com.ors.dao.JobDao;
import com.ors.web.helper.JobJSONHandler;

public class ProcessUpdateJobConfirm {

	private String jobId, closingDate, jobDescriptions, location, positionType,
			department, salary;
	private String shortKey;
	private ModelMap model;

	public ProcessUpdateJobConfirm(String jobId, String closingDate,
			String jobDescriptions, String location, String positionType,
			String department, String salary, ModelMap model, String shortKey) {
		this.jobId = jobId;
		this.closingDate = closingDate;
		this.jobDescriptions = jobDescriptions;
		this.location = location;
		this.positionType = positionType;
		this.department = department;
		this.salary = salary;
		this.model = model;
		this.shortKey = shortKey;

	}

	public String execute() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		if (closingDate.equals("")) {

			Date now = new Date();
			closingDate = sdf.format(now.getTime() + 10 * 24 * 3600 * 1000);
		}
		// else {
		System.out.println(closingDate);
		if (!closingDate.matches("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$")) {
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
		if (department.equals("")) {
			model.addAttribute("code", "400");
			model.addAttribute("message",
					"Bad Request, Department is missing.");
			return "error";
		}
		if (!salary.matches("^[0-9]+$")) {
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"Forbidden, salary must be in numbers.");
			return "error";
		}
		if (jobDescriptions.equals(""))
			jobDescriptions = "No Description";
		if (salary.equals("0"))
			salary = "Negotiable";

		String returnData = "updateSuccess";
		JobBean job = new JobBean();
		job.set_jobId(jobId);
		job.setClosingDate(closingDate);
		job.setJobDescriptions(jobDescriptions);
		job.setLocation(location);
		job.setPositionType(positionType);
		job.setDepartment(department);
		job.setSalary(salary);
		job.setStatus("created");
		JobDao jobDao = new JobDao();

		Response res = null;
		res = jobDao.updateJob(job, shortKey);
		if (res.getStatus() != 201 && res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}
		MultivaluedMap<String, Object> headers = res.getHeaders();
		Object linkStr = headers.get("Link").get(0);
		// System.out.println(linkStr.toString());
		String[] link = linkStr.toString().split(";");
		link[0] = link[0].replaceAll("[<>]", "");
		res = jobDao.getJobByLink(link[0], shortKey);
		if (res.getStatus() != 200) {
			model.addAttribute("code", res.getStatus());
			model.addAttribute("message",
					res.getStatusInfo().getReasonPhrase());
			return "error";
		}

		String res_job = "";
		res_job = res.readEntity(String.class);
		JobJSONHandler handler = new JobJSONHandler();
		job = handler.JSONtoPOJO(res_job);
		if (job == null) {
			model.addAttribute("code", "500");
			model.addAttribute("message", "Internal Server Error");
			return "error";
		}
		model.addAttribute("job", job);
		return returnData;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}

	public String getJobDescriptions() {
		return jobDescriptions;
	}

	public void setJobDescriptions(String jobDescriptions) {
		this.jobDescriptions = jobDescriptions;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getShortKey() {
		return shortKey;
	}

	public void setShortKey(String shortKey) {
		this.shortKey = shortKey;
	}

	public ModelMap getModel() {
		return model;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

}
