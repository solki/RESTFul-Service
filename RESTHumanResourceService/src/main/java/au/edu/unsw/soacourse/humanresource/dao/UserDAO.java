package au.edu.unsw.soacourse.humanresource.dao;

import java.util.ArrayList;

import au.edu.unsw.soacourse.humanresource.model.User;
import au.edu.unsw.soacourse.humanresource.model.User.RoleType;



public enum UserDAO {
	instance;

	private UserXMLHandler handler;
	
	private UserDAO() {
		handler = new UserXMLHandler();
	}

	public ArrayList<User> getAll() {
		return handler.translateToUser();
	}

	public User get(String id) {
		for (User u: handler.translateToUser()) {
			if (u.get_uid().equals(id))
				return u;
		}
		
		return null;
	}

	public ArrayList<User> getUserForHireTeam(String hireTeam) {
		ArrayList<User> users = new ArrayList<User>();
		for (User u: handler.translateToUser()) {
			if (u.getRole().equals(RoleType.reviewer) &&
					u.getDepartment().contains(hireTeam))
				users.add(u);
		}
		
		return users;
	}
	
	
	
	
}
