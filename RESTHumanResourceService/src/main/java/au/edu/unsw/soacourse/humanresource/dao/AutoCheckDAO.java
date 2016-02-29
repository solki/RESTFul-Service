package au.edu.unsw.soacourse.humanresource.dao;

import java.util.ArrayList;

import au.edu.unsw.soacourse.humanresource.model.AutoCheck;

public enum AutoCheckDAO {
	instance;

	private AutoCheckXMLHandler handler;

	private AutoCheckDAO() {
		handler = new AutoCheckXMLHandler();

	}

	public Boolean add(AutoCheck autoCheck) {
		return handler.addNewItem(autoCheck);
	}

	public AutoCheck get(String id) {
		for (AutoCheck ac: handler.translateToAutoChecks()) {
			if (ac.get_autoCheckId().equals(id) || ac.get_appId().equals(id))
				return ac;
		}

		return null;
	}
	
	public AutoCheck getAutoCheckByAppID(String id) {
		for (AutoCheck ac: handler.translateToAutoChecks()) {
			if (ac.get_appId().equals(id))
				return ac;
		}

		return null;
	}

	public ArrayList<AutoCheck> getAll() {
		return handler.translateToAutoChecks();
	}
	
	public void update(AutoCheck autoCheck) {
		handler.updateItem(autoCheck);
	}



}
