package com.ors.bean;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "_reviewId", "_appId", "_uId", "comments", "decision" })
public class ReviewBean {
	private String _reviewId, _appId, _uId, comments, decision;

	public ReviewBean() {
		super();
	}

	public ReviewBean(String _reviewId, String _appId, String _uId,
			String comments, String decision) {
		super();
		this._reviewId = _reviewId;
		this._appId = _appId;
		this._uId = _uId;
		this.comments = comments;
		this.decision = decision;
	}

	public String get_reviewId() {
		return _reviewId;
	}

	public void set_reviewId(String _reviewId) {
		this._reviewId = _reviewId;
	}

	public String get_appId() {
		return _appId;
	}

	public void set_appId(String _appId) {
		this._appId = _appId;
	}

	public String get_uId() {
		return _uId;
	}

	public void set_uId(String _uId) {
		this._uId = _uId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

}
