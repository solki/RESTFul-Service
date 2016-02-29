package com.ors.web.helper;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ors.bean.ReviewBean;

public class ReviewJSONHandler {
	
	String _reviewId, _appId, _uId, comments, decision;
	
	private JSONParser revParser;
	
	public ReviewJSONHandler() {
		super();
		revParser = new JSONParser();
	}
	
	public ReviewBean JSONtoPOJO(String jsonStr) {
		
		ReviewBean rev = new ReviewBean();
		try {
			JSONObject revObject = (JSONObject) revParser.parse(jsonStr);
			rev.set_reviewId(revObject.get("_reviewId").toString());
			rev.set_appId(revObject.get("_appId").toString());
			rev.set_uId(revObject.get("_uId").toString());
			rev.setComments(revObject.get("comments").toString());
			rev.setDecision(revObject.get("decision").toString());
		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return rev;
		
	}
	
	public ArrayList<ReviewBean> JSONtoPOJOList(String jsonStr) {
		
		ArrayList<ReviewBean> revs = new ArrayList<ReviewBean>();
		try {
			JSONArray revArray = (JSONArray) revParser.parse(jsonStr);
			for (int i = 0; i < revArray.size(); ++i) {
				revs.add(this.JSONtoPOJO(revArray.get(i).toString()));
			}
		} catch (ParseException e) {
			System.out.println("position: " + e.getPosition());
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
		return revs;
		
	}


}
