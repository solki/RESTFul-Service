package au.edu.unsw.soacourse.humanresource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.humanresource.Helper.IdentityCheckHelper;
import au.edu.unsw.soacourse.humanresource.dao.ApplicationDAO;
import au.edu.unsw.soacourse.humanresource.dao.ReviewDAO;
import au.edu.unsw.soacourse.humanresource.dao.UserDAO;
import au.edu.unsw.soacourse.humanresource.model.Review;
import au.edu.unsw.soacourse.humanresource.model.Review.DecisionType;
import au.edu.unsw.soacourse.humanresource.model.User.RoleType;

@Path("/reviews")
public class ReviewResource {

	@Context
	UriInfo uriInfo;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addReviewPosting(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@FormParam("Appid") String appid, @FormParam("Uid") String uid,
			@FormParam("Comments") @DefaultValue("") String comments,
			@FormParam("Decision") String decision) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSReviewer(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();

		//////////////////////////////////////////////

		if (appid == null || appid.equals("")
				|| ApplicationDAO.instance.get(appid) == null) {

			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>The application id provided is not valid</h3>");
			return builder.build();
		}

		if (uid == null || 
				uid.equals("") || 
				UserDAO.instance.get(uid) == null ||
				!UserDAO.instance.get(uid).getRole().equals(RoleType.reviewer)) {

			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>The reivewer id provided is not valid</h3>");
			return builder.build();
		}

		
		Boolean isValid = false;
		for (DecisionType d : DecisionType.values()) {
			if (d.toString().equals(decision))
				isValid = true;
		}
		if (!isValid) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>The decision type you provided is not valid</h3>");
			return builder.build();
		}

		// here check if application being added
		List<Review> reviews = ReviewDAO.instance.getAll();
		Boolean isExisted = false;
		for (Review r : reviews) {

			if (r.get_appId().equals(appid) && r.get_uId().equals(uid)) {
				isExisted = true;
				break;
			}			

		}
		if (isExisted) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD REQUEST<br>Your review already existed.</h3>");
			return builder.build();
		}


		Review review = new Review();

		review.set_reviewId(UUID.randomUUID().toString().substring(1, 8));
		review.set_appId(appid);
		review.set_uId(uid);
		review.setComments(comments);
		review.setDecision(DecisionType.valueOf(decision));

		ReviewDAO.instance.add(review);

		// 200 ok + link
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		URI reviewUri = ub.path("reviews/" + review.get_reviewId()).build();
		builder = Response.created(reviewUri);
		builder.link(reviewUri, "created resource");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/reviews/" + review.get_reviewId() + "/rejected").build(),
				"change the decision of this review");
		
		return builder.build();

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getReviews(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {
		ResponseBuilder builder = IdentityCheckHelper
				.IsORSManagerOrReviewer(orsKey, shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<Review> reviews = ReviewDAO.instance.getAll();

		if (reviews == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>No review has been created yet</h3>");
			return builder.build();
		}

		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(
				reviews) {
		};

		builder = Response.ok(entity);

		for (Review r : reviews) {
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			builder.link(ub.path(r.get_reviewId()).build(),
					"Review " + r.get_reviewId());
		}

		// add other link here

		return builder.build();

	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getReview(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManagerOrReviewer(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();

		Review review = ReviewDAO.instance.get(id);
		if (review == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>No review found with this id in DB</h3>");
			return builder.build();
		}

		builder = Response.ok(review);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI reviewUri = ub.build();
		builder.link(reviewUri, "self");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/reviews/" + review.get_reviewId() + "/rejected").build(),
				"change the decision of this review");
		
		
		return builder.build();

	}

	@GET
	@Path("/app/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getReviewsPerApp(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {

		ResponseBuilder builder = IdentityCheckHelper
				.IsORSManagerOrReviewer(orsKey, shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<Review> reviews = ReviewDAO.instance.getPerApp(id);

		if (reviews == null || reviews.size() == 0) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>No review found for this application</h3>");
			return builder.build();
		}

		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(
				reviews) {
		};

		builder = Response.ok(entity);

		return builder.build();
	}

	@GET
	@Path("/reviewer/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getReviewsPerReviewer(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManagerOrReviewer(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<Review> reviews = ReviewDAO.instance.getPerReviewer(id);

		if (reviews == null || reviews.size() == 0) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>No reviews found for this reviewer</h3>");
			return builder.build();
		}

		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(
				reviews) {
		};

		builder = Response.ok(entity);

		return builder.build();

	}

	@GET
	@Path("/appandreviewer/{appId}/{uid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getReviewByReviewerAndApp(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("uid") String uid, @PathParam("appId") String appId) {

		ResponseBuilder builder = IdentityCheckHelper
				.IsORSManagerOrReviewer(orsKey, shortKey);
		if (builder != null)
			return builder.build();

		Review review = ReviewDAO.instance.getByReviewerAndApp(uid, appId);

		if (review == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>No review found with this id in DB</h3>");
			return builder.build();
		}

		builder = Response.ok(review);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI reviewUri = ub.build();
		builder.link(reviewUri, "self");

		return builder.build();

	}

	@PUT
	@Path("/{id}/{status}")
	public Response changeAppStatus(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id, @PathParam("status") String status) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSReviewer(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();

		Review review = ReviewDAO.instance.get(id);
		if (review == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		Boolean isValid = false;
		for (DecisionType a : DecisionType.values()) {
			if (status.equals(a.toString()))
				isValid = true;
		}
		if (!isValid) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>The status you provided is not valid</h3>");
			return builder.build();
		}

		ReviewDAO.instance.changeStatus(id, DecisionType.valueOf(status));

		return Response.ok().build();

	}

}
