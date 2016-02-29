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
import javax.ws.rs.QueryParam;
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
import au.edu.unsw.soacourse.humanresource.dao.JobDAO;
import au.edu.unsw.soacourse.humanresource.model.Job;
import au.edu.unsw.soacourse.humanresource.model.Job.JobStatus;

@Path("/jobs")
public class JobsResource {
	@Context
	UriInfo uriInfo;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/plain")
	public Response addJobPosting(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@FormParam("ClosingDate") @DefaultValue("00-00-0000") String closingDate,
			@FormParam("JobDescriptions") @DefaultValue("") String jobDescriptions,
			@FormParam("Location") @DefaultValue("") String location,
			@FormParam("PositionType") @DefaultValue("") String positionType,
			@FormParam("Department") @DefaultValue("") String department,
			@FormParam("Salary") @DefaultValue("") String salary) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();

		// here check if job being added
		List<Job> jobs = JobDAO.instance.getAll();
		Boolean isExisted = false;
		for (Job j : jobs) {

			if (j.getClosingDate().equals(closingDate) &&
					j.getJobDescriptions().equals(jobDescriptions) &&
					j.getLocation().equals(location) &&
					j.getPositionType().equals(positionType) &&
					j.getSalary().equals(salary) &&
					j.getDepartment().equals(department)) {

				isExisted = true;
				break;
			}			

		}

		if (isExisted) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD REQUEST<br>Your job posting already existed.</h3>");
			return builder.build();
		}


		Job job = new Job();

		job.set_jobId(UUID.randomUUID().toString().substring(1, 8));

		job.setClosingDate(closingDate);
		job.setJobDescriptions(jobDescriptions);
		job.setLocation(location);
		job.setPositionType(positionType);
		job.setDepartment(department);
		job.setSalary(salary);

		job.setStatus(Job.JobStatus.created);

		JobDAO.instance.add(job);

		// 200 ok + link
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		URI JobUri = ub.path("jobs/" + job.get_jobId()).build();
		builder = Response.created(JobUri);
		builder.link(JobUri, "created resource");

		// link to itself - add another resource
		ub = uriInfo.getAbsolutePathBuilder();
		JobUri = ub.build();
		builder.link(JobUri, "self - add another one");

		// link to all jobs
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/jobs").build(), "all jobs");

		// link to modify created resource
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/jobs/update").build(), "update via sending xml");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(
				ub.path("/applications/add").build(), 
				"Post to apply this job with id:" + job.get_jobId());


		return builder.build();

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getJobPostings(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();
		// ResponseBuilder builder = null;
		ArrayList<Job> jobs = JobDAO.instance.getAll();

		GenericEntity<List<Job>> entity = new GenericEntity<List<Job>>(jobs) {
		};

		builder = Response.ok(entity);

		for (Job j : jobs) {
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			builder.link(ub.path(j.get_jobId()).build(),
					"jobPosting " + j.get_jobId());
		}

		// add other link here
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		builder.link(ub.path("/add").build(), "Post create jobPosting");

		ub = uriInfo.getAbsolutePathBuilder();
		builder.link(
				ub.path("/job").
				queryParam("description", "").
				queryParam("location", "").
				queryParam("position", "").build(), 
				"job search");
		
		return builder.build();

	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getJobPosting(@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id, @Context Request request) {

		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		Job job = JobDAO.instance.get(id);
		if (job == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		EntityTag etag = new EntityTag(HashCodeHelper.JobHashCode(job));
		builder = request.evaluatePreconditions(etag);

		// cached resource did change
		if (builder == null) {
			builder = Response.ok(job);
			builder.tag(etag);

		}

		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI jobUri = ub.build();
		builder.link(jobUri, "self");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/jobs/update").build(), "update via sending xml");
		
		ub = uriInfo.getAbsolutePathBuilder();
		builder.link(ub.path("/fixed").build(), "put update status");
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/applications/job/" + job.get_jobId()).build(), 
				"applications for this job");
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(
				ub.path("/applications/add").build(), 
				"Post to apply this job with id:" + job.get_jobId());
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(ub.path("/jobs/delete/" + job.get_jobId()).build(), 
				"delete (archive) job");
		
		
		return builder.build();

	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateJobPostingDetails(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey, JAXBElement<Job> jaxbJob,
			@Context Request request) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();

		Job job = jaxbJob.getValue();
		Job targetJob = JobDAO.instance.get(job.get_jobId());
		if (targetJob == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		EntityTag etag = new EntityTag(HashCodeHelper.JobHashCode(targetJob));
		builder = request.evaluatePreconditions(etag);

		// client is not up to date (send back 412)
		if (builder == null) {
			builder = Response.status(Response.Status.PRECONDITION_FAILED);
			builder.type("text/html");
			builder.entity(
					"<h3>412 PRECONDITION_FAILED<br>Your copy is not up to date. Update failed. Get a fresh copy and update again.</h3>");
			return builder.build();
		}

		if (!targetJob.getStatus().equals(JobStatus.created)) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>You can not update now</h3>");
			return builder.build();
		}
	

		JobDAO.instance.update(job);

		builder = Response.ok();

		UriBuilder ub = uriInfo.getBaseUriBuilder();
		URI JobUri = ub.path("jobs/" + job.get_jobId()).build();

		builder.link(JobUri, "updated job URI");

		return builder.build();

	}

	@PUT
	@Path("/{id}/{status}")
	public Response changeJobStatus(@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id, @PathParam("status") String status) {

		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		Job job = JobDAO.instance.get(id);
		if (job == null) {
			builder = Response.status(Response.Status.NOT_FOUND);
			builder.type("text/html");
			builder.entity(
					"<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		Boolean isValid = false;

		for (JobStatus j : JobStatus.values()) {
			if (status.equals(j.toString()))
				isValid = true;
		}
		if (!isValid) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type("text/html");
			builder.entity(
					"<h3>400 BAD_REQUEST<br>The status you provided is not valid</h3>");
			return builder.build();
		}

		JobDAO.instance.changeStatus(id, JobStatus.valueOf(status));

		return Response.ok().build();

	}

	@GET
	@Path("/job")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response searchJobPostings(@HeaderParam("ORSKey") String orsKey,
			// @HeaderParam("ShortKey") String shortKey,
			@QueryParam("description") String description,
			@QueryParam("location") String location,
			@QueryParam("position") String position) {

		// everyone in ORS can perform search
		// System.out.println("I m in "+ description+location+position);
		ResponseBuilder builder = IdentityCheckHelper.IsORS(orsKey);
		if (builder != null)
			return builder.build();

		ArrayList<Job> jobs = JobDAO.instance.search(description, location,
				position);

		if (jobs == null || jobs.size() == 0) {
			builder = Response.status(Response.Status.NO_CONTENT);
			builder.type("text/html");
			builder.entity(
					"<h3>204 NO_CONTENT<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		GenericEntity<List<Job>> entity = new GenericEntity<List<Job>>(jobs) {
		};

		return Response.ok(entity).build();

	}

	@DELETE
	@Path("/delete/{id}")
	public Response deleteJobPosting(@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {
		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,
				shortKey);
		if (builder != null)
			return builder.build();
		// modified
		return changeJobStatus(orsKey, id, JobStatus.archived.toString());

	}

}
