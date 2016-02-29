package com.ors.dao;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

import com.ors.bean.ApplicationBean;

public class ApplicationDao {
	final private static String REST_URI = "http://localhost:8080/RESTHumanResourceService";
	final private static String ORSKey = "i-am-ors";

	public ApplicationDao() {
		super();
	}

	public Response postApp(String jobId, String licenseNumber, String fullName,
			String postcode, String coverLetter, String briefResume) {

		WebClient client = WebClient.create(REST_URI);
		client.path("/applications/add")
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("ORSKey", ORSKey);
		Form form = new Form();
		form.param("_jobId", jobId);
		form.param("licenseNumber", licenseNumber);
		form.param("fullName", fullName);
		form.param("postcode", postcode);
		form.param("coverLetter", coverLetter);
		form.param("briefResume", briefResume);
		Response res = null;
		res = client.post(form);
		return res;

	}

	public Response updateApp(ApplicationBean app) {

		Response res = this.getApp(app.get_appId());
		if (res.getStatus() != 200) {
			return res;
		}
		String eTag = res.getEntityTag().toString();
		System.out.println("etag is:" + res.getEntityTag().toString());
		WebClient client = WebClient.create(REST_URI);
		client.path("/applications/update").type(MediaType.APPLICATION_XML)
				.header("ORSKey", ORSKey).header("If-None-Match", eTag);
		res = client.put(app);
		return res;

	}

	public Response getApp(String appId) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/applications/" + appId).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey);
		Response res = client.get();

		return res;
	}

	public Response getAppByLink(String linkStr) {
		WebClient client = WebClient.create(linkStr);
		client.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey);
		Response res = client.get();
		return res;
	}

	public Response getAllApps(String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/applications").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		return res;

	}

	public Response deleteApp(String appId) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/applications/delete/" + appId).header("ORSKey", ORSKey);
		Response res = client.delete();
		
		return res;
	}

	public Response updateAppStatus(String appId, String status) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/applications/" + appId + "/" + status).header("ORSKey",
				ORSKey);
		Response res = client.put(null);
		return res;

	}

	public Response getAppsByJob(String jobId, String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/applications/job/" + jobId)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();

		return res;
	}

}
