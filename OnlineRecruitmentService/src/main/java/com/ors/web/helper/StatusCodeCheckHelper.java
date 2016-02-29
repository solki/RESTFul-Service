package com.ors.web.helper;

import org.springframework.ui.ModelMap;

public class StatusCodeCheckHelper {

	final private static String OK = "Ok";
	final private static String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
	final private static String NOT_FOUND = "NOT FOUND";
	final private static String PRECONDITION_FAILED = "PRECONDITION FAILED";
	final private static String UNAUTHORIZED = "UNAUTHORIZED";
	final private static String BAD_REQUEST = "BAD REQUEST"; // job status check
	final private static String FORBIDDEN = "FORBIDDEN";
	final private static String CREATED = "CREATED";
	final private static String NO_CONTENT = "NO_CONTENT";

	private String msg;
	private ModelMap model;

	public StatusCodeCheckHelper() {
		super();
	}

	public int check() {
		if (msg.contains(OK.toLowerCase())) {
			model.addAttribute("code", "200");
			model.addAttribute("message", OK);
			return 200;
		} else if (msg.contains(CREATED.toLowerCase())) {
			model.addAttribute("code", "201");
			model.addAttribute("message", CREATED);
			return 201;
		} else if (msg.contains(NO_CONTENT.toLowerCase())) {
			model.addAttribute("code", "204");
			model.addAttribute("message", NO_CONTENT);
			return 204;
		} else if (msg.contains(PRECONDITION_FAILED.toLowerCase())) {
			model.addAttribute("code", "412");
			model.addAttribute("message", PRECONDITION_FAILED);
			return 412;
		} else if (msg.contains(UNAUTHORIZED.toLowerCase())) {
			model.addAttribute("code", "401");
			model.addAttribute("message", UNAUTHORIZED);
			return 401;
		} else if (msg.contains(BAD_REQUEST.toLowerCase())) {
			model.addAttribute("code", "400");
			model.addAttribute("message", BAD_REQUEST);
			return 400;
		} else if (msg.contains(NOT_FOUND.toLowerCase())) {
			model.addAttribute("code", "404");
			model.addAttribute("message", NOT_FOUND);
			return 404;
		} else if (msg.contains(FORBIDDEN.toLowerCase())) {
			model.addAttribute("code", "403");
			model.addAttribute("message", FORBIDDEN);
			return 403;
		} else {
			model.addAttribute("code", "500");
			model.addAttribute("message", INTERNAL_SERVER_ERROR);
			return 500;
		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg.toLowerCase();
	}

	public ModelMap getModel() {
		return model;
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

}
