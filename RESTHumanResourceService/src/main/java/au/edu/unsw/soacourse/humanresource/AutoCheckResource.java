package au.edu.unsw.soacourse.humanresource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import javax.xml.bind.JAXBElement;

import au.edu.unsw.soacourse.humanresource.Helper.IdentityCheckHelper;
import au.edu.unsw.soacourse.humanresource.dao.ApplicationDAO;
import au.edu.unsw.soacourse.humanresource.dao.AutoCheckDAO;
import au.edu.unsw.soacourse.humanresource.model.AutoCheck;

@Path("/autochecks")
public class AutoCheckResource {

	@Context
	UriInfo uriInfo;

	@PUT
	@Path("/add")
	@Consumes(MediaType.APPLICATION_XML)
	public Response creatAutoCheckResult(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			JAXBElement<AutoCheck> jaxbAutoCheck) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		AutoCheck autoCheck = jaxbAutoCheck.getValue();

		//////////////////////////////////////////////////////
		if (autoCheck.get_appId() == null || autoCheck.get_appId().equals("") || 
				ApplicationDAO.instance.get(autoCheck.get_appId()) == null) {
			builder =  Response.status(Response.Status.BAD_REQUEST);	
			builder.type("text/html");
			builder.entity("<h3>400 BAD_REQUEST<br>The application id associated with this autocheck is not valid</h3>");
			return builder.build();
		}

		AutoCheck targetAutoCheck = 
				AutoCheckDAO.instance.getAutoCheckByAppID(autoCheck.get_appId());


		if (targetAutoCheck == null) {
			// create it
			autoCheck.set_autoCheckId(UUID.randomUUID().toString().substring(1, 8));
			AutoCheckDAO.instance.add(autoCheck);

			UriBuilder ub = uriInfo.getBaseUriBuilder();
			URI checkUri = ub.path("autochecks/" + autoCheck.get_autoCheckId()).build();
			builder = Response.created(checkUri);
			builder.link(checkUri, "created resource");
			
			ub = uriInfo.getBaseUriBuilder();
			builder.link(
					ub.path("/reviews/add").build(), 
					"Post create review for this application with appid:" + autoCheck.get_appId());


			return builder.build();
		}

		// already exist, update it
		AutoCheckDAO.instance.update(autoCheck);

		builder = Response.ok();

		UriBuilder ub = uriInfo.getBaseUriBuilder();
		URI checkUri = ub.path("autochecks/" + targetAutoCheck.get_autoCheckId()).build();
		builder.link(checkUri, "updated autocheck result URI");
		
		ub = uriInfo.getBaseUriBuilder();
		builder.link(
				ub.path("/reviews/add").build(), 
				"Post create review for this application with appid:" + autoCheck.get_appId());

		return builder.build();	


	}


	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAutoChecks(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {

		// only manager could view all results
		ResponseBuilder builder = IdentityCheckHelper.IsORSManagerOrReviewer(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<AutoCheck> autoChecks = AutoCheckDAO.instance.getAll();

		if (autoChecks == null) {
			builder =  Response.status(Response.Status.NOT_FOUND);	
			builder.type("text/html");
			builder.entity("<h3>404 NOT_FOUND<br>No autoCheck has been created yet</h3>");
			return builder.build();
		}

		GenericEntity<List<AutoCheck>> entity 
		= new GenericEntity<List<AutoCheck>>(autoChecks) {};

		builder = Response.ok(entity);

		for (AutoCheck r : autoChecks) {
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			builder.link(ub.path(r.get_autoCheckId()).build(), "AutoCheck " + r.get_autoCheckId());
		}

		// add other link here
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		builder.link(ub.path("/add").build(), "Put create autocheck result");
		
		return builder.build();

	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAutoCheck(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManagerOrReviewer(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		AutoCheck autoCheck = AutoCheckDAO.instance.get(id);
		if (autoCheck == null) {
			builder =  Response.status(Response.Status.NOT_FOUND);	
			builder.type("text/html");
			builder.entity("<h3>404 NOT_FOUND<br>No autoCheck found with this app/autocheck id in DB</h3>");
			return builder.build();
		}

		builder = Response.ok(autoCheck);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI checkUri = ub.build();
		builder.link(checkUri, "self");

		ub = uriInfo.getBaseUriBuilder();
		builder.link(
				ub.path("/reviews/add").build(), 
				"Post create review for this application with appid:" + autoCheck.get_appId());

		
		return builder.build();

	}

	@GET
	@Path("/app/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAutoCheckByApp(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManagerOrReviewer(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		AutoCheck autoCheck = AutoCheckDAO.instance.getAutoCheckByAppID(id);
		if (autoCheck == null) {
			builder =  Response.status(Response.Status.NOT_FOUND);	
			builder.type("text/html");
			builder.entity("<h3>404 NOT_FOUND<br>No autoCheck found with this app/autocheck id in DB</h3>");
			return builder.build();
		}

		builder = Response.ok(autoCheck);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI checkUri = ub.build();
		builder.link(checkUri, "self");

		return builder.build();

	}





}
