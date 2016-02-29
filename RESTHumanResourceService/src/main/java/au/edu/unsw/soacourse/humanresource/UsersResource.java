package au.edu.unsw.soacourse.humanresource;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.humanresource.Helper.IdentityCheckHelper;
import au.edu.unsw.soacourse.humanresource.dao.UserDAO;
import au.edu.unsw.soacourse.humanresource.model.User;
import au.edu.unsw.soacourse.humanresource.model.User.RoleType;


@Path("/users")
public class UsersResource {

	@Context
	UriInfo uriInfo;

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUsers(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<User> users = UserDAO.instance.getAll();

		GenericEntity<List<User>> entity 
		= new GenericEntity<List<User>>(users) {};

		builder = Response.ok(entity);

		for (User u : users) {
			UriBuilder ub = uriInfo.getAbsolutePathBuilder();
			builder.link(ub.path(u.get_uid()).build(), "user " + u.get_uid());
		}

		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		builder.link(
				ub.path("/user").queryParam("hireteam", "SAMBA").build(), 
				"user in hireteam search");

		return builder.build();


	}	

	@GET
	@Path("{user}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUser(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("user") String id) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		User user = UserDAO.instance.get(id);
		if (user == null) {
			builder =  Response.status(Response.Status.NOT_FOUND);	
			builder.type("text/html");
			builder.entity("<h3>404 NOT_FOUND<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		builder = Response.ok(user);

		UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		builder.link(ub.build(), "self");


		if (user.getRole().equals(RoleType.reviewer)) {
			
			ub = uriInfo.getBaseUriBuilder();
			builder.link(
					ub.path("/reviews/reviewer/" + user.get_uid()).build(), 
					"reviews by reviewer");
			
		}


		return builder.build();


	}

	@GET
	@Path("/user")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUsersInHireTeam(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@QueryParam("hireteam") String hireTeam) {

		ResponseBuilder builder = IdentityCheckHelper.IsORSManager(orsKey,shortKey);
		if (builder != null)
			return builder.build();

		ArrayList<User> users = UserDAO.instance.getUserForHireTeam(hireTeam);
		if (users == null || users.size() == 0) {
			builder = Response.status(Response.Status.NO_CONTENT);
			builder.type("text/html");
			builder.entity("<h3>204 NO_CONTENT<br>Your request has no match in DB</h3>");
			return builder.build();
		}

		GenericEntity<List<User>> entity 
		= new GenericEntity<List<User>>(users) {};

		return Response.ok(entity).build();

	}




}
