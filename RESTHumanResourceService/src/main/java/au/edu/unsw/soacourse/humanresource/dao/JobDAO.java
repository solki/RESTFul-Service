package au.edu.unsw.soacourse.humanresource.dao;

import java.util.ArrayList;

import au.edu.unsw.soacourse.humanresource.model.Job;
import au.edu.unsw.soacourse.humanresource.model.Job.JobStatus;

public enum JobDAO {
	instance;

	private JobXMLHandler handler;

	private JobDAO() {
		handler = new JobXMLHandler();
	}

	public Boolean add(Job job) {
		return handler.addNewItem(job);
	}

	public ArrayList<Job> getAll() {
		return handler.translateToJobs();
	}

	public Job get(String id) {
		for (Job j: handler.translateToJobs()) {
			if (j.get_jobId().equals(id))
				return j;
		}

		return null;
	}

	public void update(Job job) {
		handler.updateItem(job);
	}

	public ArrayList<Job> search(String description, String location, String position) {
		ArrayList<Job> jobs = getAll();
		ArrayList<Job> matchjobs = new ArrayList<Job>();

		if (description != null && !description.equals("")) {
			for (Job j : jobs) {
				if (j.getJobDescriptions().toLowerCase().contains(description.toLowerCase()))
					matchjobs.add(j);
			}
			jobs = matchjobs;
			matchjobs = new ArrayList<Job>();
		}


		if (location != null && !location.equals("")) {
			for (Job j : jobs) {	System.out.println();
				if (j.getLocation().contains(location))
					matchjobs.add(j);
			}
			jobs = matchjobs;
			matchjobs = new ArrayList<Job>();
		}


		if (position != null && !position.equals("")) {
			for (Job j : jobs) {
				if (j.getPositionType().contains(position))
					matchjobs.add(j);
			}
			jobs = matchjobs;
			matchjobs = new ArrayList<Job>();
		}

		return jobs;

	}
	
	public void changeStatus(String id, JobStatus status) {
		handler.changeItemStatus(id, status);
	}
}
