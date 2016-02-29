package au.edu.unsw.soacourse.humanresource.dao;

import java.util.ArrayList;

import au.edu.unsw.soacourse.humanresource.model.Application;
import au.edu.unsw.soacourse.humanresource.model.Application.AppStatus;

public enum ApplicationDAO {

	instance;

	private ApplicationXMLHandler handler;

	private ApplicationDAO() {
		handler = new ApplicationXMLHandler();
	}

	public Boolean add(Application application) {
		return handler.addNewItem(application);
	}

	public ArrayList<Application> getAll() {
		return handler.translateToApplications();
	}

	public Application get(String id) {
		for (Application j: handler.translateToApplications()) {
			if (j.get_appId().equals(id))
				return j;
		}

		return null;
	}

	public void update(Application application) {
		handler.updateItem(application);
	}

	public ArrayList<Application> search(String jobId) {
		ArrayList<Application> applications = getAll();
		ArrayList<Application> matchApps = null;

		if (jobId != null && !jobId.equals("")) {
			for (Application a : applications) {
				if (a.get_jobId().equals(jobId)) {
					if (matchApps == null) {
						matchApps = new ArrayList<Application>();
					}
					matchApps.add(a);
				}
			}
		}

		return matchApps;

	}
	
	public void changeStatus(String id, AppStatus status) {
		handler.changeItemStatus(id, status);
	}
	
}
