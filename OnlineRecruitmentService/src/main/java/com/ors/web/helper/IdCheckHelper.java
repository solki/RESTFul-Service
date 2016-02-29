package com.ors.web.helper;

import com.ors.bean.UserBean;

public class IdCheckHelper {

	private UserBean user;

	public IdCheckHelper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IdCheckHelper(UserBean user) {
		super();
		this.user = user;
	}

	public Boolean isUser() {

		if (user == null) {
			return false;
		}
		return true;
	}

	public Boolean isManager() {

		if (!isUser()) {
			return false;
		}
		if (!user.getShortKey().contains("manager")
				|| !user.getRole().equals("manager")) {
			return false;
		}
		return true;
	}

	public Boolean isReviewer() {

		if (!isUser()) {
			return false;
		}
		if (!user.getShortKey().contains("reviewer")
				|| !user.getRole().equals("reviewer")) {
			return false;
		}
		return true;

	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

}
