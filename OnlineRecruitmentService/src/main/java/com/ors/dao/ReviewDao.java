package com.ors.dao;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

public class ReviewDao {

	final private static String REST_URI = "http://localhost:8080/RESTHumanResourceService";
	final private static String ORSKey = "i-am-ors";

	public ReviewDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Response getAllReviews(String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		return res;
	}

	public Response addReview(String appId, String uid, String comments,
			String decision, String shortKey) {

		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews/add").type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("ORSKey", ORSKey).header("ShortKey", shortKey);
		Form form = new Form();
		form.param("Appid", appId);
		form.param("Uid", uid);
		form.param("Comments", comments);
		form.param("Decision", decision);
		Response res = null;
		res = client.post(form);
		return res;

	}

	public Response getReview(String reviewId) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews/" + reviewId).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey);
		Response res = client.get();
		return res;
	}

	public Response getReviewsByApp(String appId, String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews/app/" + appId).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		return res;
	}

	public Response getReviewsViaUid(String uid, String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews/reviewer/" + uid)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		return res;
	}

	public Response getReviewByAppAndUid(String appId, String uid,
			String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews/appandreviewer/" + appId + "/" + uid)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		return res;

	}

	public Response updateStatus(String reviewId, String status,
			String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/reviews/" + reviewId + "/" + status)
				.header("ORSKey", ORSKey).header("ShortKey", shortKey);
		Response res = null;
		res = client.put(null);

		return res;

	}

}
