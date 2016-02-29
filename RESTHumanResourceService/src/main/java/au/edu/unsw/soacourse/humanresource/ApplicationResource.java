package au.edu.unsw.soacourse.humanresource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import au.edu.unsw.soacourse.humanresource.Helper.HashCodeHelper;
import au.edu.unsw.soacourse.humanresource.Helper.IdentityCheckHelper;
import au.edu.unsw.soacourse.humanresource.dao.ApplicationDAO;
import au.edu.unsw.soacourse.humanresource.dao.JobDAO;
import au.edu.unsw.soacourse.humanresource.model.Application;
import au.edu.unsw.soacourse.humanresource.model.Application.AppStatus;

@Path("/applications")
public class ApplicationResource {
	@Context
	UriInfo uriInfo;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/plain")
	public Response addApplication(@HeaderParam("ORSKey") String orsKey,
			@FormParam("_jobId") String _jobId,
			@FormParam("licenseNumber") String licenseNumber,
			@FormParam("fullName") @DefaultValue("") String fullName,
			@FormParam("postcode") @DefaultValue("") String postcode,
			@FormParam("coverLetter") @DefaultValue("") String coverLetter,
			@FormParam("briefResume") @DefaultValue("") String briefResume) {

		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		if (_jobId == null || _jobId.equals("") || 
				JobDAO.instance.get(_jobId) == null) {

			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>The job id provided is not valid</h3>");
			return builder.build();
		}
		
		if (licenseNumber == null || licenseNumber.equals("")) {

			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>You must provide licenseNumber</h3>");
			return builder.build();
		}
		
		
		// here check if application being added
		List<Application> apps = ApplicationDAO.instance.getAll();
		Boolean isExisted = false;
		for (Application a : apps) {

			if (a.getLicenseNumber().equals(licenseNumber) && a.get_jobId().equals(_jobId)) {
				isExisted = true;
				break;
			}			

		}
		if (isExisted) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD REQUEST<br>Your application already existed.</h3>");
			return builder.build();
		}
		
		
		Application application = new Application();

		application.set_appId(UUID.randomUUID().toString().substring(1, 8));
		application.set_jobId(_jobId);
		application.setLicenseNumber(licenseNumber);
		application.setFullName(fullName);
		application.setPostcode(postcode);
		application.setCoverLetter(coverLetter);
		application.setBriefResume(briefResume);

		application.setStatus(AppStatus.created);// modified

		ApplicationDAO.instance.add(application);

		// 200 ok + link
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		URI AppUri = ub.path("applications/" + application.get_appId()).build();
		builder = Response.created(AppUri);
		builder.link(AppUri, "created resource");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/applications/update").build(),
				"update via sending xml");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/autochecks/add").build(),
				"put(xml) to create auto-check result for this application with id:" + application.get_appId());
		
				
		return builder.build();

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllApplications(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {

		ResponseBuilder builder = IdentityCheckHelper
				.IsORSManagerOrReviewer(orsKey, shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<Application> applications = ApplicationDAO.instance.getAll();

		GenericEntity<List<Application>> entity = new GenericEntity<List<Application>>(
				applications) {
		};

		builder = Response.ok(entity);

		for (Application a : applications) {
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			builder.link(ub.path(a.get_appId()).build(),
					"application " + a.get_appId());
		}

		// add other link here
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		builder.link(ub.path("/add").build(), "Post create application");

		return builder.build();

	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getApplication(@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id, @Context Request request) {

		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		Application application = ApplicationDAO.instance.get(id);
		if (application == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		EntityTag etag = new EntityTag(
				HashCodeHelper.ApplicationHashCode(application));
		builder = request.evaluatePreconditions(etag);

		// cached resource did change
		if (builder == null) {
			builder = Response.ok(application);
			builder.tag(etag);

		}

		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI AppUri = ub.build();
		builder.link(AppUri, "self");
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/applications/update").build(),
				"update via sending xml");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/autochecks/add").build(),
				"put(xml) to create auto-check result for this application with id:" + application.get_appId());
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/autochecks/app/" + application.get_appId()).build(),
				"autocheck result for this application");
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/reviews/app/" + application.get_appId()).build(),
				"reviews for this application");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/applications/delete/" + application.get_appId()).build(), 
				"delete (archive) application");
		
		return builder.build();

	}

	@GET
	@Path("/job/{jobId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getApplicationsByJob(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("jobId") String jobId, @Context Request request) {

		ResponseBuilder builder = IdentityCheckHelper
				.IsORSManagerOrReviewer(orsKey, shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<Application> applications = ApplicationDAO.instance
				.search(jobId);
		if (applications == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		GenericEntity<List<Application>> entity = new GenericEntity<List<Application>>(
				applications) {
		};

		return Response.ok(entity).build();

	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateApplicationDetails(
			@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			JAXBElement<Application> jaxbApp, @Context Request request) {

		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		Application application = jaxbApp.getValue();

		Application targetApp = ApplicationDAO.instance
				.get(application.get_appId());
		if (targetApp == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		EntityTag etag = new EntityTag(
				HashCodeHelper.ApplicationHashCode(targetApp));
		builder = request.evaluatePreconditions(etag);

		// client is not up to date (send back 412)
		if (builder == null) {
			builder = Response.status(Response.Status.PRECONDITION_FAILED);
			builder.type("text/html");
			builder.entity(
					"<h3>412 PRECONDITION_FAILED<br>Your copy is not up to date. Update failed. Get a fresh copy and update again.</h3>");
			return builder.build();
		}

		if (!targetApp.getStatus().equals(AppStatus.created)) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>You can not update now</h3>");
			return builder.build();
		}
		
		

		ApplicationDAO.instance.update(application);

		builder = Response.ok();

		UriBuilder ub = uriInfo.getBaseUriBuilder();
		URI AppUri = ub.path("applications/" + application.get_appId()).build();

		builder.link(AppUri, "updated application URI");

		return builder.build();

	}

	@PUT
	@Path("/{id}/{status}")
	public Response changeAppStatus(@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id, @PathParam("status") String status) {

		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		Application application = ApplicationDAO.instance.get(id);
		if (application == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		Boolean isValid = false;
		for (AppStatus a : AppStatus.values()) {
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

		ApplicationDAO.instance.changeStatus(id, AppStatus.valueOf(status));

		return Response.ok().build();

	}

	@DELETE
	@Path("/delete/{id}")
	public Response deleteApplication(@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {

		return changeAppStatus(orsKey, id, AppStatus.archived.toString());

	}

}