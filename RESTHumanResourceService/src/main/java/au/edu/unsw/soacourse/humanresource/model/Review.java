package au.edu.unsw.soacourse.humanresource.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "_reviewId", "_appId", "_uId", "comments", "decision" })
public class Review {

	public enum DecisionType {
		notshortlisted, shortlisted
	}

	private String _reviewId;
	private String _appId;
	private String _uId;
	private String comments;
	private DecisionType decision;

	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Review(String _reviewId, String _appId, String _uId, String comments,
			DecisionType decision) {
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

	public DecisionType getDecision() {
		return decision;
	}

	public void setDecision(DecisionType decision) {
		this.decision = decision;
	}

}
