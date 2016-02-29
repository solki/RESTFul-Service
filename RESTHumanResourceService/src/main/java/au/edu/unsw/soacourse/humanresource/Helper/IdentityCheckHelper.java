package au.edu.unsw.soacourse.humanresource.Helper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class IdentityCheckHelper {
	
	public static ResponseBuilder IsORSManager(String orsKey, String shortKey) {
		ResponseBuilder builder = IsORS(orsKey);
		if (builder != null)
			return builder;
		
		if (shortKey == null || !shortKey.contains("manager")) {
			builder = Response.status(Response.Status.FORBIDDEN);
			builder.type("text/html");
			builder.entity("<h3>403 FORBIDDEN<br>You are not manager<br>Set ShortKey in header to indicate who you are</h3>");
			return builder;

		}
		return null;
		
	}
	
	
	public static ResponseBuilder IsORSReviewer(String orsKey, String shortKey) {
		ResponseBuilder builder = IsORS(orsKey);
		if (builder != null)
			return builder;
		
		if (shortKey == null || !shortKey.contains("reviewer")) {
			builder = Response.status(Response.Status.FORBIDDEN);
			builder.type("text/html");
			builder.entity("<h3>403 FORBIDDEN<br>You are not reviewer<br>Set ShortKey in header to indicate who you are</h3>");
			return builder;

		}
		return null;
		
	}
	
	public static ResponseBuilder IsORSManagerOrReviewer(String orsKey, String shortKey) {
		ResponseBuilder builder = IsORS(orsKey);
		if (builder != null)
			return builder;
		
		if (shortKey == null || (!shortKey.contains("manager") && !shortKey.contains("reviewer"))) {
			builder = Response.status(Response.Status.FORBIDDEN);
			builder.type("text/html");
			builder.entity("<h3>403 FORBIDDEN<br>You are neither reviewer nor manager<br>Set ShortKey in header to indicate who you are</h3>");
			return builder;

		}
		return null;
		
	}
	
	public static ResponseBuilder IsORS(String orsKey) {
		if (orsKey == null || !orsKey.equals("i-am-ors")) {
			ResponseBuilder builder = Response.status(Response.Status.UNAUTHORIZED);
			builder.type("text/html");
			builder.entity("<h3>401 UNAUTHORIZED<br>You are not ORS<br>Set ORSKey in header to indicate who you are</h3>");
			return builder;

		}
		return null;
		
	}
	
}
