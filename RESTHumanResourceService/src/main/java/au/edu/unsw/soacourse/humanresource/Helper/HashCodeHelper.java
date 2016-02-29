package au.edu.unsw.soacourse.humanresource.Helper;

import au.edu.unsw.soacourse.humanresource.model.Application;
import au.edu.unsw.soacourse.humanresource.model.Job;

public class HashCodeHelper {
	
	public static String JobHashCode(Job j) {
		String str = j.get_jobId();
		str += j.getClosingDate();
		str += j.getJobDescriptions();
		str += j.getLocation();
		str += j.getPositionType();
		str += j.getSalary();
		str += j.getDepartment();
		str += j.getStatus().toString();
		
		return Integer.toString(str.hashCode());
	}
	
	public static String ApplicationHashCode(Application a) {
		String str = a.get_appId();
		str += a.get_jobId();
		str += a.getLicenseNumber();
		str += a.getFullName();
		str += a.getPostcode();
		str += a.getCoverLetter();
		str += a.getBriefResume();
		str += a.getStatus().toString();
		
		return Integer.toString(str.hashCode());
	}
}
