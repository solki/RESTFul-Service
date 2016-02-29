package com.ors.dao;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

import com.ors.bean.JobBean;

public class JobDao {

	final private static String REST_URI = "http://localhost:8080/RESTHumanResourceService";
	final private static String ORSKey = "i-am-ors";

	public JobDao() {
		super();

	}

	public Response postJob(String closingDate, String jobDescriptions,
			String location, String positionType, String department, String salary,
			String shortKey) {

		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs/add").type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("ORSKey", ORSKey).header("ShortKey", shortKey);
		Form form = new Form();
		form.param("ClosingDate", closingDate);
		form.param("JobDescriptions", jobDescriptions);
		form.param("Location", location);
		form.param("PositionType", positionType);
		form.param("Department", department);
		form.param("Salary", salary);
		Response res = null;
		res = client.post(form);
		return res;
	}

	public Response updateJob(JobBean job, String shortKey) {

		Response res = this.getJob(job.get_jobId());
		if (res.getStatus() != 200) {
			return res;
		}
		String eTag = res.getEntityTag().toString();
		System.out.println("etag is:" + res.getEntityTag().toString());
		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs/update").type(MediaType.APPLICATION_XML)
				.header("ORSKey", ORSKey).header("ShortKey", shortKey)
				.header("If-None-Match", eTag);
		res = client.put(job);
		return res;

	}

	public Response updateJobStatus(String jobId, String newStatus) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs/" + jobId + "/" + newStatus).header("ORSKey",
				ORSKey);
		Response res = null;
		res = client.put(null);

		return res;
	}

	public Response getJob(String jobId) {
		// System.out.println(jobId);
		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs/" + jobId).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey);
		Response res = client.get();

		return res;

	}

	public Response getJobByLink(String linkStr, String shortKey) {
		WebClient client = WebClient.create(linkStr);
		client.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey);
		Response res = client.get();

		return res;
	}

	public Response getAllJobs(String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		return res;

	}

	public Response searchJob(String keyWord, String positionType,
			String location) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs/job").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.query("description", keyWord).query("location", location)
				.query("position", positionType);
		Response res = client.get();
		System.out.println(res.getStatus() + " "
				+ res.getStatusInfo().getReasonPhrase() + " from jobDAO"); // test
		return res;

	}

	public Response deleteJob(String jobId, String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/jobs/delete/" + jobId).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = null;
		res = client.delete();
		return res;
	}

}
