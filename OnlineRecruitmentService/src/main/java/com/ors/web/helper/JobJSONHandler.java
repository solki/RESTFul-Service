package com.ors.web.helper;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ors.bean.JobBean;

public class JobJSONHandler {

	private JSONParser jobParser;

	public JobJSONHandler() {
		super();
		jobParser = new JSONParser();
	}

	public JobBean JSONtoPOJO(String jsonStr) {

		// System.out.println("i'm in jsonhandler!");
		JobBean job = new JobBean();
		try {
			JSONObject jobObject = (JSONObject) jobParser.parse(jsonStr);
			job.set_jobId(jobObject.get("_jobId").toString());
			job.setClosingDate(jobObject.get("closingDate").toString());
			job.setSalary(jobObject.get("salary").toString());
			job.setPositionType(jobObject.get("positionType").toString());
			job.setLocation(jobObject.get("location").toString());
			job.setDepartment(jobObject.get("department").toString());
			job.setJobDescriptions(jobObject.get("jobDescriptions").toString());
			job.setStatus(jobObject.get("status").toString());

		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return job;

	}

	public ArrayList<JobBean> JSONtoPOJOList(String jsonStr) {

		ArrayList<JobBean> jobs = new ArrayList<JobBean>();
		try {
			JSONArray jobArray = (JSONArray) jobParser.parse(jsonStr);
			for (int i = 0; i < jobArray.size(); ++i) {
				jobs.add(this.JSONtoPOJO(jobArray.get(i).toString()));
			}

		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return jobs;

	}

	public ArrayList<JobBean> JSONtoPOJOListViaDept(String jsonStr,
			String dept) {
		ArrayList<JobBean> orig = this.JSONtoPOJOList(jsonStr);
		ArrayList<JobBean> jobs = new ArrayList<JobBean>();
		for (JobBean job : orig) {
			if (dept.equalsIgnoreCase(job.getDepartment())) {
				jobs.add(job);
			}
		}
		if (jobs.isEmpty()) {
			return null;
		}
		return jobs;
	}

}
