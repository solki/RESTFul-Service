package com.ors.web.helper;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ors.bean.ApplicationBean;

public class AppJSONHandler {

	private JSONParser appParser;

	public AppJSONHandler() {
		super();
		appParser = new JSONParser();
	}

	public ApplicationBean JSONtoPOJO(String jsonStr) {

		// System.out.println("i'm in jsonhandler!");
		ApplicationBean app = new ApplicationBean();
		try {
			JSONObject appObject = (JSONObject) appParser.parse(jsonStr);
			app.set_appId(appObject.get("_appId").toString());
			app.set_jobId(appObject.get("_jobId").toString());
			app.setLicenseNumber(appObject.get("licenseNumber").toString());
			app.setFullName(appObject.get("fullName").toString());
			app.setPostcode(appObject.get("postcode").toString());
			app.setBriefResume(appObject.get("briefResume").toString());
			app.setCoverLetter(appObject.get("coverLetter").toString());
			app.setStatus(appObject.get("status").toString());

		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return app;

	}

	public ArrayList<ApplicationBean> JSONtoPOJOList(String jsonStr) {

		ArrayList<ApplicationBean> apps = new ArrayList<ApplicationBean>();
		try {
			JSONArray appArray = (JSONArray) appParser.parse(jsonStr);
			for (int i = 0; i < appArray.size(); ++i) {
				apps.add(this.JSONtoPOJO(appArray.get(i).toString()));
			}

		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return apps;

	}

	public ArrayList<ApplicationBean> JSONtoPOJOListViaStatus(String jsonStr,
			ArrayList<String> status) {
		ArrayList<ApplicationBean> origList = this.JSONtoPOJOList(jsonStr);
		ArrayList<ApplicationBean> apps = new ArrayList<ApplicationBean>();
		for (ApplicationBean app : origList) {
			for (String st : status) {
				if (app.getStatus().equals(st)) {
					apps.add(app);
				}
			}
		}
		if (apps.isEmpty()) {
			return null;
		}
		return apps;
	}

}
