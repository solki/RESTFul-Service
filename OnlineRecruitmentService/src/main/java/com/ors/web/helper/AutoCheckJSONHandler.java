package com.ors.web.helper;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ors.bean.AutoCheckBean;

public class AutoCheckJSONHandler {

	private JSONParser acParser;

	public AutoCheckJSONHandler() {
		super();
		acParser = new JSONParser();
	}

	public AutoCheckBean JSONtoPOJO(String jsonStr) {

		System.out.println("i'm in autocheck jsonhandler!");
		AutoCheckBean ac = new AutoCheckBean();
		try {
			JSONObject acObject = (JSONObject) acParser.parse(jsonStr);
			ac.set_appId(acObject.get("_appId").toString());
			ac.set_autoCheckId(acObject.get("_autoCheckId").toString());
			ac.setResultDetails(acObject.get("resultDetails").toString());

		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return ac;

	}

	public ArrayList<AutoCheckBean> JSONtoPOJOList(String jsonStr) {

		ArrayList<AutoCheckBean> acs = new ArrayList<AutoCheckBean>();
		try {
			JSONArray acArray = (JSONArray) acParser.parse(jsonStr);
			for (int i = 0; i < acArray.size(); ++i) {
				acs.add(this.JSONtoPOJO(acArray.get(i).toString()));
			}

		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return acs;

	}

}
