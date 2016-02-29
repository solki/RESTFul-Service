package au.edu.unsw.soacourse.humanresource.dao;

import java.util.ArrayList;

import au.edu.unsw.soacourse.humanresource.model.Review;
import au.edu.unsw.soacourse.humanresource.model.Review.DecisionType;

public enum ReviewDAO {
	instance;

	private ReviewXMLHandler handler;

	private ReviewDAO() {
		handler = new ReviewXMLHandler();

	}

	public Boolean add(Review review) {
		return handler.addNewItem(review);
	}

	public ArrayList<Review> getAll() {
		return handler.translateToReviews();
	}

	public Review get(String id) {
		for (Review r : handler.translateToReviews()) {
			if (r.get_reviewId().equals(id))
				return r;
		}

		return null;
	}

	public ArrayList<Review> getPerApp(String id) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		for (Review r : handler.translateToReviews()) {
			if (r.get_appId().equals(id))
				reviews.add(r);
		}

		return reviews;
	}

	public ArrayList<Review> getPerReviewer(String id) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		for (Review r : handler.translateToReviews()) {
			if (r.get_uId().equals(id))
				reviews.add(r);
		}

		return reviews;
	}

	public Review getByReviewerAndApp(String uid, String appId) {
		for (Review r : this.getPerApp(appId)) {
			if (r.get_uId().equals(uid))
				return r;
		}
		return null;
	}

	public void changeStatus(String id, DecisionType status) {

		handler.changeItemStatus(id, status);
	}

}
